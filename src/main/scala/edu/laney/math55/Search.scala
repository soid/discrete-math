package edu.laney.math55

import edu.laney.math55.grammar.PropositionParser._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by greg.temchenko on 3/8/17.
  */
object Search {

  def recursiveBestFirstSearch(problem: Problem): Option[SearchNode]
    = RBSF(problem, new SearchNode(problem.p1, None, None), 99999)

  private def RBSF(problem: Problem, node: SearchNode, f_limit: Int): Option[SearchNode] = {
    if (problem.goalTest(node))
      Some(node)
    else {
      val successors = new mutable.Queue[SearchNode]

      problem.actions(node.prop).map {
        a => new SearchNode(a.resultProp, Some(node), Some(a))
      }.foreach { sn => successors.enqueue(sn) }

      if (successors.nonEmpty) {
        var result:Option[SearchNode] = None
        while (result.isEmpty && successors.nonEmpty) {
          val best = successors.dequeue()
          result = RBSF(problem, best, f_limit)
        }
        result

      } else None
    }
  }

}


class Problem(val p1: Proposition, val p2: Proposition) {

  def goalTest(testNode: SearchNode): Boolean = testNode.prop == p2

  def actions(prop:Proposition, parentPropositionConstructor:(Proposition => Proposition) = { p => p }): Seq[Action] = {
    val transformations = new ListBuffer[Action]

    val ppc = parentPropositionConstructor  // just a short alias
    prop match {
      case OperatorOr(a, b) if a==b => transformations += new Action("Idempotent Laws", ppc(a))
      case _ =>
    }
    prop match {
      case OperatorAnd(a, b) if a==b => transformations += new Action("Idempotent Laws", ppc(a))
      case _ =>
    }
    prop match {
      case OperatorNot(OperatorNot(p)) => transformations += new Action("Double Negation Law", ppc(p))
      case _ =>
    }

    prop match {
      case OperatorOr(a, b) =>
        transformations ++= actions(a, { p => ppc( OperatorOr(p, b) )} ) ++
                            actions(b, { p => ppc( OperatorOr(a, p) )} )
      case _ =>
    }

    transformations
  }

}

class SearchNode(val prop: Proposition, parent: Option[SearchNode], action: Option[Action]) {
  def explainSolution(): String = {
    if (parent.isEmpty)
      prop.toString + "   Given"
    else
      parent.get.explainSolution() + "\n" + action.get.resultProp + "   " + action.get.name
  }
}

class Action(val name: String, val resultProp: Proposition)


