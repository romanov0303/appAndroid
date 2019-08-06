package com.criticalgnome.recyclerviewwithkotlin

import android.media.Rating
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import kotlin.math.floor
import kotlin.math.round

class MainAdapter(var items: List<MainItem>, var main: MainActivity) : RecyclerView.Adapter<MainAdapter.MainHolder>() {

    public var datasList = mutableMapOf<Int, Float>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MainHolder {
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))
    }

    data class testClass(var index: Int, var float: Float)


    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        if (items[position].value.isEmpty()) {
            items[position].value = "0"
        }
        if (holder.ratingVal.rating < 1 && items[position].value.isEmpty()) {
            datasList[position] = 0f
        } else {
            datasList[position] = round(items[position].value.toFloat())
        }

        holder.firstName.text = items[position].firstName
        if (items[position].value.toFloat() > 5) {
            holder.ratingVal.rating = 5.0f
        } else {
            holder.ratingVal.rating = round(items[position].value.toFloat())
        }
        main.listPropertiesParcelable.add(position, ParcelableString(holder.firstName.text.toString(), holder.ratingVal.rating.toString()))
        rebuildAvg()
        holder.ratingVal.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            var stars = round(rating)
            if (stars < 1) {
                ratingBar.rating = 1.0f
                stars = round(1.0f)
            } else {
                ratingBar.rating = stars
            }
            datasList[position] = stars
            main.listPropertiesParcelable[position] = ParcelableString(holder.firstName.text.toString(), stars.toString())
            rebuildAvg()
            main.checkForm()

        }
    }

    protected fun rebuildAvg() {
        val res = datasList.all {
            it.value != 0f
        }
        var elementBlockRect = main.findViewById<ImageView>(R.id.imageView)
        if (res) {
            val count = items.size
            var sums: Float = 0f
            datasList.forEach {
                sums = sums + it.value
                var avgRating: Float = sums / items.size
                val rounded = avgRating.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                main.avgRating.setBackgroundResource(R.drawable.style_rectangle)
                elementBlockRect.setBackgroundResource(R.drawable.style_rectangle)
                main.avgRating.setText("$rounded")
            }
        } else {
            main.avgRating.setText("0")
            main.avgRating.setBackgroundResource(R.drawable.style_rectangle_default)
            elementBlockRect.setBackgroundResource(R.drawable.style_rectangle_default)
        }
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val firstName = itemView.findViewById<TextView>(R.id.firstName)
        public val ratingVal =  itemView.findViewById<RatingBar>(R.id.ratingVal)
        public val avgRating =  itemView.findViewById<TextView>(R.id.avgRating)
    }


}