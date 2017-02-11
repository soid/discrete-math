/**
  * Truth Table program entry point.
  *
  * Created by greg.temchenko on 2/8/17.
  */
object TruthTable {
  def main(args: Array[String]):Unit = {
    val sample = "( ((p1) v (p2)) ^ (p3) )"

//    o.parse(o.parenthesizedProposition, sample.replaceAll("\\s+", "")) match {
    val o = new PropositionalLogicParser
    o.parse(o.parenthesizedProposition, sample.replaceAll("\\s+", "")) match {
      case o.Success(matched,_) => println(matched)
      case o.Failure(msg,_) => println("FAILURE: " + msg)
      case o.Error(msg,_) => println("ERROR: " + msg)
    }
  }
}
