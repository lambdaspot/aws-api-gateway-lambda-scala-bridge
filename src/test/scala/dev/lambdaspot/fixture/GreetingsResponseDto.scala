package dev.lambdaspot.fixture

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

import scala.util.{Success, Try}

final case class GreetingsResponseDto(pleasureLevel: Int, message: String)

object GreetingsResponseDto:
  given JsonValueCodec[GreetingsResponseDto] = JsonCodecMaker.make

  def of(params: Map[String, String]): Try[GreetingsResponseDto] =
    Success(GreetingsResponseDto(100, params.getOrElse("name", "undefined")))
