import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalacheck._
import org.scalacheck.effect.PropF
import org.scalatest.exceptions.TestFailedException
import org.scalatestplus.scalacheck.{CheckerAsserting, Checkers, ScalaCheckPropertyChecks}

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
    }.check().map { result =>
      val propResult = result.status match {
        case _: Test.Proved =>
          Prop.Result(Prop.Proof)
        case Test.Exhausted =>
          Prop.Result(Prop.Undecided)
        case Test.Failed(scalaCheckArgs, scalaCheckLabels) =>
          Prop.Result(status = Prop.False, args = scalaCheckArgs, labels = scalaCheckLabels)
        case Test.PropException(scalaCheckArgs, e, scalaCheckLabels) =>
          Prop.Result(status = Prop.Exception(e), args = scalaCheckArgs, labels = scalaCheckLabels)
        case _ if result.passed =>
          Prop.Result(Prop.True)
        case _ =>
          Prop.Result(Prop.False)
      }

      Checkers.check(Prop(_ => propResult))
    }

  }
}
