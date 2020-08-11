package com.example.tweetabook.screens.main.adapter

import android.content.Context
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
import com.example.tweetabook.screens.common.showProgressBar
import kotlinx.android.synthetic.main.tweet_card.view.*
import java.util.*
import kotlin.math.roundToInt

class MyAdapter
constructor(val context: Context, adapterOnClickListener: AdapterOnClickListener) :
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
        Log.d(TAG, "onBindViewHolder: version 1")
        holder.bind(data[position], position)
    }

    fun addData(response: AdapterDataClass) {
        data.add(response)
        notifyDataSetChanged()
    }

    /*
    * When localImageUri -> firebaseImageUri and image is translated to tweet
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

        /* Log.d(
             TAG,
             "updateProgress: id: $id \t progress: $progress \t status: $status \t tweet: $tweet \n"
         )*/
        if (status == Constants.RECOGNIZING_TEXT) {
            with(data[position]) {
                this.progress = progress
            }
            //  to update the UI
            notifyItemChanged(position, Bundle().apply {
                putDouble("progress", progress)
            })
        } else if (status == Constants.IMAGE_CONVERSION_COMPLETED && tweet != null) {
//            Log.d(TAG, "updateProgress: image conversion completed")
            with(data[position]) {
                this.progress = progress
                this.tweet = tweet
            }
            //  to update the UI
            notifyItemChanged(position, Bundle().apply {
                putBoolean("translationCompleted", true)
            })
        }
    }

    fun clearData() {
        context.showProgressBar(true)
        data.clear()
        notifyDataSetChanged()
        context.showProgressBar(false)
    }

    inner class MyViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: AdapterDataClass,
            position: Int
        ) = with(itemView) {
            val (_, imageUri, progress, _, tweet) = item

            if (tweet == null) {
                foreground =
                    ColorDrawable(resources.getColor(R.color.image_conversion_pending))
                progress_bar.progress = (progress * 100).roundToInt()
            } else {
                foreground =
                    ColorDrawable(resources.getColor(android.R.color.transparent))
                progress_bar.visibility = View.GONE
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