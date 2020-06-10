package com.example.tweetabook.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tweetabook.common.di.ControllerCompositionRoot
import com.example.tweetabook.screens.common.BaseFragment
import com.example.tweetabook.screens.common.SingleActivity
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator


class AuthFragment : BaseFragment(), AuthViewMvc.Listener {
    companion object {
        @JvmStatic
        fun newInstance() =
            AuthFragment()
    }

    private lateinit var screenNavigator: ScreenNavigator
    private lateinit var controllerCompositionRoot: ControllerCompositionRoot
    private lateinit var authViewMvc: AuthViewMvcImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        controllerCompositionRoot = (activity as SingleActivity).controllerCompositionRoot

        authViewMvc =
            activity!!.let {
                screenNavigator = (it as SingleActivity).screenNavigator
                controllerCompositionRoot.getViewMvcFactory(screenNavigator)
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