# aws-api-gateway-lambda-scala-bridge

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

### Adding dependency

1.  Add the JitPack repository, for sbt:

    ```scala
      resolvers += "jitpack" at "https://jitpack.io"
    ```

2. Add the dependency:
   ```scala
     libraryDependencies += "dev.lambdaspot" %% "aws-api-gateway-lambda-scala-bridge" % "0.1.0"
   ```

### Example:

```scala
import dev.lambdaspot.aws.lambda.core._
import dev.lambdaspot.aws.lambda.events._

// Given a response object with ser/deserialization codec
final case class GreetingsResponseDto(pleasureLeve: Int, message: String)
object GreetingsResponseDto:
  given codec: JsonValueCodec[GreetingsResponseDto] = JsonCodecMaker.make

// AWS Lambda handler implementation
object HelloHandler extends AwsLambdaEntryPoint:
  override def entryPoint: HelloHandler = new HelloHandler(greeter)
  private lazy val greeter              = new GreetingsService

class HelloHandler(greeter: GreetingsService) extends ApiGatewayLambda[GreetingsResponseDto]:
  override def run(input: ApiGatewayProxiedRequest, context: Context): Try[GreetingsResponseDto] =
    greeter.process(input.pathParameters)
```

In CloudFormation, define the handler using the `apply` method as shown below:

```yaml
  MyFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: HelloHandler::apply
```
