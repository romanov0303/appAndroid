package com.criticalgnome.recyclerviewwithkotlin

import android.media.Rating
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView

class MainAdapter(var items: List<MainItem>) : RecyclerView.Adapter<MainAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.firstName.text = items[position].firstName
        holder.lastName.numStars = 5
        holder.lastName.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            println("test")
        }
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val firstName = itemView.findViewById<TextView>(R.id.firstName)
        public val lastName =  itemView.findViewById<RatingBar>(R.id.lastName)
        public var avgRating =  itemView.findViewById<TextView>(R.id.avgRating)
    }

}