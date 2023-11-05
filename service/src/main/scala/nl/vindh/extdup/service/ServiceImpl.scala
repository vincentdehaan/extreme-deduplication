package nl.vindh.extdup.service

import nl.vindh.extdup.api.Service
import nl.vindh.extdup.domain.Foo

import scala.concurrent.{ExecutionContext, Future}

class ServiceImpl(implicit ec: ExecutionContext) extends Service {
  def createFoo(foo: Foo): Future[Either[Unit, Unit]] =
    Future {
      println(s"Creating foo $foo")
      Right(())
    }
}
