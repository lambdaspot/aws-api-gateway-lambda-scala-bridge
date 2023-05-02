package dev.lambdaspot.aws.lambda.core

import com.amazonaws.services.lambda.runtime.LambdaLogger

object Meter:
  def measure[T](logger: LambdaLogger)(code: => T): T =
    val start  = System.currentTimeMillis()
    val result = code
    val end    = System.currentTimeMillis()
    logger.log(s"Request processing took ${end - start} ms\n")
    result
