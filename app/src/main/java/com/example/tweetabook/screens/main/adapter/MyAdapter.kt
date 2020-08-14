package com.example.tweetabook.screens.main.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.tweetabook.R
import com.example.tweetabook.common.Constants
import com.example.tweetabook.screens.common.showProgressBar
import kotlinx.android.synthetic.main.tweet_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class MyAdapter
constructor(
    val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope,
    adapterOnClickListener: AdapterOnClickListener
) :
    RecyclerView.Adapter<MyAdapter.MyViewHolderClass>() {
    private val TAG = "AppDebug: MyAdapter"

    interface AdapterOnClickListener {
        fun onClickTweetItem(position: Int)
    }

    var listener = adapterOnClickListener
    var data: MutableList<AdapterDataClass> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        return MyViewHolderClass(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tweet_card, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            holder.bind(data[position], position)
        }
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
                lifecycleScope.launch(Dispatchers.Main) {
                    holder.setProgress(progress)
                }
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

    /*
    * updates data when local db changes, may be redundant
    */
    fun updateData(adapterDataClass: AdapterDataClass) {
        val index = data.indexOf(adapterDataClass)
        data.removeAt(index)
        data.add(index, adapterDataClass)

        notifyItemChanged(index)
    }

    fun updateProgress(
        id: String,
        progress: Double,
        status: String,
        tweet: String?
    ) {
        if (data.size == 0)
            return

        val position = data.find {
            it.id == id
        }.let {
            data.indexOf(it)
        }

        if (status == Constants.RECOGNIZING_TEXT) {
            with(data[position]) {
                this.progress = progress
            }
            notifyItemChanged(position, mapOf("progress" to progress))
        } else if (status == Constants.IMAGE_CONVERSION_COMPLETED && tweet != null) {
            with(data[position]) {
                this.progress = progress
                this.tweet = tweet
                notifyItemChanged(position, mapOf("tweet" to tweet))
            }
        }
    }

    fun clearData() {
        context.showProgressBar(true)
        data.clear()
        notifyDataSetChanged()
        context.showProgressBar(false)
    }

    inner class MyViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: update this to show diff image if the tweet is tweeted and for the view recycled
        fun bind(
            item: AdapterDataClass,
            position: Int
        ) = with(itemView) {
            val (_, imageUri) = item

            foreground = if (item.tweet == null)
                ColorDrawable(resources.getColor(R.color.image_conversion_pending))
            else
                ColorDrawable(resources.getColor(android.R.color.transparent))

            progress_bar.visibility = View.GONE

            tweet_image_iv.load(imageUri)

            registerClickListener(position)
        }

        fun setProgress(progress: Double) {
            with(itemView) {
                foreground =
                    ColorDrawable(resources.getColor(R.color.image_conversion_pending))
                progress_bar.visibility = View.VISIBLE
                progress_bar.progress = (progress * 100).roundToInt()
            }
        }

        private fun registerClickListener(position: Int) {
            itemView.setOnClickListener {
                listener.onClickTweetItem(position)
            }
        }
    }
}