import org.slf4j.LoggerFactory

case class CookiesSet(cMap: Map [String, BigDecimal]) {
  private val logger = LoggerFactory.getLogger(classOf[CookiesSet])

  def add(s: String, id: BigDecimal) = CookiesSet(cMap + (s -> id))

  def delete(s: String) = cMap.contains(s) match {
    case true => CookiesSet(cMap - s)
    case false => {
      logger.error("deleting cookie that doesnt contained by CookiesSet")
      CookiesSet(cMap - s)
    }
  }

  def getId(s: String) = cMap.contains(s) match {
    case true => cMap(s)
    case false => {
      logger.error("getting Id cookie that doesnt contained by CookiesSet")
      val a:BigDecimal = -1
      a
    }
  }

  def contains(s: String) = cMap.contains(s)
}
