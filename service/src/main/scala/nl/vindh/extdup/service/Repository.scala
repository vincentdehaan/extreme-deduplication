package nl.vindh.extdup.service

import nl.vindh.extdup.dbinfo.{Tables, TypedTable}
import nl.vindh.extdup.domain.Foo
import org.scanamo.{DynamoFormat, Scanamo, Table}
import org.scanamo.syntax._
import shapeless.ops.hlist.Selector
import shapeless.{=:!=, HList, HNil, Poly1, Witness}
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

import scala.concurrent.{ExecutionContext, Future}

abstract class Repository[T: DynamoFormat] {
  val client: DynamoDbClient

  implicit val ec: ExecutionContext
  protected val scanamoClient = Scanamo(client)
  protected val table: Table[T] = Table[T](???)

  object tableSelector extends Poly1 {
    implicit def thisCase[H <: HList] = at[TypedTable[T, H]](x => x :: HNil)
    implicit def otherCase[U, H <: HList](implicit ev: U =:!= T) = at[TypedTable[U, H]](_ => HNil)
  }

  def put(t: T): Future[Unit] =
    Future {
      scanamoClient.exec(table.put(t))
    }

  protected def getByIndex[H <: HList, S <: HList](idx: Witness, value: String)(implicit t: TypedTable[T, H], s: Selector[H, idx.T]): Future[T] =
    Future {
      scanamoClient.exec(table.get(idx.value.asInstanceOf[Symbol].name === value))
    }.map {
      case Some(Right(t)) => t
      case _ => throw new Exception
    }
}
