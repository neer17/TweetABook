package com.example.tweetabook.screens.main.adapter

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
    var data: MutableList<AdapterDataClass> = ArrayList()

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
        Log.d(TAG, "onBindViewHolder: payload: $payloads")

        if (payloads.lastOrNull() != null) {
            with(holder.itemView) {
                Log.d(
                    TAG,
                    "bindViewHolder: position: $position \n payload last: ${payloads.last()}"
                )

                (payloads.last() as Bundle).getDouble("progress").also { progress ->
                    if (progress >= 0.8) {
                        holder.itemView.foreground =
                            ColorDrawable(resources.getColor(android.R.color.transparent))
                        progress_bar.visibility = View.GONE
                    } else {
                        holder.itemView.foreground =
                            ColorDrawable(resources.getColor(R.color.image_conversion_pending))
                        progress_bar.progress = (progress * 100).roundToInt()
                    }
                }

                (payloads.last() as Bundle).getBoolean("translationCompleted").also {
                    if (it) {
                        holder.itemView.foreground =
                            ColorDrawable(resources.getColor(android.R.color.transparent))
                        progress_bar.visibility = View.GONE
                    }
                }
            }
        }
    }


    fun addData(response: AdapterDataClass) {
        data.add(response)
        notifyDataSetChanged()
    }

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
        val position = data.find {
            it.id == id
        }.let {
            data.indexOf(it)
        }

        Log.d(
            TAG,
            "updateProgress: id: $id \t progress: $progress \t status: $status \t tweet: $tweet \n"
        )
        if (status == Constants.RECOGNIZING_TEXT) {
            with(data[position]) {
                this.progress = progress
            }
            //  to update the UI
            notifyItemChanged(position, Bundle().apply {
                putDouble("progress", progress)
            })
        } else if (status == Constants.IMAGE_CONVERSION_COMPLETED) {
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

    inner class MyViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: AdapterDataClass,
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