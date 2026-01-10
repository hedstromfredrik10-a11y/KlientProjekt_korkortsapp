package com.hfad.korkortsapp

/**
 * Datamodell som representerar ett resultat i leaderboarden.
 *
 * Klassen används för att lagra information om en användares
 * prestation i ett quiz.
 *
 * @property username Användarens namn.
 * @property score Användarens poäng i quizet.
 * @property timestamp Tidpunkt då resultatet sparades,
 * angivet som millisekunder sedan 1 januari 1970 (Unix time).
 */
data class LeaderboardModel(
    val username: String = "",
    val score: Int = 0,
    val timestamp: Long = 0L
)
