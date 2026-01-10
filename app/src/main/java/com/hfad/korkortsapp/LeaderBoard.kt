package com.hfad.korkortsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener

/**
 * Fragment som visar en leaderboard (Top 10) för slutprovet.
 *
 * Leaderboarden hämtas från Firebase och visas i en
 * RecyclerView. Varje rad innehåller användarnamn och poäng.
 * @author Fredrik, Matias, Desbele, Kacper
 */
class LeaderBoard : Fragment() {

    /**
     * Repository som ansvarar för kommunikation med databasen.
     */
    private val repo = QuizRepository()

    /**
     * Listener som används för att lyssna på uppdateringar av Top 10-resultat.
     */
    private var top10Listener: ValueEventListener? = null

    /**
     * RecyclerView som visar leaderboard-listan.
     */
    private lateinit var recyclerView: RecyclerView

    /**
     * ID för quizet vars leaderboard ska visas.
     */
    private val QUIZ_ID = "1"

    /**
     * Skapar och returnerar fragmentets vy.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_leader_board, container, false)

        recyclerView = view.findViewById(R.id.leaderboardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = LeaderBoardAdapter(emptyList())

        return view
    }

    /**
     * Anropas när fragmentet blir synligt för användaren.
     *
     * Startar en lyssnare mot Firebase som hämtar Top 10-resultat för quizet
     * och uppdaterar RecyclerView när data ändras.
     */
    override fun onStart() {
        super.onStart()

        top10Listener = repo.listenTop10(
            quizId = QUIZ_ID,
            onUpdate = { pairs ->
                val items = pairs.map { (_, model) ->
                    LeaderboardModel(
                        username = model.username.ifBlank { "Okänd" },
                        score = model.score
                    )
                }
                recyclerView.adapter = LeaderBoardAdapter(items)
            }
        )
    }
}
