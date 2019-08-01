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

class ImageAdapter(var items: MutableList<Uri>,var activity: MainActivity) : RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ImageHolder {
        return ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ImageHolder, p1: Int) {
        var el: Uri = items[p1]
        p0.image.setImageURI(el)
        p0.remove.setOnClickListener {
            items.removeAt(p1)
            this.notifyDataSetChanged()
            println(items) //TODO сделать проверку на то где находиться фото (галерея или в памяти приложения)
        }
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val image = itemView.findViewById<ImageView>(R.id.imageObject)
        public val remove = itemView.findViewById<ImageView>(R.id.removeElement)
    }
}