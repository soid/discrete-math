import PropositionSyntax._

import scala.util.parsing.combinator.RegexParsers

/**
  * Created by greg.temchenko on 2/8/17.
  *
  * References:
  *  http://www.donroby.com/wp/scala/parsing-expressions-with-scala-parser-combinators-2/
  *  https://github.com/droby/expression-parser/blob/master/src/main/scala/com/donroby/parsing/ExpressionParsers.scala
  */
class PropositionalLogicParser extends RegexParsers {
  def propositionalVar: Parser[PropositionVar] = "p[0-9]+".r  ^^ { x => PropositionVar(x.toString) }
  def parenthesizedVar = "(" ~> propositionalVar <~ ")"
  def operand = propositionalVar | parenthesizedProposition

  def parenthesizedProposition = "(" ~> proposition <~ ")"
  def operatorOr: Parser[OperatorOr] = operand ~ "v" ~ operand ^^ { case a ~ v ~ b => OperatorOr(a, b) }
  def operatorAnd: Parser[OperatorAnd] = operand ~ "^" ~ operand ^^ { case a ~ v ~ b => OperatorAnd(a, b) }
  def operatorNot: Parser[OperatorNot] = "~" ~ operand ^^ { case "~" ~ pVar => OperatorNot(pVar) }

  def proposition:Parser[Proposition] = operatorOr | operatorAnd | operatorNot | propositionalVar | parenthesizedVar
}
