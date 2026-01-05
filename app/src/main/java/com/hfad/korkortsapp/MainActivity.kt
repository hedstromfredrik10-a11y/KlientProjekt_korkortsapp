package com.hfad.korkortsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.hfad.korkortsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoginFragment.Listener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var quizModelList: MutableList<QuizModel>
    private lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()

        UserSession.clear(this)
        binding.contentLayout.visibility = View.GONE
        openFragment(LoginFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> { showHome(); true }
                R.id.leaderboard -> { showLeaderboard(); true }
                else -> false
            }
        }
    }

    override fun onLoggedIn() {
        showHome()
    }

    private fun setUpRecyclerView() {
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase() {
        FirebaseDatabase.getInstance()
            .getReference("quizes")
            .get()
            .addOnSuccessListener { dataSnapshot ->
                quizModelList.clear()
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) quizModelList.add(quizModel)
                    }
                }
                setUpRecyclerView()
            }
    }

    private fun showHome() {
        binding.contentLayout.visibility = View.VISIBLE
        supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    private fun showLeaderboard() {
        binding.contentLayout.visibility = View.GONE
        openFragment(LeaderBoard())
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
