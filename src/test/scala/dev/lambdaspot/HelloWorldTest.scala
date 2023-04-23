package dev.lambdaspot

import dev.lambdaspot.aws.lambda.core.data.Fixtures
import org.scalatest.*
import dev.lambdaspot.fixture.HelloHandler
import dev.lambdaspot.fixture.GreetingsResponseDto
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Try

class HelloWorldTest extends AnyFlatSpec with Matchers with TryValues {

  private val testSubject = HelloHandler

  it should "say hello when name is given" in {
    val request =
      Fixtures
        .DummyApiGatewayRequest("GET")
        .copy(pathParameters = Map("name" -> "Hilary"))

    val result: Try[GreetingsResponseDto] = testSubject.entryPoint.run(request, null)

    result.success.value shouldBe GreetingsResponseDto(10, "Hello Hilary!")
  }

  it should "say hello to stranger" in {
    val request =
      Fixtures
        .DummyApiGatewayRequest("GET")
        .copy(pathParameters = Map.empty)

    val result: Try[GreetingsResponseDto] = testSubject.entryPoint.run(request, null)

    result.success.value shouldBe GreetingsResponseDto(3, "Hello World!")
  }
  
}
