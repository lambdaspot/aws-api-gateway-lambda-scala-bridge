//> using scala "3"
//> using jvm "17"
//> using repository "jitpack"
//> using dep "com.github.lambdaspot:aws-lambda-scala-bridge:0.1.5"
//> using dep "com.amazonaws:aws-lambda-java-core:1.2.2"
//> using dep "com.github.plokhotnyuk.jsoniter-scala::jsoniter-scala-core:2.23.0"
//> using dep "com.github.plokhotnyuk.jsoniter-scala::jsoniter-scala-macros:2.23.0"

import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import dev.lambdaspot.aws.lambda.events.*
import dev.lambdaspot.aws.lambda.core.*
import dev.lambdaspot.aws.lambda.core.AwsLambdaEntryPoint.SyntacticSugar
import scala.language.implicitConversions

import scala.util.{Success, Try}

// AWS Lambda handler
object HelloHandler extends AwsLambdaEntryPoint:
  override lazy val entryPoint: ApiGatewayLambda[GreetingsDto] =
    (request: ApiGatewayProxiedRequest, context: Context) =>
      Success(GreetingsDto(100, request.pathParameters.getOrElse("name", "Stranger")))

// Response object
final case class GreetingsDto(pleasureLevel: Int, name: String)
object GreetingsDto:
  given JsonValueCodec[GreetingsDto] = JsonCodecMaker.make
