package com.example.tweetabook.screens.auth

import androidx.fragment.app.Fragment
import com.example.tweetabook.R


class AuthFragment: Fragment(R.layout.fragment_auth) {
    private val TAG = "AppDebug: AuthFragment "

    companion object {
        @JvmStatic
        fun newInstance() = AuthFragment()
    }
}