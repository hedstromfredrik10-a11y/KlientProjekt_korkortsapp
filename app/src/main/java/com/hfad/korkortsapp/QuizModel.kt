package com.hfad.korkortsapp

/**
 * Datamodell som representerar ett quiz.
 *
 * Innehåller information om quizets ID, titel, beskrivning,
 * tidsgräns samt en lista med tillhörande frågor.
 */
data class QuizModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val questionList: List<QuestionModel>
) {

    /**
     * Tom konstruktor som krävs av Firebase för deserialisering.
     */
    constructor() : this("", "", "", "", emptyList())
}

/**
 * Datamodell som representerar en fråga i ett quiz.
 *
 * Varje fråga har en frågetext, flera svarsalternativ
 * samt ett korrekt svar.
 */
data class QuestionModel(
    val question: String,
    val options: List<String>,
    val correct: String
) {

    /**
     * Tom konstruktor som krävs av Firebase för deserialisering.
     */
    constructor() : this("", emptyList(), "")
}
