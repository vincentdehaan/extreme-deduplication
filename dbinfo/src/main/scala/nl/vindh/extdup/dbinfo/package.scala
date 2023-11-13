package nl.vindh.extdup

import shapeless.{::, HList, HNil, LabelledGeneric, Typeable, Witness}
import shapeless.ops.record.Selector

package object dbinfo {
  case class TypedTable[T : Typeable, I <: HList] private(indices: Seq[String]) {
    def withIndex[L <: HList, S](idx: Witness)(implicit gen: LabelledGeneric.Aux[T, L], selector: Selector.Aux[L, idx.T, S], ev: S =:= String): TypedTable[T, idx.T :: I] =
      TypedTable(indices :+ idx.value.asInstanceOf[Symbol].name)

    def toTableInfo: TableInfo[T] =
      TableInfo(s"resources-${Typeable[T].describe.toLowerCase}-table", indices)
  }

  case class TableInfo[T] private[dbinfo](name: String, indices: Seq[String])

  object Table {
    def create[T : Typeable] = TypedTable[T, HNil](Seq())
  }
}
