import io.ktor.application.call
import io.ktor.http.ContentType
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
                call.respondText(statusAll().toString(), ContentType.Text.Plain) //TODO JSON?
            }

            get("/{pin}") {
                call.parameters["pin"]?.let { pin ->
                    call.respondText(status(pin), ContentType.Text.Plain)
                }
            }

            put("/toggle/{pin}") {
                call.parameters["pin"]?.let { pin: String ->
                    toggle(pin)
                }
            }

            put("/toggle/color/{color}") {
                call.parameters["color"]?.let { color ->
                    toggleColor(color)
                }
            }

            put("/light") {
                all()
            }

            put("/dark") {
                none()
            }

            put("/dance/{type}") {
                call.parameters["type"]?.let { type -> dance(type) }
            }
        }
    }.start(wait = true)
}