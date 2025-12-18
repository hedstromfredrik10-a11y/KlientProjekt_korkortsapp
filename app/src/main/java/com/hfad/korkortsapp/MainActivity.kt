package com.hfad.korkortsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.mutableLongListOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.korkortsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList: MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()


    }

    private fun setUpRecyclerView() {
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

    }

    private fun getDataFromFirebase() {
        //Bestäm frågorna

//        FirebaseDatabase.getInstance().reference
//            .get()
//            .addOnSuccessListener { dataSnapshot ->
//                if (dataSnapshot.exists()) {
//                    for (snapshot in dataSnapshot.children) {
//                        val quizModel = snapshot.getValue(QuizModel::class.java)
//                        if (quizModel != null) {
//                            quizModelList.add(quizModel)
//                        }
//                    }
//                }
//                setUpRecyclerView()
//            }

        var categoryId = "trafik"
        FirebaseFirestore.getInstance().collection("Categories").document(categoryId)
            .collection("questions")
            .whereEqualTo("order", 1).get()
            .addOnSuccessListener { dataSnapshot ->
                if (!dataSnapshot.isEmpty) {
                    for (snapshot in dataSnapshot.documents) {
                        val q = snapshot?.toObject(QuestionModel::class.java)
                        val listQuestionModel = mutableListOf<QuestionModel>()
//                        constructor() : this("", emptyList(), 0, 0, "")
                        listQuestionModel.add(QuestionModel(q?.text ?: "",
                            q?.options ?: mutableListOf(), q?.correctIndex ?: 0, q?.order ?: 1, q?.imagePath
                        ))

                        val quizModel = QuizModel(1, "hej", "000", listQuestionModel)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }
                setUpRecyclerView()
            }

//        val listQuestionModel = mutableListOf<QuestionModel>()
//        listQuestionModel.add(QuestionModel("Fråga 1", mutableListOf("1", "2", "3", "4"), 3))
//        listQuestionModel.add(QuestionModel("Fråga 2", mutableListOf("1", "2", "3", "4"), "3"))
//        listQuestionModel.add(QuestionModel("Fråga 3", mutableListOf("1", "2", "3", "4"), "3"))
//        listQuestionModel.add(QuestionModel("Fråga 4", mutableListOf("1", "2", "3", "4"), "3"))
//
//        quizModelList.add(QuizModel(1, "Skyltar", "20", listQuestionModel))

//        setUpRecyclerView()
    }


}