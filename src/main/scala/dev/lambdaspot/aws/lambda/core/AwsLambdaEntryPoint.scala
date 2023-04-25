package dev.lambdaspot.aws.lambda.core

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import dev.lambdaspot.aws.lambda.events.ApiGatewayProxiedRequest

import java.io.{InputStream, OutputStream}
import scala.language.implicitConversions
import scala.util.Try

trait AwsLambdaEntryPoint:

  def entryPoint: RequestStreamHandler

  def apply(input: InputStream, output: OutputStream, context: Context): Unit =
    entryPoint.handleRequest(input, output, context)

object AwsLambdaEntryPoint:
  private type Request        = ApiGatewayProxiedRequest
  private type AwsLambda[Dto] = ApiGatewayLambda[Dto]

  given SyntacticSugar[Dto](using JsonValueCodec[Dto]): Conversion[(Request, Context) => Try[Dto], AwsLambda[Dto]] with
    def apply(f: (Request, Context) => Try[Dto]): AwsLambda[Dto] =
      new SingleAbstractMethodInterface[Dto]:
        def apply(input: Request, context: Context): Try[Dto] = f(input, context)

  private trait SingleAbstractMethodInterface[Dto] extends AwsLambda[Dto]:
    def apply(input: Request, context: Context): Try[Dto]
    override def run(input: Request, context: Context): Try[Dto] = apply(input, context)
