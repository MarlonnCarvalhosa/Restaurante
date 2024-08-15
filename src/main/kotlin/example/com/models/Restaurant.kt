package example.com.models

import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val id: Long? = 0,
    val name: String,
    val description: String,
    val logoUrl: String,
    val themeColor: String
)
