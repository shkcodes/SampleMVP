package com.shkmishra.samplemvp.ui.listscreen

import com.shkmishra.samplemvp.data.Restaurant


interface RestaurantListContract {
    interface View {
        fun showHideLoading(loading: Boolean)
        fun showRestaurants(restaurants: List<Restaurant>)
        fun showErrorMessage(message: String)
        fun showToast(message: String)
    }

    interface Presenter {
        fun loadRestaurants(refresh: Boolean = false)
        fun dislikeRestaurant(id: String)
        fun removeFromDislikes(id: String)
        fun destroy()
    }
}