# aws-api-gateway-lambda-scala-bridge

[![Release](https://jitpack.io/v/lambdaspot/aws-api-gateway-lambda-scala-bridge.svg)](https://jitpack.io/#lambdaspot/aws-api-gateway-lambda-scala-bridge)

A Scala 3 bridge
for [`aws-lambda-java-core`](https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-core) designed to
work with API Gateway Lambda proxy integration, using
[Jsoniter-Scala](https://blog.lambdaspot.dev/the-fastest-and-safest-json-parser-and-serializer-for-scala) to streamline
JSON handling for better efficiency.

Mainly meant as an example to use in blog articles and has limited functionality. However, it can evolve into a
versatile library supporting various Lambda types and IO monad integration.

The micro-library delivers an improved alternative to the official handler, introducing a streamlined interface that minimizes
boilerplate code and constructor injection support, ultimately enhancing the efficiency and enjoyment of working with
your Scala projects.

### Quickstart with [SBT](https://www.scala-sbt.org/):

1.  Add the JitPack repository, and the following dependency:

```scala
resolvers           += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.lambdaspot" % "aws-api-gateway-lambda-scala-bridge" % "0.1.3"
```

### Quickstart with [Scala-CLI](https://scala-cli.virtuslab.org/):

1.  Add the following directives to your file:

```scala
//> using repository "jitpack"
//> using dep "com.github.lambdaspot:aws-api-gateway-lambda-scala-bridge:0.1.3"
```

### Example usage:

#### Using shorthand syntax

This example illustrates developing a simplified AWS Lambda function, utilizing the more concise implementation approach
made possible by the Single Abstract Method (SAM) syntax.

```scala
package com.example
  
import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import dev.lambdaspot.aws.lambda.events.*
import dev.lambdaspot.aws.lambda.core.*
import dev.lambdaspot.aws.lambda.core.AwsLambdaEntryPoint.SyntacticSugar
import scala.language.implicitConversions
import scala.util.{Success, Try}
  
// AWS Lambda handler implementation
object HelloHandler extends AwsLambdaEntryPoint:
  override lazy val entryPoint: ApiGatewayLambda[GreetingsResponseDto] =
    (request: ApiGatewayProxiedRequest, context: Context) =>
      Success(GreetingsResponseDto("Mr.", "John Doe"))

// Given a response object with ser/deserialization codec
final case class GreetingsResponseDto(title: String, name: String)
object GreetingsResponseDto:
  given JsonValueCodec[GreetingsResponseDto] = JsonCodecMaker.make
```

The handler processes the event, serializing a response to JSON. In the given example, it is:
```json
{ "title": "Mr.", "name": "John Doe" }
```

#### Using class-based syntax with constructor injection support

This example illustrates creating a simple AWS Lambda function using a detailed implementation method. It is beneficial
for larger projects that involve multiple services. By designing the Lambda controller as a class with
constructor-injected dependencies, the application becomes more maintainable and extensible, making it easier to meet
complex project demands.

```scala
package com.example

import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import dev.lambdaspot.aws.lambda.core.*
import dev.lambdaspot.aws.lambda.events.*
import scala.util.{Success, Try}
  
// AWS Lambda handler implementation
object HelloHandler extends AwsLambdaEntryPoint:
  override lazy val entryPoint: HelloHandler = new HelloHandler(greeter)
  private lazy val greeter                   = new GreetingsService

class HelloHandler(greeter: GreetingsService) extends ApiGatewayLambda[GreetingsResponseDto]:
  override def run(input: ApiGatewayProxiedRequest, context: Context): Try[GreetingsResponseDto] =
    greeter.process(input.pathParameters)

// Given a response object with ser/deserialization codec
final case class GreetingsResponseDto(pleasureLevel: Int, message: String)
object GreetingsResponseDto:
  given JsonValueCodec[GreetingsResponseDto] = JsonCodecMaker.make
```

The handler processes the event, serializing a response to JSON. In the given example, it would be, e.g.:
```json
{ "pleasureLevel": 10, "message": "Hello World!" }
```

#### Deploying to AWS

In CloudFormation, define the handler with the `apply` as shown below:

```yaml
  MyFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: com.example.HelloHandler::apply
```
