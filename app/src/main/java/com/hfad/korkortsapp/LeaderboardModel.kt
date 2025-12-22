package com.hfad.korkortsapp

import java.sql.Timestamp

data class LeaderboardModel(
    val username: String = "",
    val score: Int = 0,
    val timestamp: Long = 0L
)
