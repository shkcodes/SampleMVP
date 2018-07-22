package com.shkmishra.samplemvp.ui.listscreen

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.shkmishra.samplemvp.R
import com.shkmishra.samplemvp.data.Restaurant
import com.shkmishra.samplemvp.utils.RecyclerClickListener
import com.shkmishra.samplemvp.utils.inflate
import kotlinx.android.synthetic.main.item_restaurant_list.view.*

class RestaurantListAdapter(private val clickListener: RecyclerClickListener) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    private val restaurants: MutableList<Restaurant> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantListAdapter.RestaurantViewHolder {
        return RestaurantViewHolder(parent.inflate(R.layout.item_restaurant_list))
    }

    override fun getItemCount() = restaurants.size

    override fun onBindViewHolder(holder: RestaurantListAdapter.RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    fun addVenues(newRestaurants: List<Restaurant>) {
        restaurants.addAll(newRestaurants)
        notifyItemRangeInserted(restaurants.size - newRestaurants.size, newRestaurants.size)
    }

    fun getVenue(position: Int): Restaurant {
        return restaurants[position]
    }

    fun removeVenue(position: Int) {
        restaurants.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addVenue(position: Int, restaurant: Restaurant) {
        restaurants.add(position, restaurant)
        notifyItemInserted(position)
    }

    fun clear() {
        restaurants.clear()
        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.dislikeButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener.onDislikeClick(restaurants[adapterPosition].id, adapterPosition)
            }
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val venue = restaurants[adapterPosition]
                    clickListener.onItemClick("${venue.location.lat},${venue.location.lng}", venue.name)
                }
            }
        }

        fun bind(item: Restaurant) {
            itemView.venueAddress.text = item.location.address
            item.location.crossStreet?.let { itemView.venueAddress.append(", ${item.location.crossStreet}") }
            itemView.venueName.text = item.name
            itemView.venueDistance.text = itemView.context.resources.getString(R.string.restaurant_distance, "%.2f".format(item.location.distance.toDouble() / 1000))
        }
    }
}