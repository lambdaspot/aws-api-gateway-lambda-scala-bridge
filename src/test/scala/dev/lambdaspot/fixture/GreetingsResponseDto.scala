package dev.lambdaspot.fixture

import com.github.plokhotnyuk.jsoniter_scala.core.JsonValueCodec
import com.github.plokhotnyuk.jsoniter_scala.macros.JsonCodecMaker

final case class GreetingsResponseDto(pleasureLeve: Int, message: String)

object GreetingsResponseDto:
  given codec: JsonValueCodec[GreetingsResponseDto] = JsonCodecMaker.make
