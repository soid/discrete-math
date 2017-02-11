import PropositionParser.PropositionVar
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}

/**
  * Created by greg.temchenko on 2/10/17.
  */
class TruthTableSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val testPropositions = Table(
    ( "Proposition", "Expected Variables Set" ),
    ( "( ~(p1) )", Set(PropositionVar("p1")) ),
    ( "( ~((p1) > (p2)) )", Set(PropositionVar("p1"), PropositionVar("p2")) ),
    ( "( ~((p1) = (p2)) )", Set(PropositionVar("p1"), PropositionVar("p2")) ),
    ( "( ~((p1) v (p2)) )", Set(PropositionVar("p1"), PropositionVar("p2")) ),
    ( "( ~((p1) ^ (p2)) )", Set(PropositionVar("p1"), PropositionVar("p2")) ),
    ( "( ((p1) v (p2)) v (p3) )", Set(PropositionVar("p1"), PropositionVar("p2"), PropositionVar("p3")) ),
    ( "( ((p1) v (p2)) v ((p1) v (p3)) )", Set(PropositionVar("p1"), PropositionVar("p2"), PropositionVar("p3")) ),
//    ( "( (p1) ^ (p2) v (p3) )", Set(PropositionVar("p1"), PropositionVar("p2"), PropositionVar("p3")) ),
    ( "( (p1) ^ ((p2) v (p3)) )", Set(PropositionVar("p1"), PropositionVar("p2"), PropositionVar("p3")) ),
    ( "( ((p1) v (p2)) ^ ((p3) v (p4)) )", Set(PropositionVar("p1"), PropositionVar("p2"), PropositionVar("p3"), PropositionVar("p4")) ),
    ( "( ~((p1) v (p2)) )", Set(PropositionVar("p1"), PropositionVar("p2")) )
//    ( "( (~(p1) v (p2)) )", Set(PropositionVar("p1"), PropositionVar("p2")) )
  )

  property("Propositions should return correct set of variables") {
    forAll(testPropositions) { case (proposition, expectedVariables) =>
      // parse the expression
      val matchedOption = TruthTable.parse(proposition)
      assert(matchedOption.isDefined)

      // get all variables
      val syntaxTree = matchedOption.get

      val varsSet = TruthTable.getVars(syntaxTree)
      varsSet should be (expectedVariables)
    }
  }

  val testVarsGenerators = Table(
    ("Vars Set", "Expected Table"),

    (
      Set(PropositionVar("p1"), PropositionVar("p2")),
      Stream(
        Map(PropositionVar("p1") -> true, PropositionVar("p2") -> true),
        Map(PropositionVar("p1") -> false, PropositionVar("p2") -> true),
        Map(PropositionVar("p1") -> true, PropositionVar("p2") -> false),
        Map(PropositionVar("p1") -> false, PropositionVar("p2") -> false)
      )
    )

  )

  property("Generator works correctly") {
    forAll(testVarsGenerators) { case (vars, expectedTable) =>
      val lazyTable = TruthTable.generateVariablesValues(vars)
      val table = lazyTable.take(20)

      table.size should be(4)
      table should be(expectedTable)
    }
  }

  property("Evaluator works correctly") {
    forAll(testPropositions) { case (proposition, expectedVariables) =>
      // parse the expression
      val matchedOption = TruthTable.parse(proposition)
      assert(matchedOption.isDefined)

      // get all variables
      val syntaxTree = matchedOption.get

      val varsSet = TruthTable.getVars(syntaxTree)
      val values = TruthTable.generateVariablesValues(varsSet)
      val results = values.map(PropositionEvaluator.evaluate(syntaxTree, _))

      results should contain (true) // only check that propositions are satisfiable
    }
  }

}
