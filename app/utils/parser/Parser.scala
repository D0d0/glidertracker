package utils.parser

import java.util.regex.Pattern

import utils.AprsUtils

object Parser {
  val aprsPositionPattern: Pattern = Pattern.compile(PatternConstants.PATTERN_APRS_POSITION, Pattern.MULTILINE)
  val aprsStatusPattern: Pattern = Pattern.compile(PatternConstants.PATTERN_APRS_STATUS, Pattern.MULTILINE)
  val ognAircraftPattern: Pattern = Pattern.compile(PatternConstants.PATTERN_AIRCRAFT_BEACON)
  val ognReceiverPattern: Pattern = Pattern.compile(PatternConstants.PATTERN_RECEIVER_BEACON)

  def isAPRSStatus(line: String): Boolean = {
    val statusMatcher = aprsStatusPattern.matcher(line)
    if (statusMatcher.matches()) {
      val comment = statusMatcher.group("comment")
      val receiverMatcher = ognReceiverPattern.matcher(comment)
      receiverMatcher.matches
    } else {
      false
    }
  }

  def isAircraft(line: String): Boolean = {
    val positionMatcher = aprsPositionPattern.matcher(line)
    if (positionMatcher.matches()) {
      val comment = positionMatcher.group("comment")
      if (comment != null) {
        val aircraftMatcher = ognAircraftPattern.matcher(comment)
        aircraftMatcher.matches()
      } else {
        false
      }
    } else {
      false
    }
  }

  def isBeacon(line: String): Boolean = {
    val positionMatcher = aprsPositionPattern.matcher(line)
    if (positionMatcher.matches()) {
      val comment = positionMatcher.group("comment")
      if (comment != null) {
        val receiverMatcher = ognReceiverPattern.matcher(comment)
        receiverMatcher.matches()
      } else {
        true
      }
    } else {
      false
    }
  }

  case class AircraftData(id: String, longitude: Double, latitude: Double)

  implicit def toAircraftData(s: String): AircraftData = {
    val positionMatcher = aprsPositionPattern.matcher(s)
    if (positionMatcher.matches()) {
      val comment = positionMatcher.group("comment")
      val aircraftMatcher = ognAircraftPattern.matcher(comment)

      if (aircraftMatcher.matches()) {
        val latitudeSign: Double = if (positionMatcher.group("latitudeSign") == "S") -1 else 1
        val latitude: Double = latitudeSign * (AprsUtils.dmsToDeg(positionMatcher.group("latitude").toDouble / 100) +
          (if (positionMatcher.group("posExtension") == null) 0 else positionMatcher.group("latitudeEnhancement").toDouble / 1000 / 60))

        val longitudeSign: Double = if (positionMatcher.group("longitudeSign") == "W") -1 else 1
        val longitude: Double = longitudeSign * (AprsUtils.dmsToDeg(positionMatcher.group("longitude").toDouble / 100) +
          (if (positionMatcher.group("posExtension") == null) 0
          else positionMatcher.group("longitudeEnhancement").toDouble / 1000 / 60))


        AircraftData(aircraftMatcher.group("id"), longitude, latitude)
      } else {
        AircraftData("", 0, 0)
      }
    } else {
      AircraftData("", 0, 0)
    }
  }
}
