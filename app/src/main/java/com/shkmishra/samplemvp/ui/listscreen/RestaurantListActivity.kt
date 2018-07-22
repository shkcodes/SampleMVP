package com.shkmishra.samplemvp.ui.listscreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.transition.Slide
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.shkmishra.samplemvp.R
import com.shkmishra.samplemvp.SampleMVPApp
import com.shkmishra.samplemvp.data.Restaurant
import com.shkmishra.samplemvp.di.restaurantlist.RestaurantListModule
import com.shkmishra.samplemvp.utils.EndlessRecyclerOnScrollListener
import com.shkmishra.samplemvp.utils.RecyclerClickListener
import com.shkmishra.samplemvp.utils.doOnAnimationEnd
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import javax.inject.Inject


class RestaurantListActivity : AppCompatActivity(), RestaurantListContract.View, RecyclerClickListener {

    @Inject
    lateinit var presenter: RestaurantListPresenter
    private lateinit var dividers: DividerItemDecoration
    private lateinit var restaurantListAdapter: RestaurantListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        performInjection()

        initViews()
        presenter.loadRestaurants()
    }

    private fun performInjection() {
        (application as SampleMVPApp)
                .appComponent
                .plus(RestaurantListModule(this))
                .inject(this)
    }

    private fun initViews() {
        restaurantListAdapter = RestaurantListAdapter(this@RestaurantListActivity)
        val linearLayoutManager = LinearLayoutManager(this)
        restaurantsList.apply {
            adapter = restaurantListAdapter
            layoutManager = linearLayoutManager
        }
        restaurantsList.addOnScrollListener(object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.loadRestaurants()
            }
        })
        dividers = DividerItemDecoration(this@RestaurantListActivity, LinearLayoutManager.VERTICAL)

        swipeRefresh.setOnRefreshListener {
            presenter.loadRestaurants(true)
            restaurantListAdapter.clear()
        }

    }


    override fun showHideLoading(loading: Boolean) {
        swipeRefresh.isRefreshing = loading
    }

    override fun showRestaurants(restaurants: List<Restaurant>) {
        swipeRefresh.visibility = View.VISIBLE
        messageText.visibility = View.GONE

        if (restaurantListAdapter.itemCount == 0) {
            val slide = Slide()
            slide.duration = 500
            restaurantsList.removeItemDecoration(dividers)
            slide.doOnAnimationEnd {
                restaurantsList.addItemDecoration(dividers)
            }
            TransitionManager.beginDelayedTransition(restaurantsList, slide)
        }

        restaurantListAdapter.addVenues(restaurants)

    }

    override fun showErrorMessage(message: String) {
        messageText.apply {
            text = message
            visibility = View.VISIBLE
        }
        swipeRefresh.visibility = View.GONE
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDislikeClick(venueId: String, position: Int) {
        presenter.dislikeRestaurant(venueId)
        val venue = restaurantListAdapter.getVenue(position)
        restaurantListAdapter.removeVenue(position)

        Snackbar.make(contentLayout, "We will not suggest this restaurant again", Snackbar.LENGTH_SHORT).setAction("Undo") {
            presenter.removeFromDislikes(venueId)
            restaurantListAdapter.addVenue(position, venue)
        }.show()
    }

    override fun onItemClick(latLong: String, venueName: String) {
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$latLong?q=$latLong($venueName)"))
        mapIntent.`package` = "com.google.android.apps.maps"
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else showToast("Please install Google Maps to view the restaurants")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
