import com.google.gson.Gson

import java.util.List

package object models {
  sealed trait DBObject

  case class Captures(captures: List[Capture])

  case class Capture(ssid: String, timestamp: Long,
                     position: Position, location: String,
                     hotspots: List[Hotspot]) extends DBObject

  case class Hotspot(bssid: String, timestamp: Long, level: Int) extends DBObject

  case class Location(name: String, filename: String, orientation: Int) extends DBObject

  case class Position(x: Double, y: Double)
}
