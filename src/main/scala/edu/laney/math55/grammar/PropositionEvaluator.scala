package edu.laney.math55.grammar

import edu.laney.math55.grammar.PropositionParser._

/**
  * Evaluation of proposition given known values.
  *
  * Created by greg.temchenko on 2/10/17.
  */
object PropositionEvaluator {
  def evaluate(prop: Proposition, values: Map[PropositionVar, Boolean]): Boolean = {
    prop match {
      case pVar:PropositionVar => values(pVar)
      case OperatorOr(prop1, prop2) => evaluate(prop1, values) || evaluate(prop2, values)
      case OperatorAnd(prop1, prop2) => evaluate(prop1, values) && evaluate(prop2, values)
      case OperatorNot(propNot) => ! evaluate(propNot, values)

      case OperatorImplication(prop1, prop2) => evaluate(OperatorOr(OperatorNot(prop1), prop2), values)
      case OperatorBiImplication(prop1, prop2) =>
        evaluate(OperatorAnd(OperatorOr(OperatorNot(prop1), prop2), OperatorOr(OperatorNot(prop2), prop1)),
          values)
    }
  }
}
