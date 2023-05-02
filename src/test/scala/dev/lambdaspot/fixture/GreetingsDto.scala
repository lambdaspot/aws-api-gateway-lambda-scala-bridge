package dev.lambdaspot.fixture

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import scala.util.{Success, Try}

final case class GreetingsDto(pleasureLevel: Int, message: String)

object GreetingsDto:
  given JsonValueCodec[GreetingsDto] = JsonCodecMaker.make

  def of(params: Map[String, String]): Try[GreetingsDto] =
    Success(GreetingsDto(100, params.getOrElse("name", "undefined")))
