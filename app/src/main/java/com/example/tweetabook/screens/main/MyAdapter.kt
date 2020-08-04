package com.example.tweetabook.screens.main

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.tweetabook.R
import com.example.tweetabook.common.Constants
import com.example.tweetabook.screens.main.responses.TweetResponse
import kotlinx.android.synthetic.main.tweet_card.view.*
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class MyAdapter
@Inject
constructor(adapterOnClickListener: AdapterOnClickListener) :
    RecyclerView.Adapter<MyAdapter.MyViewHolderClass>() {
    private val TAG = "AppDebug: MyAdapter"

    interface AdapterOnClickListener {
        fun onClickTweetItem(position: Int)
    }

    var listener = adapterOnClickListener
    var data: MutableList<TweetResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        return MyViewHolderClass(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.tweet_card, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {
        holder.bind(data[position], position)
    }

    //  when "notifyItemChanged" is called
    override fun onBindViewHolder(
        holder: MyViewHolderClass,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.firstOrNull() != null) {
            with(holder.itemView) {
                (payloads.first() as Bundle).getDouble("progress").also { progress ->
                    Log.d(TAG, "bindViewHolder: position: $position \n progress: $progress")
                    if (progress == 1.0) {
                        holder.itemView.foreground =
                            ColorDrawable(resources.getColor(android.R.color.transparent))
                        progress_bar.visibility = View.GONE

                    } else {
                        holder.itemView.foreground =
                            ColorDrawable(resources.getColor(R.color.image_conversion_pending))
                        progress_bar.progress = (progress * 100).roundToInt()
                    }
                }
            }
        }
    }


    fun addData(response: TweetResponse) {
        data.add(response)
        notifyDataSetChanged()
    }

    fun updateProgress(
        id: String,
        progress: Double,
        status: String,
        tweet: String?
    ) {
        val position = data.find {
            it.id == id
        }.let {
            data.indexOf(it)
        }
        if (status == Constants.RECOGNIZING_TEXT) {
            with(data[position]) {
                this.progress = progress
            }
            //  to update the UI
            notifyItemChanged(position, Bundle().apply {
                putDouble("progress", progress)
            })
        } else if (status == Constants.IMAGE_CONVERSION_COMPLETED) {
            tweet?.let {
                data[position].tweet = it
            }
        }
    }

    inner class MyViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: TweetResponse,
            position: Int
        ) = with(itemView) {
            val (_, imageUri, progress) = item
            with(progress_bar) {
                max = 100
                this.progress = (progress * 100).roundToInt()
            }
            tweet_image_iv.load(imageUri)

            registerClickListener(position)
        }

        private fun registerClickListener(position: Int) {
            itemView.setOnClickListener {
                listener.onClickTweetItem(position)
            }
        }
    }
}