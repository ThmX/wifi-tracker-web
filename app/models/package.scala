import com.google.gson.Gson

import java.util.List

package object models {
  sealed trait DBObject

  case class Capture(id: Long, SSID: String, spots: List[CaptureSpot]) extends DBObject

  case class CaptureSpot(id: Long, BSSID: String, timestamp: Long, level: Int) extends DBObject

  case class MapPic(id: Long, name: String, filename: String, orientation: Int) extends DBObject
}
