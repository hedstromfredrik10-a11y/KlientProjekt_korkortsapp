package com.hfad.korkortsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener

class LeaderBoard : Fragment() {

    private val repo = QuizRepository()
    private var top10Listener: ValueEventListener? = null

    private lateinit var recyclerView: RecyclerView

    private val QUIZ_ID = "1"  // ÄNDRA om ditt Quiz faktiskt har annat id

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

    override fun onStart() {
        super.onStart()

        // Lyssna på top 10 i quizResults}
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

