package com.example.tweetabook.screens.common

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.tweetabook.R


fun Context.showProgressBar(value: Boolean) {
    if (value)
        (this as SingleActivity).findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
    else
        (this as SingleActivity).findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE

}

fun Context.showToast(value: String) {
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}