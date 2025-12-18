package com.hfad.korkortsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.mutableLongListOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
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

        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }
                setUpRecyclerView()
                //dett är en ändring
            }

//        val listQuestionModel = mutableListOf<QuestionModel>()
//        listQuestionModel.add(QuestionModel("Fråga 1", mutableListOf("1", "2", "3", "4"), "3"))
//        listQuestionModel.add(QuestionModel("Fråga 2", mutableListOf("1", "2", "3", "4"), "3"))
//        listQuestionModel.add(QuestionModel("Fråga 3", mutableListOf("1", "2", "3", "4"), "3"))
//        listQuestionModel.add(QuestionModel("Fråga 4", mutableListOf("1", "2", "3", "4"), "3"))
//
//        quizModelList.add(QuizModel("1", "Skyltar", "Allt om skyltar", "20", listQuestionModel))
//
//        setUpRecyclerView()
    }


}