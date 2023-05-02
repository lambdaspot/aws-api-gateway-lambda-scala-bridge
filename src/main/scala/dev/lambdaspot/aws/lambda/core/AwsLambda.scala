package dev.lambdaspot.aws.lambda.core

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.github.plokhotnyuk.jsoniter_scala.core.*
import dev.lambdaspot.aws.lambda.events.{ApiGatewayProxiedRequest, ApiGatewayProxiedResponse}

import java.io.{InputStream, OutputStream}
import scala.util.{Failure, Success, Try}

/** Base class for AWS Lambda functions that are triggered by any events.
  * <br/><br/> Example:
  * {{{
  * import com.amazonaws.services.lambda.runtime.Context
  * import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
  * import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
  * import dev.lambdaspot.aws.lambda.core.{AwsLambda, AwsLambdaEntryPoint}
  *
  * import scala.util.{Success, Try}
  *
  * // Request event
  * final case class PersonDto(name: String, age: Int)
  * object PersonDto:
  *   given JsonValueCodec[PersonDto] = JsonCodecMaker.make
  *
  * // AWS Lambda request handler
  * object SimpleHandler extends AwsLambdaEntryPoint:
  *   override lazy val entryPoint: SimpleHandler = new SimpleHandler
  *
  * class SimpleHandler extends AwsLambda[PersonDto, GreetingsDto]:
  *   override def run(event: PersonDto, context: Context): Try[GreetingsDto] =
  *     Success(GreetingsDto(100, s"Hello ${event.name}!"))
  * }}}
  *
  * @param JsonValueCodec[In]
  *   serializer for request event
  * @param JsonValueCodec[Out]
  *   deserializer for response
  * @tparam In
  *   request event type
  * @tparam Out
  *   response type
  */
abstract class AwsLambda[In, Out](using JsonValueCodec[In], JsonValueCodec[Out]) extends RequestStreamHandler:
  def run(input: In, context: Context): Try[Out]

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit =
    Meter.measure(context.getLogger) {
      processLambda(input, output, context).fold(throw _, identity)
    }

  private def processLambda(input: InputStream, output: OutputStream, context: Context) =
    for
      request <- Try(readFromStream[In](input))
      result  <- run(request, context)
      _       <- Try(writeToStream(result, output))
    yield ()

  private def writeResponse(output: OutputStream, statusCode: Int, responseJson: String): Try[Unit] =
    Try(writeToStream(ApiGatewayProxiedResponse(statusCode, body = Some(responseJson)), output))

end AwsLambda
