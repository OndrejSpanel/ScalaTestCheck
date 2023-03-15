import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalacheck._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class MainTest extends AsyncFlatSpec with Matchers with ScalaCheckPropertyChecks {
  behavior of "Main"

  it should "perform computations" in {
    forAll(Gen.oneOf(0 until 10)) { i =>
      assert(i < 5) // intentional fail - we want to see how the failure is reported
    }
  }
}
