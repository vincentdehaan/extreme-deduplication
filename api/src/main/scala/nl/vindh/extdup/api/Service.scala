package nl.vindh.extdup.api

import nl.vindh.extdup.domain.Foo

import scala.concurrent.Future

trait Service {
  def createFoo(foo: Foo): Future[Either[Unit, Unit]]
}
