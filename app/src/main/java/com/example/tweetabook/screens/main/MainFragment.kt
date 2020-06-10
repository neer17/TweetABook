package com.example.tweetabook.screens.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tweetabook.screens.common.BaseFragment
import com.example.tweetabook.screens.common.SingleActivity

class MainFragment : BaseFragment(), MainViewMvc.Listener {
    private lateinit var mainViewMvc: MainViewMvc

    companion object {
        @JvmStatic
        fun newInstance(): MainFragment{
            Log.d("MainFragment", "newInstance: ")
            return MainFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")

        mainViewMvc =
            activity!!.let {
                (it as SingleActivity).controllerCompositionRoot.getViewMvcFactory()
                    .getMainViewMvcImpl(container!!)
            }

        return mainViewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        mainViewMvc.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        mainViewMvc.unregisterListener(this)
    }


}