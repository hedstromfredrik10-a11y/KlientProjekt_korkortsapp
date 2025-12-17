package com.hfad.korkortsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        quizModelList.add(QuizModel("1", "Skyltar", "Allt om skyltar", "15"))
        quizModelList.add(QuizModel("2", "Miljö", "Allt om miljö", "15"))
        quizModelList.add(QuizModel("3", "Bilen", "Allt om bilen", "15"))
        setUpRecyclerView()

    }


}