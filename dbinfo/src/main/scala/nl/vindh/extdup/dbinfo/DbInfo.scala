package nl.vindh.extdup.dbinfo

import nl.vindh.extdup.domain.Foo
import shapeless._
import shapeless.ops.record._
import scala.language.experimental.macros

case class TypedTable[T : Typeable, I <: HList] private(indices: Seq[String]) {
  def withIndex[L <: HList, S](idx: Witness)(implicit gen: LabelledGeneric.Aux[T, L], selector: Selector.Aux[L, idx.T, S], ev: S =:= String): TypedTable[T, idx.T :: I] =
    TypedTable(indices :+ idx.value.asInstanceOf[Symbol].name)

  def toTable: Table =
    Table(Typeable[T].describe.toLowerCase, indices)
}

case class Table private[dbinfo](name: String, indices: Seq[String])

object Table {
  trait TableCreator[T] {

  }

  def create[T : Typeable] = TypedTable[T, HNil](Seq())
}

object Tables {
  val tables = Table.create[Foo]
    .withIndex(Symbol("name")) :: HNil
}
