package dev.lambdaspot.aws.lambda.events

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

final case class ApiGatewayProxiedResponse(
  statusCode: Int,
  headers: Map[String, String] = Map.empty,
  multiValueHeaders: Map[String, Seq[String]] = Map.empty,
  body: Option[String],
  isBase64Encoded: Boolean = false
)

object ApiGatewayProxiedResponse:
  given JsonValueCodec[ApiGatewayProxiedResponse] = JsonCodecMaker.make
