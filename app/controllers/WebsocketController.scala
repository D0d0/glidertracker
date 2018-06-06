package controllers

import java.net.URI

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.Materializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, Sink}
import javax.inject._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.libs.json._
import play.api.mvc._
import utils.parser.Parser

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class WebsocketController @Inject()(cc: ControllerComponents)
                                   (implicit actorSystem: ActorSystem,
                                    mat: Materializer,
                                    executionContext: ExecutionContext) extends AbstractController(cc) {
  private type WSMessage = String

  private val (chatSink, chatSource) = {

    val userName = sys.props("user.name")
    val consumerSettings =
      ConsumerSettings(actorSystem, new StringDeserializer, new StringDeserializer)
        .withBootstrapServers("127.0.0.1:9092")
        .withClientId(userName)
        .withGroupId(userName)
        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val sink = BroadcastHub.sink[WSMessage]
    val source = Consumer.plainSource(consumerSettings, Subscriptions.topics("aircraft"))
      .log("message")
      .map(x => Parser.toAircraftData(x.value()))
      .map(x => {
        JsObject(Seq(
          "id" -> JsString(x.id),
          "lat" -> JsNumber(x.latitude),
          "lng" -> JsNumber(x.longitude)
        )).toString()
      })

    source.toMat(sink)(Keep.both).run()
  }

  private val userFlow: Flow[WSMessage, WSMessage, _] = {
    Flow.fromSinkAndSource(Sink.ignore, chatSource)
  }

  def data: WebSocket = {
    WebSocket.acceptOrResult[WSMessage, WSMessage] {
      case rh if sameOriginCheck(rh) =>
        Future.successful(userFlow).map { flow =>
          Right(flow)
        }.recover {
          case e: Exception =>
            val msg = "Cannot create websocket"
            val result = InternalServerError(msg)
            Left(result)
        }

      case rejected =>
        Future.successful {
          Left(Forbidden("forbidden"))
        }
    }
  }

  def map = Action {
    Ok(views.html.map())
  }

  private def sameOriginCheck(implicit rh: RequestHeader): Boolean = {
    // The Origin header is the domain the request originates from.
    // https://tools.ietf.org/html/rfc6454#section-7

    rh.headers.get("Origin") match {
      case Some(originValue) if originMatches(originValue) =>
        true

      case Some(badOrigin) =>
        false

      case None =>
        false
    }
  }

  private def originMatches(origin: String): Boolean = {
    try {
      val url = new URI(origin)
      url.getHost == "localhost" &&
        (url.getPort match {
          case 9000 | 9001 => true;
          case _ => false
        })
    } catch {
      case e: Exception => false
    }
  }
}