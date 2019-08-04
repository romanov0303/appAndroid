package com.criticalgnome.recyclerviewwithkotlin

import android.media.Image
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView

class ImageAdapter(var items: MutableList<String>,var activity: MainActivity) : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ImageHolder {
        return ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return activity.elementsImg.size
    }

    override fun onBindViewHolder(p0: ImageHolder, p1: Int) {
        var el: String = activity.elementsImg[p1]
        p0.image.setImageURI(Uri.parse(el))
        p0.remove.setOnClickListener {
            activity.elementsImg.removeAt(p1)
            this.notifyDataSetChanged()
        }
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val image = itemView.findViewById<ImageView>(R.id.imageObject)
        public val remove = itemView.findViewById<ImageView>(R.id.removeElement)
    }
}