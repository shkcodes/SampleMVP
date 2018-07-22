package com.shkmishra.samplemvp.ui.listscreen

import android.content.SharedPreferences
import com.shkmishra.samplemvp.data.Restaurant
import com.shkmishra.samplemvp.data.api.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RestaurantListPresenter(private val restaurantView: RestaurantListContract.View, private val apiService: ApiService, private val sharedPreferences: SharedPreferences) : RestaurantListContract.Presenter {

    var offset = 0
    private val compositeDisposable = CompositeDisposable()

    override fun loadRestaurants(refresh: Boolean) {
        if (refresh) offset = 0

        compositeDisposable.add(apiService.getRestaurants(offset, 50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { restaurantView.showHideLoading(true) }
                .doOnTerminate { restaurantView.showHideLoading(false) }
                .flatMap { Observable.fromArray(it.response.groups[0].items) }
                .subscribe(
                        { items ->
                            val venues = mutableListOf<Restaurant>()
                            if (items.isEmpty())
                                restaurantView.showToast("No more restaurants found")
                            else {
                                items.map { item -> venues.add(item.venue) }
                                restaurantView.showRestaurants(venues.filter { venue -> !sharedPreferences.getBoolean(venue.id, false) })
                            }
                        },
                        { t ->
                            t.printStackTrace()
                            restaurantView.showErrorMessage("Unable to fetch restaurants (${t.message})")
                        }))
        offset += 50
    }

    override fun dislikeRestaurant(id: String) {
        sharedPreferences.edit().putBoolean(id, true).apply()
    }

    override fun removeFromDislikes(id: String) {
        sharedPreferences.edit().remove(id).apply()
    }

    override fun destroy() {
        compositeDisposable.dispose()
    }

}
