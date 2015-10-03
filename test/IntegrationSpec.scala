import java.io.File

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

import play.api.test._
import play.api.test.Helpers._

import com.sksamuel.scrimage.nio.PngWriter
import com.sksamuel.scrimage.{Pixel, Image}

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {

  "Application" should {

    implicit val writer = PngWriter.NoCompression

    val file = new File("test.png")
    file.delete()

    "create a picture" in {
      Image(50, 50, (1 to 2500).map(_ => Pixel(0x70FFFFFF)).toArray).output(file)

      file.exists() must_=== true
    }
  }
}
