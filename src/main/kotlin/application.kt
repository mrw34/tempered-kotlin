import com.mongodb.client.MongoClients
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.basic
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.bson.Document
import java.time.Instant

val TOKEN = System.getenv("TOKEN")
val MONGO_URL = System.getenv("MONGO_URL")

fun main() {
    embeddedServer(Netty, port = 8000) {
        install(CORS) {
            host("markwoodbridge.com", listOf("https"))
        }
        install(ContentNegotiation) {
            jackson()
        }
        install(Authentication) {
            basic {
                validate { credentials ->
                    if (credentials.name == TOKEN) UserIdPrincipal(credentials.name) else null
                }
            }
        }
        routing {
            authenticate {
                post("/") {
                    val reading: Map<String, Number> = call.receive<Map<String, Number>>()
                    val document = Document()
                        .append("timestamp", Instant.ofEpochSecond(reading["time"]!!.toLong()))
                        .append("temperature", reading["temperature"]!!)
                        .append("humidity", reading["relative humidity"]!!)
                    val client = MongoClients.create(MONGO_URL)
                    client
                        .getDatabase("tempered")
                        .getCollection("data")
                        .insertOne(document)
                    client.close()
                    call.respond(HttpStatusCode.OK)
                }
            }
            get("/") {
                val client = MongoClients.create(MONGO_URL)
                val readings = client
                    .getDatabase("tempered")
                    .getCollection("data")
                    .find()
                    .projection(fields(excludeId()))
                    .into(ArrayList())
                client.close()
                call.respond(readings)
            }
        }
    }.start(wait = true)
}
