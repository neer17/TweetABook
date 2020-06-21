package com.example.tweetabook.screens.common

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.example.tweetabook.R
import com.example.tweetabook.common.viewmodel.MyViewModelFactory
import com.example.tweetabook.screens.common.screennavigator.ScreenNavigator
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_single.*
import kotlinx.android.synthetic.main.toolbar.view.*

class SingleActivity : BaseActivity(), FragmentFrameWrapper {
    private val TAG = "AppDebug: " + SingleActivity::class.java.simpleName

    private lateinit var toolbar: Toolbar
    lateinit var screenNavigator: ScreenNavigator
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        instantiateViewModel()
        attachMenuOnToolbar()

        screenNavigator = controllerCompositionRoot.getScreenNavigator(savedInstanceState)
        screenNavigator.navigateToMainFrag()

        subscribe()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.getFilesCount()
    }

    private fun subscribe() {
       mainViewModel.totalFiles.observe(this, androidx.lifecycle.Observer {totalFiles ->
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
                controllerCompositionRoot
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
}