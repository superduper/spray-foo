import akka.actor.{Props, Actor}
import akka.util.Timeout
import shapeless._
import akka.pattern.ask
import spray.routing._
import spray.json._
import spray.routing.directives.BasicDirectives._
import spray.routing.directives.RouteDirectives._
import spray.httpx.SprayJsonSupport
import spray.httpx.encoding.NoEncoding

//
//  Actor
//

case class GetByID(id: Int)
case class Del(id: Int)
case class Foo(id: Int, bar: String)
case object GetAll

class FooActor extends Actor {

  def receive = {
    case GetByID(1) => sender ! provideFoo(1)
    case GetByID(_) => sender ! None
    case GetAll     => sender ! allFoos
    case Del(id)    => deleteFoo(id)
  }

  private def deleteFoo(id: Int) {
    println("delete foo")
  }

  private def allFoos: List[Int] = {
    println("noway-fooway")
    1 :: 2 :: 3 :: Nil
  }

  private def provideFoo(id: Int) = {
    println("lookup foo")
    Some(Foo(id, "bar"))
  }
}

//
//  Http Service  
//

class FooHttpService extends Actor with HttpService with SprayJsonSupport with DefaultJsonProtocol {

  def actorRefFactory = context

  val fooActor = context.actorFor("/user/fooActor")
  
  def receive = runRoute(mainRoute)

  implicit val fooFormat = jsonFormat2(Foo)

  final implicit val timeout = Timeout(1000)

  def lookupFoo(id: Int): Directive[Foo ::HNil] =
    provide(
      (fooActor ? GetByID(id)).mapTo[Option[Foo]]
    ).unwrapFuture.flatMap {
      case Some(foo: Foo) => provide(foo)
      case None => reject(ValidationRejection("No such Foo found"))
    }

  def deleteFoo(id: Int): Directive0 =
    mapRequestContext { ctx =>
      (fooActor ! Del(id))
      ctx
    }

  val allFoos: Directive[List[Int] :: HNil] =
    extract {
      ctx =>
        (fooActor ? GetAll).mapTo[List[Int]]
    }.unwrapFuture.flatMap {
      case foos: List[Int] => provide(foos)
      case _ => throw new Exception("Invalid response on list foos")
    }

  val mainRoute =
    pathPrefix("foo") {
      path(IntNumber) { fooID =>
        lookupFoo(fooID) { foo =>
          get {
            complete(foo)
          } ~
          delete {
            deleteFoo(fooID) {
              complete("deleted")
            }
          }
        }
      } ~
      get {
        allFoos { foos =>
          complete("all-foos: "+foos)
        }
      }
    }
}

//
//    Boot Class
//

import spray.can.server.SprayCanHttpServerApp

object Boot extends App 
  with SprayCanHttpServerApp {

  def executionContext = system.dispatcher

  val httpService = system.actorOf(Props(new FooHttpService))
  val fooActor = system.actorOf(Props(new FooActor), "fooActor")
  // create a new HttpServer using our handler tell it where to bind to
  newHttpServer(httpService) ! Bind(interface = "0.0.0.0", port = 8000)

}