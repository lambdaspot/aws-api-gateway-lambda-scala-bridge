package dev.lambdaspot.fixture

import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import dev.lambdaspot.aws.lambda.core.*
import dev.lambdaspot.aws.lambda.core.AwsLambdaEntryPoint.SyntacticSugar
import dev.lambdaspot.aws.lambda.events.*

import scala.util.{Success, Try}

object SyntacticSugarHandler extends AwsLambdaEntryPoint:
  override lazy val entryPoint: ApiGatewayLambda[GreetingsResponseDto] =
    (input: ApiGatewayProxiedRequest, context: Context) => GreetingsResponseDto.of(input.pathParameters)
