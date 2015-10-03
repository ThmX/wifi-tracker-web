package actors

import java.io.File

import scala.util.{Random, Try}

import akka.actor._

import com.sksamuel.scrimage.{Pixel, Image}
import com.sksamuel.scrimage.nio.{JpegWriter, PngWriter}

import models._


object ProcessActor {

  def props(path: File) = Props(classOf[ProcessActor], path)

  case class Process(name: String, captures: List[Capture])
}

class ProcessActor(pwd: File) extends Actor {
  import ProcessActor._

  implicit val writer = JpegWriter(9, true)

  def receive = {

    case Process(name, captures) => sender() ! Try {

      val input = new File(pwd, name + ".png")
      val output = File.createTempFile(name, ".jpg", pwd)

      val img = Image.fromFile(input)

      import img._

      Image(width, height, (1 to (width * height)).map(_ => Pixel(Random.nextInt())).toArray).output(output)

      output.getName
    }
  }
}