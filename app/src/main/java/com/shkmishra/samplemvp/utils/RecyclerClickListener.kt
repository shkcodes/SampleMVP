package com.shkmishra.samplemvp.utils

interface RecyclerClickListener {
    fun onDislikeClick(venueId: String, position: Int)
    fun onItemClick(latLong: String, venueName: String)
}