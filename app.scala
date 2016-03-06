import io.peregrine._
import scala.collection.mutable

case class Message(message: String)
case class User(name: String, age: Int)

object MyApp extends PeregrineApp {
  val db = mutable.Map[Int, User]()
  var index = 0

  put("/api/users/:id") { req =>
    val id = req.param("id").getOrElse("-1").toInt
    db.get(id) match{
      case None => json(Message("Not found"))
      case Some(u) =>
        buildUser(req) match {
          case None => json(Message("usuario no construido"))
          case Some(u) => db.put(id, u)
            json(Message("Listo"))
        }
    }
  }

  def buildUser(req: Request): Option[User] = {
      for{
        name <- req.param("name")
        age <- req.param("age")
      } yield User(name, age.toInt)
  }

  post("/api/users"){ req =>
    buildUser(req) match {
      case None => json(Message("No pude crear el usuario"))
      case Some(u) => db.put(index,u)
      index = index+1
      json(Message("Creado")).status(201)
    }
  }

  delete("/api/users/:id") { req =>
    val id = req.param("id").getOrElse("-1").toInt
    db.remove(id)
    json(Message("se fue"))
  }

  get("/api/users/:id") { req =>
    val id = req.param("id").getOrElse("-1").toInt
    db.get(id) match {
      case None => json(Message("error, not found"))
      case Some(u) => json(u)
    }
  }

  get("/api/users"){ req =>
    json(db.values.toList)
  }
}
