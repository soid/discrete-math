/**
  * Created by greg.temchenko on 2/8/17.
  */
object PropositionSyntax {

  // syntax objects
  sealed abstract class Proposition
  case class PropositionVar(varName: String) extends Proposition
  case class OperatorOr(prop1: Proposition, prop2: Proposition) extends Proposition
  case class OperatorAnd(prop1: Proposition, prop2: Proposition) extends Proposition
  case class OperatorNot(prop: Proposition) extends Proposition

}
