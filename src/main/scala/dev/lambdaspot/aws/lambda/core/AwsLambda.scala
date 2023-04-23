package dev.lambdaspot.aws.lambda.core

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.github.plokhotnyuk.jsoniter_scala.core.*
import dev.lambdaspot.aws.lambda.events.{ApiGatewayProxiedRequest, ApiGatewayProxiedResponse}

import java.io.{InputStream, OutputStream}
import scala.util.Try

abstract class AwsLambda[In, Out](using JsonValueCodec[In], JsonValueCodec[Out]) extends RequestStreamHandler:
  def run(input: In, context: Context): Try[Out]
