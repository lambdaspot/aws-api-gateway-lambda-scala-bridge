package dev.lambdaspot.fixture

import scala.util.{Success, Try}

private val DefaultResponse       = Success(GreetingsDto(3, "Hello World!"))
private def doGreet(name: String) = Success(GreetingsDto(10, s"Hello $name!"))

class GreetingsService:
  def process(data: Map[String, String]): Try[GreetingsDto] =
    data
      .get("name")
      .fold(DefaultResponse) { name =>
        doGreet(name)
      }
