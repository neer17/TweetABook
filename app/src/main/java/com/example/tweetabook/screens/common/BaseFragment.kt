package com.example.tweetabook.screens.common

import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment: Fragment() {
     val TAG = "AppDebug: " + "BaseFragment"
}