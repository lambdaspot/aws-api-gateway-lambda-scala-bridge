package dev.lambdaspot

import dev.lambdaspot.aws.lambda.core.data.Fixtures
import dev.lambdaspot.fixture.{GreetingsDto, HelloHandler, SyntacticSugarHandler}
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Try

class SyntacticSugarTest extends BaseTest:

  private val testSubject = SyntacticSugarHandler

  it should "should support syntactic sugar for less verbose implementation" in {
    val request =
      Fixtures
        .DummyApiGatewayRequest("GET")
        .copy(pathParameters = Map("name" -> "Hilary"))

    val result: Try[GreetingsDto] = testSubject.entryPoint.run(request, null)

    result.success.value shouldBe GreetingsDto(100, "Hilary")
  }
