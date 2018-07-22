package com.shkmishra.samplemvp.data.api

import com.shkmishra.samplemvp.data.ApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/venues/explore/?ll=28.63,77.21&categoryId=4d4b7105d754a06374d81259")
    fun getRestaurants(@Query("offset") offset: Int = 0, @Query("limit") limit: Int = 50): Observable<ApiResponse>

}
