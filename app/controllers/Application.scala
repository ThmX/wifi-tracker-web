package controllers

import java.io.File
import javax.inject.Inject

import actors.ProcessActor.Process
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import play.api.Play._
import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import com.google.gson.Gson

import play.api.Logger
import play.api.mvc._

import actors._
import models._

import scala.util.{Success, Failure, Try}

class Application @Inject() (system: ActorSystem) extends Controller {

  implicit val timeout: Timeout = 10.seconds

  val gson = new Gson()
  import gson._

  val pwd = current.getFile("locations/")
  val db = current.getFile("db.json")

  val dbActor = system.actorOf(DBActor.props(db), "db-actor")
  import DBActor._

  val processActor = system.actorOf(ProcessActor.props(pwd), "process-actor")
  import ProcessActor._

  def index = Action.async { implicit request =>
    for {
      captures <- (dbActor ? GetCaptureAndLocation("hackzurich", "technopark_0")).mapTo[List[Capture]]
      points <- (processActor ? Process(captures)).mapTo[List[Point]]
    } yield Ok(views.html.map(points))
  }

  def location(name: String) = Action {
    serveFile(name + ".png", pwd)
  }

  def config = Action { implicit request =>
    Ok("hackzurich")
  }

  def capture = Action { implicit request =>
    request.body.asText.flatMap(t => Some(fromJson(t, classOf[Capture]))) match {
      case Some(capture) =>
        dbActor ! capture
        Ok("Captured")

      case _ => NotFound
    }
  }

  def dbe = Action { implicit request =>
    dbActor ! Export()
    Ok(views.html.map(List()))
  }

  def dbi = Action { implicit request =>
    dbActor ! Import()
    Ok(views.html.map(List()))
  }

  private def serveFile(name: String, pwd: File) = Ok.sendFile(new File(pwd, name)).withHeaders(
    "Set-Cookie" -> "fileDownload=true; path=/",
    "Content-Type" -> "image/jpg",
    "Content-Disposition" -> s"attachment; filename=$name"
  )
}
