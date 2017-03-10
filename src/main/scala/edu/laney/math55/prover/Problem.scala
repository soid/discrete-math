package edu.laney.math55.prover

import edu.laney.math55.grammar.PropositionParser._

import scala.collection.mutable.ListBuffer

/**
  * Problem definition.
  *
  * Created by greg.temchenko on 3/9/17.
  */
class Problem(val p1: Proposition, val p2: Proposition) {

  def goalTest(testNode: SearchNode): Boolean = testNode.prop == p2

  def actions(prop:Proposition,
              parentPropositionConstructor:(Proposition => Proposition) = { p => p }): Seq[Action] = {
    val transformations = new ListBuffer[Action]

    val ppc = parentPropositionConstructor  // just a short alias

    // try to add all propositional equivalences possible to the transformation list:
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
      case OperatorOr(a, OperatorAnd(b, c)) =>
        transformations += new Action("Distributive Law", ppc( OperatorAnd(OperatorOr(a, b), OperatorOr(a, c)) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(a, OperatorOr(b, c)) =>
        transformations += new Action("Distributive Law", ppc( OperatorOr(OperatorAnd(a, b), OperatorAnd(a, c)) ))
      case _ =>
    }
    prop match {
      case OperatorOr(a, OperatorAnd(b, c)) if a==b =>
        transformations += new Action("Absorption Law", ppc( a ))
      case _ =>
    }
    prop match {
      case OperatorAnd(a, OperatorOr(b, c)) if a==b =>
        transformations += new Action("Absorption Law", ppc( a ))
      case _ =>
    }
    prop match {
      case OperatorNot(OperatorAnd(a, b)) =>
        transformations += new Action("De Morgan’s Law", ppc( OperatorOr(OperatorNot(a), OperatorNot(b)) ))
      case _ =>
    }
    prop match {
      case OperatorNot(OperatorOr(a, b)) =>
        transformations += new Action("De Morgan’s Law", ppc( OperatorAnd(OperatorNot(a), OperatorNot(b)) ))
      case _ =>
    }
    prop match {
      case OperatorImplication(a, b) =>
        transformations += new Action("Definition of Conditional", ppc( OperatorOr(OperatorNot(a), b) ))
      case _ =>
    }
    prop match {
      case OperatorBiImplication(a, b) =>
        transformations += new Action("Definition of Bi-conditional",
          ppc( OperatorAnd(OperatorImplication(a,b), OperatorImplication(b,a)) ))
      case _ =>
    }
    prop match {
      case OperatorNot(OperatorNot(p)) => transformations += new Action("Double Negation Law", ppc(p))
      case _ =>
    }
    // Commutative Laws
    prop match {
      case OperatorOr(a, b) => transformations += new Action("Commutative Law", ppc( OperatorOr(b, a) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(a, b) => transformations += new Action("Commutative Law", ppc( OperatorAnd(b, a) ))
      case _ =>
    }

    // // // // // // //
    // reverse rules
    prop match {
      case p @ PropositionVar(_) => {
        transformations += new Action("Double Negation Law", ppc( OperatorNot(OperatorNot(p)) ))
        transformations += new Action("Idempotent Law", ppc( OperatorOr(p, p) ))
        transformations += new Action("Idempotent Law", ppc( OperatorAnd(p, p) ))
        // reverse absorption law is not implemented
      }
      case _ =>
    }
    prop match {
      case OperatorOr(a, OperatorOr(b, c)) =>
        transformations += new Action("Associative Law", ppc( OperatorOr(OperatorOr(a, b), c) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(a, OperatorAnd(b, c)) =>
        transformations += new Action("Associative Law", ppc( OperatorAnd(OperatorAnd(a, b), c) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(OperatorOr(a, b), OperatorOr(c, d)) if c == a =>
        transformations += new Action("Distributive Law", ppc( OperatorOr(a, OperatorAnd(b, c)) ))
      case _ =>
    }
    prop match {
      case OperatorOr(OperatorAnd(a, b), OperatorAnd(c, d)) if c == a =>
        transformations += new Action("Distributive Law", ppc( OperatorAnd(a, OperatorOr(b, c)) ))
      case _ =>
    }
    prop match {
      case OperatorOr(OperatorNot(a), OperatorNot(b)) =>
        transformations += new Action("De Morgan’s Law", ppc( OperatorNot(OperatorAnd(a, b)) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(OperatorNot(a), OperatorNot(b)) =>
        transformations += new Action("De Morgan’s Law", ppc( OperatorNot(OperatorOr(a, b)) ))
      case _ =>
    }
    prop match {
      case OperatorOr(OperatorNot(a), b) =>
        transformations += new Action("Definition of Conditional", ppc( OperatorImplication(a, b) ))
      case _ =>
    }
    prop match {
      case OperatorAnd(OperatorImplication(a,b), OperatorImplication(c,d)) if c==b && d==a =>
        transformations += new Action("Definition of Bi-conditional", ppc( OperatorBiImplication(a,b) ))
      case _ =>
    }

    // // // // // // // // // // // // // // // // // // // // // //
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

  // A* Search heuristic

  val prop2hm = heuristicMap(p2)
  def heuristic(prop: Proposition): Double = {
    val m = heuristicMap(prop)
    val allFields = List("vars", "or", "and", "not", "impl", "bi-impl")
    val mv = for (op <- allFields) yield {
      m.get(op).getOrElse(0) - prop2hm.get(op).getOrElse(0)
    }
    val sum = mv.map(_^2).sum
    val r = math.sqrt(sum)
    r
  }

  def heuristicMap(prop: Proposition): Map[String, Int] = {
    import scalaz._
    import Scalaz._
    prop match {
      case pVar:PropositionVar => Map("vars" -> 1)
      case OperatorNot(OperatorNot(propNot)) => Map("not" -> 1) |+| heuristicMap(propNot)
      case OperatorOr(prop1, prop2) => Map("or" -> 1) |+| heuristicMap(prop1) |+| heuristicMap(prop2)
      case OperatorAnd(prop1, prop2) => Map("and" -> 1) |+| heuristicMap(prop1) |+| heuristicMap(prop2)
      case OperatorNot(propNot) => Map("not" -> 1) |+| heuristicMap(propNot)

      case OperatorImplication(prop1, prop2) => Map("impl" -> 1) |+| heuristicMap(prop1) |+| heuristicMap(prop2)
      case OperatorBiImplication(prop1, prop2) => Map("bi-impl" -> 1) |+| heuristicMap(prop1) |+| heuristicMap(prop2)
    }
  }

}
