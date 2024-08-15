package example.com.repository

import example.com.models.Restaurant

class RestaurantRepository {

    val restaurants get() = _restaurant.toList()

    fun save(restaurant: Restaurant) {
        _restaurant.add(restaurant)
    }

    fun update(id: Long, updatedRestaurant: Restaurant): Boolean {
        val index = _restaurant.indexOfFirst { it.id == id }
        return if (index != -1) {
            _restaurant[index] = updatedRestaurant.copy(id = id)
            true
        } else {
            false
        }
    }

    fun findById(id: Long): Restaurant? {
        return _restaurant.find { it.id == id }
    }

    fun delete(id: Long): Boolean {
        val index = _restaurant.indexOfFirst { it.id == id }
        return if (index != -1) {
            _restaurant.removeAt(index)
            true
        } else {
            false
        }
    }

    companion object {
        private val _restaurant = mutableListOf<Restaurant>()
    }
}