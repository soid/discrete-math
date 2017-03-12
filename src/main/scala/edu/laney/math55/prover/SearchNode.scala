package edu.laney.math55.prover

import edu.laney.math55.grammar.PropositionParser.Proposition

/**
  * Created by greg.temchenko on 3/9/17.
  */
class SearchNode(val prop: Proposition,
                 val parent: Option[SearchNode] = None,
                 val action: Option[Action] = None,
                 val depth: Int = 0) {
  var iteration: Int = 0

  def explainSolution(): String = {
    if (parent.isEmpty)
      prop.toString + "   Given"
    else
      parent.get.explainSolution() + "\n" + action.get.resultProp + "   " + action.get.name
  }

  override def toString = "[" + prop + "]"
}
