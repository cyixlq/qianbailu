package com.test.qianbailu.module.live.room

import com.test.qianbailu.model.bean.LiveRooms

data class LiveRoomsViewState(
    val isLoading: Boolean = false,
    val throwable: Throwable? = null,
    val liveRooms: LiveRooms? = null
)