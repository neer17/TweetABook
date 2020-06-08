package com.example.tweetabook.screens.common.views;

public interface ObservableViewMvc<ListenerType> :
    ViewMvc {

    fun registerListener(listener: ListenerType)

    fun unregisterListener(listener: ListenerType)
}
