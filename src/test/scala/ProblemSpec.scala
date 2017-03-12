import edu.laney.math55.grammar.PropositionParser
import edu.laney.math55.prover.Problem
import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.prop.Tables.Table

class ProblemSpec extends PropSpec with TableDrivenPropertyChecks with Matchers {

  val testPropositions = Table(
    ( "Proposition", "Expected Transformations" ),
    ( "( p1 )", Seq("( p1 v p1)", "( p1 ^ p1)") ),
    ( "( (p ^ (~q)) v (q ^ (~p)) )", Seq("( ((p ^ (~q)) v q) ^ ((p ^ (~q)) v (~p)) )") )
  )

  property("Propositions should return correct transformations") {
    forAll(testPropositions) { case (proposition, expectedTransformations) =>
      val syntaxTree = PropositionParser.parse(proposition).get
      val stubP2 = PropositionParser.parse("( p1 )").get

      val problem = new Problem(syntaxTree, stubP2)
      val actualTransformations = problem.actions(syntaxTree).map(_.resultProp)
      expectedTransformations.foreach { expTransformationString =>
        val expTransformation = PropositionParser.parse(expTransformationString).get
        assert(actualTransformations.contains(expTransformation))
      }
    }
  }

}
