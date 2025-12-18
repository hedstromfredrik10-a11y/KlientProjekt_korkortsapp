package com.hfad.korkortsapp

//import android.R

data class QuizModel(
    val id: Int,
    val title: String,
//    val subtitle: String,
    val time: String,
    val qusetionList: List<QuestionModel>

) {
    constructor() : this(0, "", "", emptyList())
}

data class QuestionModel(

    val text: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val order: Int = 0,
    val imagePath: String? = null

//    val question: String,
//    val options: List<String>,
//    val correct: String
) {
//    constructor() : this("", emptyList(), "")
}


