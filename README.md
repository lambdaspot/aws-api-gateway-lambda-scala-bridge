# aws-lambda-scala-bridge

[![Release](https://jitpack.io/v/lambdaspot/aws-lambda-scala-bridge.svg)](https://jitpack.io/#lambdaspot/aws-lambda-scala-bridge)

A strawman implementation of a Scala 3 bridge
for [`aws-lambda-java-core`](https://github.com/aws/aws-lambda-java-libs/tree/main/aws-lambda-java-core),
utilizing [Jsoniter-Scala](https://blog.lambdaspot.dev/the-fastest-and-safest-json-parser-and-serializer-for-scala) to
streamline JSON handling for better efficiency.

Mainly **meant as an example** to use in **blog articles** and has **limited functionality**. However, it **can evolve**
into a
**versatile library** supporting more Lambda features and IO monad integration.

The micro-library delivers an improved alternative to the official handler, introducing a streamlined interface that
minimizes
boilerplate code and constructor injection support, ultimately enhancing the efficiency and enjoyment of working with
your Scala projects.

### Quickstart with [SBT](https://www.scala-sbt.org/):

1. Add the JitPack repository, and the following dependency:

```scala
resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.lambdaspot" % "aws-lambda-scala-bridge" % "0.1.4"
```

### Quickstart with [Scala-CLI](https://scala-cli.virtuslab.org/):

1. Add the following directives to your file:

```scala
//> using repository "jitpack"
//> using dep "com.github.lambdaspot:aws-lambda-scala-bridge:0.1.4"
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
object HelloHandler extends AwsLambdaEntryPoint

:
  override lazy val entryPoint: ApiGatewayLambda[GreetingsDto] =
    (request: ApiGatewayProxiedRequest, context: Context) =>
      Success(GreetingsDto("Mr.", "John Doe"))

  // Given a response object with serialization codec
  final case class GreetingsDto(title: String, name: String)

  object GreetingsDto

:
  given JsonValueCodec
  [GreetingsDto
  ] = JsonCodecMaker.make
```

The handler processes the event, serializing a response to JSON. In the given example, it is:

```json
{
  "statusCode": 200,
  "headers": {
    "Content-Type": "application/json"
  },
  "body": "{ \"title\": \"Mr.\", \"name\": \"John Doe\" }"
}
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
object HelloHandler extends AwsLambdaEntryPoint

:
  override lazy val entryPoint: HelloHandler = new HelloHandler(greeter)
  private lazy val greeter = new GreetingsService

  class HelloHandler(greeter: GreetingsService) extends ApiGatewayLambda[GreetingsDto]

:
  override def run(input: ApiGatewayProxiedRequest, context: Context): Try[GreetingsDto] =
    greeter.process(input.pathParameters)

  // Given a response object with serialization codec
  final case class GreetingsDto(pleasureLevel: Int, message: String)

  object GreetingsDto

:
  given JsonValueCodec
  [GreetingsDto
  ] = JsonCodecMaker.make
```

The handler processes the event, serializing a response to JSON. In the given example, it would be, e.g.:

```json
{
  "statusCode": 200,
  "headers": {
    "Content-Type": "application/json"
  },
  "body": "{ \"pleasureLevel\": 10, \"message\": \"Hello World!\" }"
}
```

#### A support for custom request events:

It's possible to handle any request event type. For example, the following code:

```scala
import com.amazonaws.services.lambda.runtime.Context
import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker
import dev.lambdaspot.aws.lambda.core.{AwsLambda, AwsLambdaEntryPoint}

import scala.util.{Success, Try}

// AWS Lambda request handler
object SimpleHandler extends AwsLambdaEntryPoint

:
override lazy val entryPoint: SimpleHandler = new SimpleHandler

class SimpleHandler extends AwsLambda[PersonDto, ApiGatewayProxiedResponse]

:
override def run(event: PersonDto, context: Context): Try[ApiGatewayProxiedResponse] =
  Success(
    ApiGatewayProxiedResponse(
      statusCode = 200,
      headers = Map("Content-Type" -> "application/json"),
      body = Some(s"Hello ${event.name}!")
    )
  )

// Given request event with deserialization codec
final case class PersonDto(name: String, age: Int)

object PersonDto

:
given JsonValueCodec
[PersonDto
] = JsonCodecMaker.make

// Given a response object with serialization codec
final case class GreetingsDto(pleasureLevel: Int, message: String)

object GreetingsDto

:
given JsonValueCodec
[GreetingsDto
] = JsonCodecMaker.make
```

Handles JSON event:

```json
{
  "name": "John Doe",
  "age": 44
}
```

The handler processes the event, serializing a response to JSON. In the given example, it is:

```json
{
  "statusCode": 200,
  "headers": {
    "Content-Type": "application/json"
  },
  "body": "Hello John Doe!"
}
```

#### Deploying to AWS

In CloudFormation, define the handler with the `apply` as shown below:

```yaml
MyFunction:
  Type: AWS::Serverless::Function
  Properties:
    Handler: com.example.HelloHandler::apply
```
