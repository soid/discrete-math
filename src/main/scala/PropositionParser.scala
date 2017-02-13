import PropositionParser._

import scala.util.parsing.combinator.RegexParsers

/**
  * Parsing propositions from strings to syntax tree.
  *
  * Created by greg.temchenko on 2/8/17.
  *
  * References:
  *  http://www.donroby.com/wp/scala/parsing-expressions-with-scala-parser-combinators-2/
  *  https://github.com/droby/expression-parser/blob/master/src/main/scala/com/donroby/parsing/ExpressionParsers.scala
  */
class PropositionParser extends RegexParsers {

  // grammar

  def propositionalVar: Parser[PropositionVar] = "[prqxy][0-9]+".r  ^^ { x => PropositionVar(x.toString) }
  def parenthesizedVar = "(" ~> propositionalVar <~ ")"
  def operand = propositionalVar | parenthesizedProposition

  def parenthesizedProposition = "(" ~> proposition <~ ")"
  def operatorOr: Parser[OperatorOr] = operand ~ "v" ~ operand ^^ { case a ~ v ~ b => OperatorOr(a, b) }
  def operatorAnd: Parser[OperatorAnd] = operand ~ "^" ~ operand ^^ { case a ~ v ~ b => OperatorAnd(a, b) }
  def operatorImpl: Parser[OperatorImplication] = operand ~ ">" ~ operand ^^ { case a ~ v ~ b => OperatorImplication(a, b) }
  def operatorBiImpl: Parser[OperatorBiImplication] = operand ~ "=" ~ operand ^^ { case a ~ v ~ b => OperatorBiImplication(a, b) }
  def operatorNot: Parser[OperatorNot] = "~" ~ operand ^^ { case "~" ~ pVar => OperatorNot(pVar) }

  def proposition:Parser[Proposition] = operatorOr | operatorAnd | operatorImpl | operatorBiImpl |
    operatorNot | propositionalVar | parenthesizedVar

}


object PropositionParser {

  // syntax objects

  sealed abstract class Proposition
  case class PropositionVar(varName: String) extends Proposition
  case class OperatorOr(prop1: Proposition, prop2: Proposition) extends Proposition
  case class OperatorAnd(prop1: Proposition, prop2: Proposition) extends Proposition
  case class OperatorNot(prop: Proposition) extends Proposition
  case class OperatorImplication(prop1: Proposition, prop2: Proposition) extends Proposition
  case class OperatorBiImplication(prop1: Proposition, prop2: Proposition) extends Proposition

  /**
    * Parsing helper for parsing strings into syntax tree
    */
  def parse(expressionStr: String):Option[Proposition] = {
    val o = new PropositionParser

    val matchedOption = o.parse(o.parenthesizedProposition, expressionStr.replaceAll("\\s+", "")) match {
      case o.Success(matched,_) => Some(matched)
      case fail: o.Failure =>
        println("FAILURE: " + fail)
        None
      case err: o.Error =>
        println("ERROR: " + err)
        None
    }

    matchedOption.asInstanceOf[Option[Proposition]]
  }

}

