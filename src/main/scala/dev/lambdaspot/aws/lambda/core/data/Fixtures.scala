package dev.lambdaspot.aws.lambda.core.data

import dev.lambdaspot.aws.lambda.events.*

object Fixtures:

  def DummyApiGatewayRequest(givenHttpMethod: String): ApiGatewayProxiedRequest = ApiGatewayProxiedRequest(
    resource = "1.1",
    path = "test",
    httpMethod = givenHttpMethod,
    requestContext = RequestContext(
      path = "/test",
      accountId = "123456789012",
      resourceId = "123456",
      apiId = "1234567890",
      stage = "dev",
      resourcePath = "/{proxy+}",
      httpMethod = givenHttpMethod,
      extendedRequestId = Some("c6af9ac6-7b61-11e6-9a41-93e8deadbeef"),
      identity = RequestIdentity(
        apiKey = None,
        userArn = None,
        cognitoAuthenticationType = None,
        caller = None,
        userAgent = None,
        user = None,
        cognitoIdentityPoolId = None,
        cognitoAuthenticationProvider = None,
        sourceIp = None,
        accountId = None
      )
    )
  )

end Fixtures
