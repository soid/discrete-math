package edu.laney.math55

import edu.laney.math55.grammar.PropositionParser._

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by greg.temchenko on 3/8/17.
  */
object Search {

  def recursiveBestFirstSearch(problem: Problem): Option[SearchNode]
    = RBSF(problem, new SearchNode(problem.p1, None, None), 99999)

  @tailrec
  private def RBSF(problem: Problem, node: SearchNode, f_limit: Int,
                   successors: mutable.Queue[SearchNode] = new mutable.Queue[SearchNode]): Option[SearchNode] = {
    if (problem.goalTest(node))
      Some(node)
    else {
      problem.actions(node.prop).map {
        a => new SearchNode(a.resultProp, Some(node), Some(a))
      }.foreach { sn => successors.enqueue(sn) }

      if (successors.nonEmpty)
        RBSF(problem, successors.dequeue(), f_limit, successors)
      else None
    }
  }

}


class Problem(val p1: Proposition, val p2: Proposition) {

  def goalTest(testNode: SearchNode): Boolean = testNode.prop == p2

  def actions(prop:Proposition, parentPropositionConstructor:(Proposition => Proposition) = { p => p }): Seq[Action] = {
    val transformations = new ListBuffer[Action]

    val ppc = parentPropositionConstructor  // just a short alias

    // all laws
    prop match {
      case OperatorOr(a, b) if a==b => transformations += new Action("Idempotent Law", ppc(a))
      case _ =>
    }
    prop match {
      case OperatorAnd(a, b) if a==b => transformations += new Action("Idempotent Law", ppc(a))
      case _ =>
    }
    prop match {
      case OperatorOr(OperatorOr(a, b), c) =>
        transformations += new Action("Associative Law", ppc( OperatorOr(a, OperatorOr(b, c)) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(OperatorAnd(a, b), c) =>
        transformations += new Action("Associative Law", ppc( OperatorAnd(a, OperatorAnd(b, c)) ))
      case _ =>
    }
    prop match {
      case OperatorNot(OperatorNot(p)) => transformations += new Action("Double Negation Law", ppc(p))
      case _ =>
    }
    prop match {
      case OperatorOr(a, b) => transformations += new Action("Commutative Law", ppc( OperatorOr(b, a) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(a, b) => transformations += new Action("Commutative Law", ppc( OperatorAnd(b, a) ))
      case _ =>
    }

    // recursively build transformations for every sub-proposition
    prop match {
      case OperatorOr(a, b) =>
        transformations ++= actions(a, { p => ppc( OperatorOr(p, b) )} ) ++
                            actions(b, { p => ppc( OperatorOr(a, p) )} )
      case OperatorAnd(a, b) =>
        transformations ++= actions(a, { p => ppc( OperatorAnd(p, b) )} ) ++
                            actions(b, { p => ppc( OperatorAnd(a, p) )} )
      case OperatorNot(a) =>
        transformations ++= actions(a, { p => ppc( OperatorNot(p) )} )
      case OperatorImplication(a, b) =>
        transformations ++= actions(a, { p => ppc( OperatorImplication(p, b) )} ) ++
                            actions(b, { p => ppc( OperatorImplication(a, p) )} )
      case OperatorBiImplication(a, b) =>
        transformations ++= actions(a, { p => ppc( OperatorBiImplication(p, b) )} ) ++
                            actions(b, { p => ppc( OperatorBiImplication(a, p) )} )
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


