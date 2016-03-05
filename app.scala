import io.peregrine._

object MyApp extends PeregrineApp {
  get("/perro") { req =>
    json(Map("nombre" -> "Jonathan", "edad" -> "23"))
  }
}
