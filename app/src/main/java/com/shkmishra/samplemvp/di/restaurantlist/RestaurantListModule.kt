package com.shkmishra.samplemvp.di.restaurantlist

import android.content.SharedPreferences
import com.shkmishra.samplemvp.data.api.ApiService
import com.shkmishra.samplemvp.ui.listscreen.RestaurantListContract
import com.shkmishra.samplemvp.ui.listscreen.RestaurantListPresenter
import dagger.Module
import dagger.Provides

@Module
class RestaurantListModule(private val view: RestaurantListContract.View) {


    @RestaurantListScope
    @Provides
    fun providePresenter(apiService: ApiService, sharedPreferences: SharedPreferences) = RestaurantListPresenter(view, apiService, sharedPreferences)
}