package dev.lambdaspot.aws.lambda.events

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

final case class RequestIdentity(
  apiKey: Option[String],
  userArn: Option[String],
  cognitoAuthenticationType: Option[String],
  caller: Option[String],
  userAgent: Option[String],
  user: Option[String],
  cognitoIdentityPoolId: Option[String],
  cognitoAuthenticationProvider: Option[String],
  sourceIp: Option[String],
  accountId: Option[String]
)

final case class RequestContext(
  resourceId: String,
  apiId: String,
  resourcePath: String,
  httpMethod: String,
  accountId: String,
  stage: String,
  identity: RequestIdentity,
  extendedRequestId: Option[String],
  path: String
)

final case class ApiGatewayProxiedRequest(
  resource: String,
  path: String,
  httpMethod: String,
  headers: Map[String, String] = Map.empty,
  queryStringParameters: Map[String, String] = Map.empty,
  pathParameters: Map[String, String] = Map.empty,
  stageVariables: Map[String, String] = Map.empty,
  requestContext: RequestContext,
  body: Option[String] = None,
  isBase64Encoded: Option[Boolean] = Option(false)
)

object ApiGatewayProxiedRequest:
  given codec: JsonValueCodec[ApiGatewayProxiedRequest] = JsonCodecMaker.make
