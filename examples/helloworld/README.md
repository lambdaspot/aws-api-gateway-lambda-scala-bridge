## Building deployable artifact

To build a deployment package using [Scala-CLI](https://scala-cli.virtuslab.org/), run the following command:

```bash
scala-cli --power package HelloHandler.scala --assembly --preamble=false
```