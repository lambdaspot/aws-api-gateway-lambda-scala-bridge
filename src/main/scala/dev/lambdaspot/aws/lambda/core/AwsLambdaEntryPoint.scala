package dev.lambdaspot.aws.lambda.core

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}

import java.io.{InputStream, OutputStream}

trait AwsLambdaEntryPoint:

  def entryPoint: RequestStreamHandler

  def apply(input: InputStream, output: OutputStream, context: Context): Unit =
    entryPoint.handleRequest(input, output, context)
