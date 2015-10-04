package actors

import java.io.File

import play.api.libs.json.Json

import scala.util.{Random, Try}
import scala.collection.convert.wrapAll

import akka.actor._


import models._

object ProcessActor {

  def props(path: File) = Props(classOf[ProcessActor], path)

  case class Point(x: Double, y: Double, alpha: Double)
  implicit val pointWrites = Json.writes[Point]

  case class Process(captures: List[Capture])
}

class ProcessActor(pwd: File) extends Actor with ActorLogging {
  import ProcessActor._

  def receive = {

    case Process(captures) => sender() ! captures.map { c =>
      val Position(x, y) = c.position
      val hotspots = wrapAll.iterableAsScalaIterable(c.hotspots)
      val alpha = hotspots.foldLeft(0)((a,b) => a + b.level) / hotspots.size

      Point(x, y, alpha)
    }
  }
}