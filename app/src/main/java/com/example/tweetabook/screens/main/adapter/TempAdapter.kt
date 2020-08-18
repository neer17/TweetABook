package com.example.tweetabook.screens.main.adapter


import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.tweetabook.R
import kotlinx.android.synthetic.main.tweet_card.view.*
import java.util.*
import kotlin.math.roundToInt

class TempAdapter() : RecyclerView.Adapter<TempAdapter.MyViewHolderClass>() {

    var data: MutableList<AdapterDataClass> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        return MyViewHolderClass(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tweet_card, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        holder.bind(data[position])
    }

    //  updating progress bar for items displayed on screen
    override fun onBindViewHolder(
        holder: MyViewHolderClass,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)

        if (payloads.firstOrNull() != null) {
            val lastPayload = payloads.last() as Map<String, Any>
            val progressPresent = lastPayload.containsKey("progress")
            val tweetPresent = lastPayload.containsKey("tweet")
            if (progressPresent) {
                val progress = lastPayload["progress"] as Double
                holder.setProgress(progress)
            } else if (tweetPresent) {
                with(holder.itemView) {
                    progress_bar.visibility = View.GONE
                    foreground = ColorDrawable(resources.getColor(android.R.color.transparent))
                }
            }
        }
    }

    fun addData(response: AdapterDataClass) {
        data.add(response)
        notifyDataSetChanged()
    }

    fun updateProgress(
        id: String,
        progress: Double,
        progressDone: Boolean
    ) {
        if (data.size == 0)
            return

        val position = data.find {
            it.id == id
        }.let {
            data.indexOf(it)
        }

        if (!progressDone) {
            with(data[position]) {
                this.progress = progress
            }
            notifyItemChanged(position, mapOf("progress" to progress))
        } else {
            with(data[position]) {
                notifyItemChanged(position, mapOf("progressDone" to true))
            }
        }
    }

    inner class MyViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: AdapterDataClass
        ) = with(itemView) {
            val (_, imageUri) = item

            foreground = if (item.tweet == null)
                ColorDrawable(resources.getColor(R.color.image_conversion_pending))
            else
                ColorDrawable(resources.getColor(android.R.color.transparent))

            progress_bar.visibility = View.GONE
            tweet_image_iv.load(imageUri)
        }

        fun setProgress(progress: Double) {
            with(itemView) {
                foreground =
                    ColorDrawable(resources.getColor(R.color.image_conversion_pending))
                progress_bar.visibility = View.VISIBLE
                progress_bar.progress = (progress * 100).roundToInt()
            }
        }
    }
}