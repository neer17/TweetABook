package com.example.tweetabook.screens.common

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.tweetabook.R
import com.example.tweetabook.common.viewmodel.MyViewModelFactory
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_single.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class SingleActivity : BaseActivity(), FragmentFrameWrapper {
    private val TAG = "AppDebug: " + SingleActivity::class.java.simpleName

    private lateinit var toolbar: Toolbar
    lateinit var screenNavigator: ScreenNavigator
    lateinit var mainViewModel: MainViewModel

    val scope = CoroutineScope(Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        attachMenuOnToolbar()

        screenNavigator = singleActivityController.getScreenNavigator(savedInstanceState)
        screenNavigator.navigateToMainFrag()

        socketIOConnection()
        instantiateViewModel()
        subscribe()
    }



    override fun onStart() {
        super.onStart()
        mainViewModel.getFilesCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        socketIODisconnection()
    }

    private fun subscribe() {
       mainViewModel.totalFiles.observe(this, Observer {totalFiles ->
           totalFiles?.let {
               toolbar.toolbar_count_tv.text = totalFiles.toString()
           }
       })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        screenNavigator.saveInstanceState(outState)
    }

    override fun getFrameLayout(): FrameLayout {
        return frame_content
    }

    private fun instantiateViewModel() {
        val mainViewModel: MainViewModel by viewModels {
            MyViewModelFactory(
                singleActivityController
            )
        }
        this.mainViewModel = mainViewModel
    }

    private fun attachMenuOnToolbar() {
        toolbar = findViewById<View>(R.id.include) as Toolbar
        toolbar.let {
            it.inflateMenu(R.menu.main_fragment_menu)
            it.setOnMenuItemClickListener {
                menuOnItemClick(it.itemId)
            }
        }
    }

    private fun menuOnItemClick(id: Int): Boolean = when (id) {
        R.id.empty_storage -> {
            mainViewModel.deleteAll()
            true
        }
        R.id.purge_data -> {
            //  TODO: delete all data from the main fragment
            true
        }
        else -> false
    }


    //  Socket.IO
    private fun socketIOConnection() {
        singleActivityController.mySocket.socketIOConnection()
    }

    private fun socketIODisconnection() {
        singleActivityController.mySocket.socketIODisconnection()
    }
}
