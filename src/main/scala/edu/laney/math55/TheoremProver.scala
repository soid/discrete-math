package edu.laney.math55

import edu.laney.math55.grammar.PropositionParser
import edu.laney.math55.prover.{AStarSearch, BreadthFirstSearch, Problem}


/**
  * Theorem Prover entry point.
  *
  * Created by greg.temchenko on 3/8/17.
  */
object TheoremProver {
  def main(args: Array[String]) = {
    if (args.length != 2) {
      println("ERROR: need two specify two propositions")
    } else {
      val p1Str = args(0)
      val p2Str = args(1)

      val matchedP1Option = PropositionParser.parse(p1Str)
      val matchedP2Option = PropositionParser.parse(p2Str)

      if (matchedP1Option.isDefined && matchedP2Option.isDefined) {
        //
        val p1 = matchedP1Option.get
        val p2 = matchedP2Option.get
        println("Theorem: " + p1 + " === " + p2)
        println()

        val problem = new Problem(p1, p2)
//        val solution = new AStarSearch().search(problem)
        val solution = new BreadthFirstSearch().search(problem)

        if (solution.isDefined) {
          println("Proof:")
          println(solution.get.explainSolution())
          println("Q.E.D.")
          println()
          println("Iterations taken: " + solution.get.iteration)
        } else
          println("Solution Not Found")

      } else {
        println("Error Occurred")
      }

    }
  }
}
