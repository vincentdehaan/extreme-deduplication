package nl.vindh.extdup.dbinfo

import nl.vindh.extdup.domain.{Bar, Foo}
import shapeless._

import scala.language.experimental.macros

object Tables {
  val tables =
    Table.create[Foo]
      .withIndex(Symbol("name")) ::
    Table.create[Bar]
      .withIndex(Symbol("barName")) :: HNil
}
