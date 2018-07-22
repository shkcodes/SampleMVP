package com.shkmishra.samplemvp.di.restaurantlist

import com.shkmishra.samplemvp.ui.listscreen.RestaurantListActivity
import dagger.Subcomponent

@RestaurantListScope
@Subcomponent(modules = [RestaurantListModule::class])
interface RestaurantListComponent {

    fun inject(restaurantListActivity: RestaurantListActivity)
}