import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

/**
 *@author mhashim6 on 09/01/19
 */

fun main(args: Array<String>) {
    embeddedServer(Netty, 8585) {
        routing {
            get("/") {
                call.respondText(stateAll().toString(), ContentType.Text.Plain) //TODO JSON?
            }

            get("/{pin}") {
                call.parameters["pin"]?.let { pin ->
                    call.respondText(state(pin), ContentType.Text.Plain)
                }
            }

            put("/toggle/{pin}") {
                call.parameters["pin"]?.let { pin: String ->
                    toggle(pin)
                }
                call.respond(HttpStatusCode.OK)
            }

            put("/toggle/color/{color}") {
                call.parameters["color"]?.let { color ->
                    toggleColor(color)
                }
                call.respond(HttpStatusCode.OK)
            }

            put("/light") {
                all()
                call.respond(HttpStatusCode.OK)
            }

            put("/dark") {
                none()
                call.respond(HttpStatusCode.OK)
            }

            put("/dance/{type}") {
                call.parameters["type"]?.let { type -> dance(type) }
                call.respond(HttpStatusCode.OK)
            }

            put("/dance") {
                dance()
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}