package mypackage

import org.slf4j.LoggerFactory

class CookiesSet(var cMap: Map [String, BigDecimal]) {
  private val logger = LoggerFactory.getLogger(classOf[CookiesSet])

  def add(s: String, id: BigDecimal):Unit ={
    cMap = cMap + (s -> id)
  }

  def delete(s: String):Unit = cMap.contains(s) match {
    case true => {
      cMap = cMap - s
    }
    case false => {
      logger.error("deleting cookie that doesnt contained by CookiesSet")
    }
  }

  def getId(s: String):BigDecimal = cMap.contains(s) match {
    case true => cMap(s)
    case false => {
      logger.error("getting Id cookie that doesnt contained by CookiesSet")
      val a:BigDecimal = -1
      a
    }
  }

  def contains(s: String):Boolean = cMap.contains(s)
}
