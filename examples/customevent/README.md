## Example AWS Lambda Application

This example illustrates creating a simple AWS Lambda function using a custom request event, and explicit API Gateway
response, written by hand.

## Building deployable artifact

To build a deployment package using [Scala-CLI](https://scala-cli.virtuslab.org/), run the following command:

```bash
scala-cli --power package MyApp.scala --assembly --preamble=false
```

Run the function locally
using [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html):

```bash
sam local start-api
```

Visit http://localhost:3000/hello/Jerry to see the result.