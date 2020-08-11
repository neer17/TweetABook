package com.example.tweetabook.screens.common

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.tweetabook.R
import com.example.tweetabook.screens.main.MainFragment
import com.example.tweetabook.screens.main.viewmodel.MainViewModel
import com.example.tweetabook.socket.MySocket
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

@AndroidEntryPoint
class SingleActivity : AppCompatActivity() {
    private val TAG = "AppDebug: " + SingleActivity::class.java.simpleName

    @Inject
    lateinit var mySocket: MySocket

    private lateinit var toolbar: Toolbar

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment::class.java, null, "MainFragment")
                .commit()

        attachMenuOnToolbar()
        socketIOConnection()
        subscribe()
    }

    private fun subscribe() {
        viewModel.totalFiles.observe(this, Observer { totalFiles ->
            totalFiles?.let {
                toolbar.toolbar_count_tv.text = totalFiles.toString()
            }
        })
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
            viewModel.emptyStorage()
            true
        }
        R.id.purge_data -> {
            viewModel.deleteAllTweets()
            true
        }
        else -> false
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFilesCount()
    }

    override fun onDestroy() {
        super.onDestroy()
        socketIODisconnection()
    }

    //  Socket.IO
    private fun socketIOConnection() {
        mySocket.socketIOConnection()
    }

    private fun socketIODisconnection() {
        mySocket.socketIODisconnection()
    }
}
