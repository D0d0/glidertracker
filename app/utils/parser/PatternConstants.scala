package utils.parser

object PatternConstants {
  val PATTERN_APRS_POSITION = "^(?<callsign>.+?)>(?<dstcall>[A-Z0-9]+),((?<relay>[A-Za-z0-9]+)\\*)?.*,(?<receiver>.+?):/(?<time>\\d{6})+h(?<latitude>\\d{4}\\.\\d{2})(?<latitudeSign>N|S)(?<symboltable>.)(?<longitude>\\d{5}\\.\\d{2})(?<longitudeSign>E|W)(?<symbol>.)(?<courseExtension>(?<course>\\d{3})/(?<groundSpeed>\\d{3}))?/A=(?<altitude>\\d{6})(?<posExtension>\\s!W((?<latitudeEnhancement>\\d)(?<longitudeEnhancement>\\d))!)?(?:\\s(?<comment>.*))?$"
  val PATTERN_APRS_STATUS = "(?<callsign>.+?)>(?<dstcall>[A-Z0-9]+),.+,(?<receiver>.+?):>(?<time>\\d{6})+h\\s(?<comment>.*)$"

  val PATTERN_RECEIVER_BEACON: String = "" +
    "(?:" + "v(?<version>\\d+\\.\\d+\\.\\d+)" +
    "(?:\\.(?<platform>.+?))?" + "\\s)?" +
    "CPU:(?<cpuLoad>[\\d.]+)\\s" +
    "RAM:(?<ramFree>[\\d.]+)/(?<ramTotal>[\\d.]+)MB\\s" +
    "NTP:(?<ntpOffset>[\\d.]+)ms/(?<ntpCorrection>[+-][\\d.]+)ppm\\s" +
    "(?:(?<voltage>[\\d.]+)V\\s)?" +
    "(?:(?<amperage>[\\d.]+)A\\s)?" +
    "(?:(?<cpuTemperature>[+-][\\d.]+)C\\s*)?" +
    "(?:(?<visibleSenders>\\d+)/(?<senders>\\d+)Acfts\\[1h\\]\\s*)?" +
    "(?:RF:" + "(?:" + "(?<rfCorrectionManual>[+-][\\d]+)" +
    "(?<rfCorrectionAutomatic>[+-][\\d.]+)ppm/" + ")?" +
    "(?<signalQuality>[+-][\\d.]+)dB" +
    "(?:/(?<sendersSignalQuality>[+-][\\d.]+)dB@10km\\[(?<sendersMessages>\\d+)\\])?" +
    "(?:/(?<goodSendersSignalQuality>[+-][\\d.]+)dB@10km\\[(?<goodSenders>\\d+)/(?<goodAndBadSenders>\\d+)\\])?" + ")?"

  val PATTERN_AIRCRAFT_BEACON: String = "" +
    "id(?<details>\\w{2})(?<id>\\w{6}?)\\s?" +
    "(?:(?<climbRate>[+-]\\d+?)fpm\\s?)?" +
    "(?:(?<turnRate>[+-][\\d.]+?)rot\\s?)?" +
    "(?:FL(?<flightLevel>[\\d.]+)\\s?)?" +
    "(?:(?<signalQuality>[\\d.]+?)dB\\s?)?" +
    "(?:(?<errors>\\d+)e\\s?)?" +
    "(?:(?<frequencyOffset>[+-][\\d.]+?)kHz\\s?)?" +
    "(?:gps(?<gpsAccuracy>\\d+x\\d+)\\s?)?" +
    "(?:s(?<flarmSoftwareVersion>[\\d.]+)\\s?)?" +
    "(?:h(?<flarmHardwareVersion>[\\dA-F]{2})\\s?)?" +
    "(?:r(?<flarmId>[\\dA-F]+)\\s?)?" +
    "(?:(?<signalPower>[+-][\\d.]+)dBm\\s?)?" +
    "(?:(?<proximity>(hear[\\dA-F]{4}\\s?)+))?"
}