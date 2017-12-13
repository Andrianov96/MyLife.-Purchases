import mypackage.CookiesSet
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class CookiesSetTest extends FlatSpec with Matchers {
  trait mocks{
    val emptyCookieSet = new CookiesSet(Map[String, BigDecimal]())
    val randomValue:BigDecimal = Random.nextInt()
    val randomStr = Random.nextString(10)

  }

  "contains" should "return true if element is in cookieSet" in new mocks{
    emptyCookieSet.add(randomStr, randomValue)
    emptyCookieSet.contains(randomStr) should be(true)
  }
  it should "return false if element is not in cookieSet" in new mocks{
    emptyCookieSet.add(randomStr, randomValue)
    emptyCookieSet.contains(randomStr.concat(randomStr)) should be(false)
  }

  "getId" should "return id if element is in cookieSet" in new mocks{
    emptyCookieSet.add(randomStr, randomValue)
    emptyCookieSet.getId(randomStr) should be(randomValue)
  }

  it should "return -1 on not contained element" in new mocks {
    emptyCookieSet.getId(randomStr) should be(-1)
  }

  "delete" should "delete contained element" in new mocks {
    emptyCookieSet.add(randomStr, randomValue)
    emptyCookieSet.delete(randomStr)
    emptyCookieSet.contains(randomStr) should be(false)
  }
}
