package nl.vindh.extdup.service

import nl.vindh.extdup.api.Service
import nl.vindh.extdup.domain.Foo

import scala.concurrent.{ExecutionContext, Future}

class ServiceImpl(fooRepository: FooRepository)(implicit ec: ExecutionContext) extends Service {
  def createFoo(foo: Foo): Future[Either[Unit, Unit]] =
    fooRepository.put(foo).map(Right(_))

  override def getFooByName(name: String): Future[Either[Unit, Foo]] =
    fooRepository.getByName(name).map(Right(_))
}
