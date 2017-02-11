import PropositionSyntax.{Proposition, PropositionVar}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers, PropSpec}

/**
  * Created by greg.temchenko on 2/10/17.
  */
class TruthTableSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val testPropositions = Table(
    ( "Proposition", "Expected Variables Set" ),
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

      if (matchedOption.isDefined) {
        // get all variables
        val syntaxTree = matchedOption.get

        val varsSet = TruthTable.getVars(syntaxTree)
        varsSet should be (expectedVariables)
      }
    }
  }

}
