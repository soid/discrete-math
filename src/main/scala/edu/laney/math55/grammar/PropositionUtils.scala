package edu.laney.math55.grammar

import edu.laney.math55.grammar.PropositionParser._

/**
  * Various utilities for working with propositions
  *
  * Created by greg.temchenko on 2/10/17.
  */
object PropositionUtils {

  /**
    * Returns all variable used in proposition
    *
    * @param proposition proposition for searching vars
    * @return
    */
  def getVariablesSet(proposition:Proposition, vars: Set[PropositionVar] = Set()) : Set[PropositionVar] = {
    proposition match {
      case prop: PropositionVar => vars + prop
      case OperatorOr(p1, p2) => vars ++ getVariablesSet(p1) ++ getVariablesSet(p2)
      case OperatorAnd(p1, p2) => vars ++ getVariablesSet(p1) ++ getVariablesSet(p2)
      case OperatorNot(prop) => vars ++ getVariablesSet(prop)
      case OperatorImplication(p1, p2) => vars ++ getVariablesSet(p1) ++ getVariablesSet(p2)
      case OperatorBiImplication(p1, p2) => vars ++ getVariablesSet(p1) ++ getVariablesSet(p2)
    }
  }

  /**
    * Generates all possible combinations of given variables values (table of values).
    *
    * @param vars variables set
    * @return
    */
  def generateVariablesValues(vars: Set[PropositionVar]) : Stream[Map[PropositionVar,Boolean]] = {
    def loop(row: Int): Stream[ Map[PropositionVar,Boolean] ] = {
      val m = (for ((pVar, col) <- vars.zipWithIndex) yield {
        val streak = Math.floor( row / scala.math.pow(2, col) )
        val value = streak % 2 == 0

        pVar -> value
      }).toMap

      // maximum number of elements is 2^vars.size
      if (row < scala.math.pow(2, vars.size)-1) m #:: loop(row + 1)
      else m #:: Stream.empty
    }

    loop(0)
  }

}
