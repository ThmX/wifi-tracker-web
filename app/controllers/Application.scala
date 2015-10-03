package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.Future
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import com.google.gson.Gson

import play.api.Logger
import play.api.mvc._

import actors._
import models._

class Application @Inject() (system: ActorSystem) extends Controller {

  implicit val timeout: Timeout = 3.seconds

  val gson = new Gson()
  import gson._

  val dbActor = system.actorOf(DBActor.props, "db-actor")
  import DBActor._

  def index = Action { implicit request =>
    Ok(views.html.map())
  }

  def config = Action { implicit request =>
    Ok("hackzurich")
  }

  def capture = Action.async { implicit request =>
    request.body.asText.flatMap(t => Some(fromJson(t, classOf[Capture]))) match {
      case Some(capture) => for {
          abc <- dbActor ? Insert(capture)
        } yield {
          Logger.info("Result from dbActor is " + abc)
          Ok(toJson(capture))
        }

      case _ => Future.successful(NotFound)
    }
  }

}
