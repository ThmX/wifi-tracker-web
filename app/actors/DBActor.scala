package actors

import akka.actor._

import anorm._
import play.api.db.DB
import play.api.Play.current

import models._

object DBActor {

  def props = Props[DBActor]

  case class Insert(obj: DBObject)

  case class GetCapture(ssid: String)
  case class GetCaptureSpot(bssid: String)
  case class GetMapPic(name: String)
}

class DBActor extends Actor {
  import DBActor._

  def receive = {

    /*
    obj match {
      case Capture(id, ssid, spots) =>
        SQL("select * from CAPTURE where ssid = {ssid}").on('ssid -> ssid)

      case CaptureSpot(id, bssid, timestamp, level) =>
        SQL("select * from CAPTURE where bssid = {bssid}").on('bssid -> bssid)

      case MapPic(id, name, filename, orientation) =>
        SQL("select * from CAPTURE where name = {name}").on('name -> name)
    }
     */

    case Insert(obj) => DB.withConnection { implicit c =>
      val result: Boolean = SQL("Select 1").execute()
      sender() ! result

    }
  }
}