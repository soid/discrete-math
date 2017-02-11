/**
  * Created by greg.temchenko on 2/8/17.
  */
object PropositionSyntax {

  // syntax objects
  sealed abstract class Proposition
  case class PropositionVar(name: String) extends Proposition
  case class OperatorOr(var1: Proposition, var2: Proposition) extends Proposition
  case class OperatorAnd(var1: Proposition, var2: Proposition) extends Proposition

}
