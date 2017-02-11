import PropositionParser._

/**
  * Truth Table program entry point.
  *
  * Created by greg.temchenko on 2/8/17.
  */
object TruthTable {

  def main(args: Array[String]) = {
    for (expressionStr <- args) {
      val table = getTruthTableString(expressionStr)
      print(table)
    }
  }

  // helper functions

  def getTruthTableString(expressionStr: String):String = {
    // parse the expression
    val matchedOption = parse(expressionStr)

    if (matchedOption.isDefined) {
      val buf = new StringBuilder
      buf.append("Truth-table for the proposition: " + expressionStr + "\n")

      // get all variables
      val syntaxTree = matchedOption.get

      val varsSet = getVars(syntaxTree)
      val varsList = varsSet.toList
      for (pVar <- varsList) {
        buf.append(f"${pVar.varName}%5s")
      }
      buf.append("    *\n")

      val valuesRows = generateVariablesValues(varsSet)
      for (row <- valuesRows) {
        for (pVar <- varsList) {
          val value = getBoolString(row(pVar))
          buf.append(f"$value%5s")
        }
        val value = PropositionEvaluator.evaluate(syntaxTree, row)
        val valueStr = getBoolString(value)
        buf.append(f"$valueStr%5s")
        buf.append("\n")
      }
      buf.toString()
    } else {
      "Error Occurred"
    }
  }

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

  def getVars(sytaxTree:Proposition, vars: Set[PropositionVar] = Set()) : Set[PropositionVar] = {
    sytaxTree match {
      case prop: PropositionVar => vars + prop
      case OperatorOr(p1, p2) => vars ++ getVars(p1) ++ getVars(p2)
      case OperatorAnd(p1, p2) => vars ++ getVars(p1) ++ getVars(p2)
      case OperatorNot(prop) => vars ++ getVars(prop)
      case OperatorImplication(p1, p2) => vars ++ getVars(p1) ++ getVars(p2)
      case OperatorBiImplication(p1, p2) => vars ++ getVars(p1) ++ getVars(p2)
    }
  }

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

  def getBoolString(value: Boolean) = if (value) "T" else "F"

}













