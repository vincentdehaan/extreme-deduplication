package nl.vindh.extdup.cloudformation

import com.monsanto.arch.cloudformation.model.resource.BillingMode.PAY_PER_REQUEST
import com.monsanto.arch.cloudformation.model.resource._
import com.monsanto.arch.cloudformation.model.{Template, VPCWriter}
import nl.vindh.extdup.dbinfo.{Table, Tables, TypedTable}
import shapeless._

object MainVPCWriter extends VPCWriter {
  def main(args: Array[String]): Unit =
    jsonToFile(
      fileName = "templates.json",
      subDir = "cloudformation/templates/",
      jsObj = tables()
    )

  def tables() =
    Template(
      Resources = Tables.tables.map(toTable).toList[Table].map(tableTemplate)
    )

  def tableTemplate(table: Table): `AWS::DynamoDB::Table` =
    `AWS::DynamoDB::Table`(
      name = s"${table.name}-table",
      AttributeDefinitions =
        table.indices.map(AttributeDefinition(_, StringAttributeType)),
      BillingMode = Some(PAY_PER_REQUEST),
      KeySchema = table.indices.map(KeySchema(_, HashKeyType)),
      GlobalSecondaryIndexes =
        table.indices.map {
          idx => GlobalSecondaryIndex(
            IndexName = s"by$idx",
            KeySchema = Seq(KeySchema(idx, HashKeyType)),
            Projection = AllProjection
          )
        },
      LocalSecondaryIndexes = Seq(),
      TableName = Some(s"resources-${table.name}-table")
    )

  object toTable extends Poly1 {
    implicit def tableCase[T, I <: HList]: Case[TypedTable[T, I]] { type Result = Table } =
      at(_.toTable)
  }
}
