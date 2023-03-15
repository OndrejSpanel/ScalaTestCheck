import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalacheck._
import org.scalacheck.effect.PropF
import org.scalatest.exceptions.TestFailedException
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.concurrent.Future

class MainTest extends AsyncFlatSpec with Matchers with ScalaCheckPropertyChecks {
  behavior of "Main"

  it should "perform computations" in {
    forAll(Gen.oneOf(0 until 10)) { i =>
      assert(i < 5) // intentional fail - we want to see how the failure is reported
    }
  }

  it should "perform async computations" in {

    val f: Future[Test.Result] = PropF.forAllF(Gen.oneOf(0 until 10)) { i  =>
      Future {
        assert(i < 5) // intentional fail - we want to see how the failure is reported
      }.map(_ => ()) // forAllF supports Future[Unit] by default, so we need to throw away the Assertion value
    }.check()

    f.map { result =>
      result.status match {
        case Test.Exhausted =>
          throw new Exception("Test exhausted")

        case Test.Failed(scalaCheckArgs, scalaCheckLabels) =>
          throw new Exception("Test failed")

        case Test.PropException(scalaCheckArgs, e, scalaCheckLabels) =>
          throw e // new Exception("Test thrown an exception")
        case _ =>
          assert(result.passed)
      }
    }
  }
}
