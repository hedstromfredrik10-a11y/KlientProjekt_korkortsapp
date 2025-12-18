package com.hfad.korkortsapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

data class Question(
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val order: Int = 0,
    val imagePath: String? = null
)

class DataBaseConnection : AppCompatActivity() {

    //    private lateinit var statusText: TextView
    private lateinit var questionText: TextView
    private lateinit var optionsText: TextView
    private lateinit var knappen: Button
    private lateinit var knappen2: Button
    private lateinit var short: CollectionReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val db = FirebaseFirestore.getInstance()
//        statusText = findViewById<TextView>(R.id.statusText)
        questionText = findViewById<TextView>(R.id.questionText)
        optionsText = findViewById<TextView>(R.id.optionsText)
        knappen = findViewById<Button>(R.id.knappen)
        knappen2 = findViewById<Button>(R.id.knappen2)

        var nummer: Int = 1
        var categoryId = "trafik"

        short = db.collection("Categories").document(categoryId).collection("questions")
//        statusText.text = "Hämtar fråga från Firestore..."

        makeGetShorter(nummer)

        knappen.setOnClickListener {
            nummer++
            makeGetShorter(nummer)
        }

        knappen2.setOnClickListener {
            if (nummer > 1) nummer--
            makeGetShorter(nummer)
        }
    }

    fun makeGetShorter(nummer:Int) {
        short.whereEqualTo("order", nummer).get().addOnSuccessListener { snapshot ->
            val doc = snapshot.documents.firstOrNull()
            val q = doc?.toObject(Question::class.java)

            if (q == null) {
//                statusText.text = "Hittade ingen fråga. Kolla path/namn och att 'order' finns."
                return@addOnSuccessListener
            }

//            statusText.text = "✅ Hämtat!"
            questionText.text = q.text
            optionsText.text = q.options
                .mapIndexed { i, opt -> "${i + 1}. $opt" }
                .joinToString("\n")
        }
            .addOnFailureListener { e ->
//                statusText.text = "❌ Fel: ${e.message}"
            }
    }

}