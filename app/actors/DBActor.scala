package actors

import java.io.{FileReader, FileWriter, File}
import scala.collection.convert.wrapAll

import akka.actor._
import com.google.gson.Gson

import models._

object DBActor {

  def props(db: File) = Props(classOf[DBActor], db)

  case class Import()
  case class Export()

  case class Inserted()

  case class GetCapture(ssid: String)
  case class GetHotspot(bssid: String)
  case class GetLocation(location: String)
  case class GetCaptureAndLocation(ssid: String, location: String)
}

class DBActor(db: File) extends Actor with ActorLogging {
  import DBActor._

  self ! Import()

  var captures = List[Capture]()

  var locations = List[Location]()

  def receive = {

    case Import() if db.exists() =>
      val gson = new Gson()
      val Captures(c) = gson.fromJson(new FileReader(db), classOf[Captures])
      captures = wrapAll.iterableAsScalaIterable(c).toList
      log.info(captures.toString())

    case Export() =>
      val gson = new Gson()
      val writer = new FileWriter(db)
      try {
        gson.toJson(Captures(wrapAll.seqAsJavaList(captures)), writer)
      } finally {
        writer.close()
      }

    case GetCapture(ssid) =>
      sender() ! captures.filter(_.ssid == ssid)

    case GetHotspot(bssid) =>

    case GetLocation(name) =>
      sender() ! locations.filter(l => l.name == name)

    case GetCaptureAndLocation(ssid, location) =>
      sender() ! captures.filter(c => c.ssid == ssid && c.location == location)

    case capture: Capture =>
      log.info("Captured: " + capture.toString)
      captures ::= capture

    case location: Location =>
      locations ::= location

  }
}