package com.shkmishra.samplemvp.di

import com.shkmishra.samplemvp.di.restaurantlist.RestaurantListComponent
import com.shkmishra.samplemvp.di.restaurantlist.RestaurantListModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun plus(module: RestaurantListModule): RestaurantListComponent
}