package com.example.tweetabook.screens.common.views

import java.util.*
import kotlin.collections.HashSet

abstract class BaseObservableViewMvc<ListenerType> : BaseViewMvc(),
    ObservableViewMvc<ListenerType> {

    private val mListeners: MutableSet<ListenerType> = HashSet()

    override fun registerListener(listener: ListenerType) {
        mListeners.add(listener)
    }

    override fun unregisterListener(listener: ListenerType) {
        mListeners.remove(listener)
    }

    protected val listeners: Set<ListenerType>
        get() = Collections.unmodifiableSet(mListeners)
}
