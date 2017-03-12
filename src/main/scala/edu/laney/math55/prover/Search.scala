package edu.laney.math55.prover

import java.text.SimpleDateFormat
import java.util.Calendar

import edu.laney.math55.grammar.PropositionParser._

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * Created by greg.temchenko on 3/8/17.
  */
abstract class DepthFirstSearch {

  def enqueueFrontier(problem:Problem, node:SearchNode): Unit
  def isFrontierEmpty(): Boolean
  def dequeueFrontier(): SearchNode

  def search(problem: Problem): Option[SearchNode] = RBSF(problem, new SearchNode(problem.p1), 99999)

  private def RBSF(problem: Problem, node: SearchNode, f_limit: Int): Option[SearchNode] = {
    def diff(t: (Double,SearchNode)) = t._1
    val explored = Set[Proposition]()
    RBSF(problem, node, f_limit, explored, 0)
  }

  @tailrec
  private def RBSF(problem: Problem,
                   node: SearchNode,
                   f_limit: Int,
                   explored: Set[Proposition],
                   iteration: Int): Option[SearchNode] = {
    if (problem.goalTest(node)) {
      node.iteration = iteration
      Some(node)
    } else {
      problem.actions(node.prop).map { a =>
        new SearchNode(a.resultProp, Some(node), Some(a), node.depth +1)
      }.foreach { sn =>
        if (!explored.contains(sn.prop)) enqueueFrontier(problem, sn)
      }

      if (!isFrontierEmpty()) {
        val best = dequeueFrontier()

        // debugging output
        if (iteration % 10000 == 0) {
          val format = new SimpleDateFormat("hh:mm:ss.S")
          val ts = format.format(Calendar.getInstance().getTime)
          println(ts + " Iteration: " + iteration + " depth: " + node.depth)
        }

        RBSF(problem, best, f_limit, explored + best.prop, iteration +1)
      } else None
    }
  }

}

class AStarSearch extends DepthFirstSearch {
  val frontier = new mutable.PriorityQueue[(Double,SearchNode)]()(Ordering.by(diff).reverse)

  def enqueueFrontier(problem:Problem, node:SearchNode): Unit = {
    frontier.enqueue(problem.heuristic(node.prop) -> node)
  }

  def isFrontierEmpty(): Boolean = frontier.isEmpty

  def dequeueFrontier(): SearchNode = frontier.dequeue()._2

  private def diff(t: (Double,SearchNode)) = t._1
}

class BreadthFirstSearch extends DepthFirstSearch {
  val frontier = new mutable.Queue[SearchNode]

  def enqueueFrontier(problem:Problem, node:SearchNode): Unit = frontier.enqueue(node)

  def isFrontierEmpty(): Boolean = frontier.isEmpty

  def dequeueFrontier(): SearchNode = frontier.dequeue()
}


