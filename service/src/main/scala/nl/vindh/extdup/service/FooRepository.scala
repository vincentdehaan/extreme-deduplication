package nl.vindh.extdup.service

import nl.vindh.extdup.dbinfo.{TableInfo, Tables, TypedTable}
import nl.vindh.extdup.domain.{Bar, Foo}
import org.scanamo.generic.auto._
import shapeless.PolyDefns.Case1
import shapeless._
import shapeless.ops.hlist._
import shapeless.labelled.FieldType
import shapeless.ops.record.Selector
import shapeless.tag.Tagged
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

import scala.concurrent.{ExecutionContext, Future}

class FooRepository(implicit val ec: ExecutionContext, val client: DynamoDbClient) extends Repository[Foo] {
  implicit val thisTable = Tables.tables.flatMap(tableSelector).head
  val tableInfo: TableInfo[Foo] = thisTable.toTableInfo
  println(table)

  def getByName(name: String): Future[Foo] =
    getByIndex(Symbol("name"), name)

}
