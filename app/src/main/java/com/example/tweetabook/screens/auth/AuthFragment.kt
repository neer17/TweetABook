package com.example.tweetabook.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tweetabook.screens.common.BaseFragment
import com.example.tweetabook.screens.common.SingleActivity


class AuthFragment : BaseFragment(), AuthViewMvc.Listener {
    private lateinit var authViewMvc: AuthViewMvcImpl

    companion object {
        @JvmStatic
        fun newInstance() =
            AuthFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        authViewMvc =
            activity!!.let {
                (it as SingleActivity).controllerCompositionRoot.getViewMvcFactory()
                    .getAuthViewMvcImpl(container!!)
            }
        return authViewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        authViewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        authViewMvc.unregisterListener(this)
    }
}