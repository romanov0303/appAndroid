package com.criticalgnome.recyclerviewwithkotlin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView

class ImageAdapter(var items: List<ImageItems>,var activity: MainActivity) : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ImageHolder {
        return ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ImageHolder, p1: Int) {

    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val firstName = itemView.findViewById<TextView>(R.id.firstName)
        public val ratingVal =  itemView.findViewById<RatingBar>(R.id.ratingVal)
        public val avgRating =  itemView.findViewById<TextView>(R.id.avgRating)
    }
}