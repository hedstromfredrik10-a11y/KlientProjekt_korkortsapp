package com.hfad.korkortsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.hfad.korkortsapp.databinding.ActivityMainBinding

/**
 * Huvudaktivitet för applikationen.
 *
 * Ansvarar för:
 * - Inloggningsflödet
 * - Navigering mellan startsida och leaderboard
 * - Hämtning och visning av quiz-data från Firebase
 *
 * @author Fredrik, Matias, Desbele, Kacper
 */
class MainActivity : AppCompatActivity(), LoginFragment.Listener {

    /**
     * ViewBinding för activity_main.xml.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Lista med quiz som hämtas från Firebase.
     */
    private lateinit var quizModelList: MutableList<QuizModel>

    /**
     * Adapter som används för att visa quiz-listan i RecyclerView.
     */
    private lateinit var adapter: QuizListAdapter

    /**
     * Anropas när aktiviteten skapas.
     *
     * Initierar UI, laddar quiz-data, rensar eventuell användarsession
     * och visar inloggningsfragmentet.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()

        UserSession.clear(this)
        binding.contentLayout.visibility = View.GONE
        binding.bottomNavigation.visibility = View.GONE
        openFragment(LoginFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    showHome(); true
                }

                R.id.leaderboard -> {
                    showLeaderboard(); true
                }

                R.id.logout -> {
                    showLogin(); true
                }

                else -> false
            }
        }
    }

    /**
     * Anropas när användaren har loggat in via LoginFragment och
     * visar startsidan med quiz-listan.
     */
    override fun onLoggedIn() {
        showHome()
    }

    /**
     * Initierar RecyclerView för att visa quiz-listan.
     */
    private fun setUpRecyclerView() {
        adapter = QuizListAdapter(quizModelList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    /**
     * Hämtar quiz-data från Firebase Realtime Database.
     *
     * Data hämtas från noden "quizes" och omvandlas till en lista
     * av QuizModel-objekt.
     */
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

    /**
     * Visar startsidan med quiz-listan och bottennavigeringen.
     */
    private fun showHome() {
        binding.contentLayout.visibility = View.VISIBLE
        binding.bottomNavigation.visibility = View.VISIBLE
        supportFragmentManager.findFragmentById(R.id.fragmentContainer)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    /**
     * Visar leaderboard-fragmentet.
     *
     * Döljer quiz-listan och öppnar LeaderBoard.
     */
    private fun showLeaderboard() {
        binding.contentLayout.visibility = View.GONE
        openFragment(LeaderBoard())
    }

    /**
     * Loggar ut användaren och visar inloggningsfragmentet.
     *
     * Rensar sparad användarsession och döljer navigation.
     */
    private fun showLogin() {
        UserSession.clear(this)
        binding.contentLayout.visibility = View.GONE
        binding.bottomNavigation.visibility = View.GONE
        openFragment(LoginFragment())
    }

    /**
     * Byter fragment i fragment-containern.
     *
     * @param fragment Fragmentet som ska visas.
     */
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
