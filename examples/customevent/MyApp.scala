//> using scala "3"
//> using jvm "17"

//> using dep "com.github.lambdaspot::aws-api-gateway-lambda-scala-bridge:0.1.4"
//> using dep "com.amazonaws:aws-lambda-java-core:1.2.2"
//> using dep "com.github.plokhotnyuk.jsoniter-scala::jsoniter-scala-core:2.23.0"
//> using dep "com.github.plokhotnyuk.jsoniter-scala::jsoniter-scala-macros:2.23.0"

import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import dev.lambdaspot.aws.lambda.core.*
import dev.lambdaspot.aws.lambda.events.ApiGatewayProxiedResponse

import scala.util.{Success, Try}

// AWS Lambda handler
object MyApp extends AwsLambdaEntryPoint:
  override lazy val entryPoint =
    new AwsLambda[PersonDto, ApiGatewayProxiedResponse]:
      override def run(person: PersonDto, context: Context): Try[ApiGatewayProxiedResponse] =
        Success(
          ApiGatewayProxiedResponse(
            statusCode = 200,
            headers = Map("Content-Type" -> "application/json"),
            body = Some(s"Hello ${person.name}!")
          )
        )

// Request object
final case class PersonDto(name: String, age: Int)
object PersonDto:
  given JsonValueCodec[PersonDto] = JsonCodecMaker.make
