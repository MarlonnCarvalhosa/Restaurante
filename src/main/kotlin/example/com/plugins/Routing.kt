package example.com.plugins

import example.com.models.Restaurant
import example.com.repository.RestaurantRepository
import example.com.utils.generateId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val repository = RestaurantRepository()
    routing {
        route("/restaurants") {
            get {
                call.respond(repository.restaurants)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                    return@get
                }

                val restaurant = repository.findById(id)
                if (restaurant != null) {
                    call.respond(HttpStatusCode.OK, restaurant)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Restaurant not found")
                }
            }

            post {
                val restaurant = try {
                    call.receive<Restaurant>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid restaurant data")
                    return@post
                }

                val newRestaurant = restaurant.copy(id = generateId())

                repository.save(newRestaurant)
                call.respond(HttpStatusCode.Created, newRestaurant)
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                    return@put
                }

                val restaurant = call.receive<Restaurant>()
                val updatedRestaurant = Restaurant(
                    id = id,
                    name = restaurant.name,
                    description = restaurant.description,
                    logoUrl = restaurant.logoUrl,
                    themeColor = restaurant.themeColor
                )

                val existingRestaurant = repository.findById(id)
                if (existingRestaurant == null) {
                    call.respond(HttpStatusCode.NotFound, "Restaurant not found")
                    return@put
                }

                val updated = repository.update(id, updatedRestaurant)
                if (updated) {
                    call.respond(HttpStatusCode.OK, updatedRestaurant)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to update restaurant")
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                    return@delete
                }

                val deleted = repository.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK, "Restaurant deleted")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Restaurant not found")
                }
            }
        }
    }
}