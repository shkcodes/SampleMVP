package com.shkmishra.samplemvp.data

data class ApiResponse(val response: ResponseBody)

data class ResponseBody(val groups: List<Group>)

data class Group(val items: List<Item>)

data class Item(val venue: Restaurant)

data class Restaurant(val id: String,
                      val name: String,
                      val location: Location
)

data class Location(val address: String?,
                    val crossStreet: String?,
                    val distance: Long,
                    val lat: Double,
                    val lng: Double)