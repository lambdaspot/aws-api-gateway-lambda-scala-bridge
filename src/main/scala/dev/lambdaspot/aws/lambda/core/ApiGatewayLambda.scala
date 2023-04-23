package dev.lambdaspot.aws.lambda.core

import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.{JsonValueCodec, readFromStream, writeToStream, writeToString}
import dev.lambdaspot.aws.lambda.events.{ApiGatewayProxiedRequest, ApiGatewayProxiedResponse}

import java.io.{InputStream, OutputStream}
import scala.util.Try

/** Base class for AWS Lambda functions that are triggered by API Gateway with
  * the proxy integration type. <br><br>Example usage:
  * {{{
  * import com.amazonaws.services.lambda.runtime.Context
  * import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
  * import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
  * import org.core.{ApiGatewayLambda, AwsLambda, AwsLambdaEntryPoint}
  * import org.core.ApiGatewayProxiedRequest
  * import scala.util.{Success, Try}
  *
  * // Give the response object
  * final case class ResponseDto(level: Int, message: String)
  * object ResponseDto:
  *   given codec: JsonValueCodec[ResponseDto] = JsonCodecMaker.make
  *
  * // Implementation of the lambda request handler supporting construction injection
  * object HelloHandler extends AwsLambdaEntryPoint:
  *   lazy val entryPoint: HelloHandler = new HelloHandler
  *
  * class HelloHandler extends ApiGatewayLambda[ResponseDto]:
  *   def run(input: ApiGatewayProxiedRequest, context: Context): Try[ResponseDto] =
  *     Success(ResponseDto(1, "Hello World!"))
  *
  * }}}
  *
  * @tparam Out
  *   the type of the response body
  */
abstract class ApiGatewayLambda[Out](using JsonValueCodec[Out]) extends AwsLambda[ApiGatewayProxiedRequest, Out]:

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit =
    val requestStart = System.currentTimeMillis()

    processLambda(input, output, context).recover { error =>
      Console.err.println(s"Request failed with error: ${error.getMessage}")
      writeResponse(output, 500, error.getMessage)
    }

    val requestDuration = System.currentTimeMillis() - requestStart
    Console.out.println(s"Request took $requestDuration ms")

  private def processLambda(input: InputStream, output: OutputStream, context: Context) =
    for
      request      <- Try(readFromStream[ApiGatewayProxiedRequest](input))
      result       <- run(request, context)
      responseJson <- Try(writeToString(result))
      _            <- writeResponse(output, 200, responseJson)
    yield ()

  private def writeResponse(output: OutputStream, statusCode: Int, responseJson: String): Try[Unit] =
    Try(writeToStream(ApiGatewayProxiedResponse(statusCode, body = Some(responseJson)), output))

end ApiGatewayLambda
