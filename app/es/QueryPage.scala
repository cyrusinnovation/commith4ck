package es

case class QueryPage(from: Int, size: Int) {
  def next() = {
    this.copy(from = from+size)
  }
}

object QueryPage {
  val Default = QueryPage(0, 10)
}
