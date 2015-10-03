import java.io.File

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.junit.runner._
import org.specs2.matcher.Matchers
import org.specs2.mutable._
import org.specs2.runner._

import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.{Image, Pixel}

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class ProcessActorSpec extends TestKit(ActorSystem("processActorSpec"))
  with ImplicitSender
  with Matchers {


}
