import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalacheck._
import org.scalacheck.effect.PropF
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

    PropF.forAllF(Gen.oneOf(0 until 10)) { i  =>
      Future {
        assert(i < 5) // intentional fail - we want to see how the failure is reported
      }.map(_ => ()) // forAllF supports Future[Unit] by default, so we need to throw away the Assertion value
    }.check().map { r =>
      assert(r.passed) // works, but does not propagate the test result
    }
  }

}
