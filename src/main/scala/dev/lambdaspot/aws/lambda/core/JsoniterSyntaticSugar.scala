package dev.lambdaspot.aws.lambda.core

import com.github.plokhotnyuk.jsoniter_scala.core.*

import scala.util.Try

trait JsoniterSyntaticSugar:

  extension (payload: String)
    def fromJson[T](using JsonValueCodec[T]): Try[T] =
      Try(readFromString(payload))

  extension [T](source: T)
    def toJson(using JsonValueCodec[T]): Try[String] =
      Try(writeToString(source))
