package dev.lambdaspot

import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import dev.lambdaspot.aws.lambda.core.data.Fixtures
import dev.lambdaspot.aws.lambda.core.{ApiGatewayLambda, AwsLambda, AwsLambdaEntryPoint}
import dev.lambdaspot.aws.lambda.events.ApiGatewayProxiedResponse
import dev.lambdaspot.fixture.HelloHandler.greeter
import dev.lambdaspot.fixture.{GreetingsDto, GreetingsService, HelloHandler}
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Success, Try}

class HelloWorldTest extends BaseTest:

  private val testSubject = HelloHandler

  it should "say hello when name is given" in {
    val request =
      Fixtures
        .DummyApiGatewayRequest("GET")
        .copy(pathParameters = Map("name" -> "Hilary"))

    val result: Try[GreetingsDto] = testSubject.entryPoint.run(request, null)

    result.success.value shouldBe GreetingsDto(10, "Hello Hilary!")
  }

  it should "say hello to stranger" in {
    val request =
      Fixtures
        .DummyApiGatewayRequest("GET")
        .copy(pathParameters = Map.empty)

    val result: Try[GreetingsDto] = testSubject.entryPoint.run(request, null)

    result.success.value shouldBe GreetingsDto(3, "Hello World!")
  }

  it should "support simple custom request event" in {
    import PersonApp.*
    val request     = PersonDto("Hilary", 44)
    val testSubject = SimpleHandler

    val result: Try[ApiGatewayProxiedResponse] = testSubject.entryPoint.run(request, null)

    result.success.value.body shouldBe Some("Hello Hilary!")
  }

end HelloWorldTest

object PersonApp:

  // Request event
  final case class PersonDto(name: String, age: Int)

  object PersonDto:
    given JsonValueCodec[PersonDto] = JsonCodecMaker.make

  // AWS Lambda request handler
  object SimpleHandler extends AwsLambdaEntryPoint:
    override lazy val entryPoint: SimpleHandler = new SimpleHandler

  class SimpleHandler extends AwsLambda[PersonDto, ApiGatewayProxiedResponse]:
    override def run(event: PersonDto, context: Context): Try[ApiGatewayProxiedResponse] =
      Success(
        ApiGatewayProxiedResponse(
          statusCode = 200,
          headers = Map("Content-Type" -> "application/json"),
          body = Some(s"Hello ${event.name}!")
        )
      )
