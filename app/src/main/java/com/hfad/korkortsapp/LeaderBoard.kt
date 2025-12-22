import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.korkortsapp.LeaderBoardItem
import com.hfad.korkortsapp.R

class LeaderBoard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_leader_board, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.leaderboardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Exempeldata (ersätt med riktig data från quizet)
        val leaderboardData = listOf(
            LeaderBoardItem("Anna", 8),
            LeaderBoardItem("Erik", 6),
            LeaderBoardItem("Sara", 10)
        )

        recyclerView.adapter = LeaderBoardAdapter(leaderboardData)

        return view
    }
}
