package com.hfad.korkortsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView-adapter som används för att visa en leaderboard med användarnamn
 * och poäng.
 *
 * @author Fredrik, Matias, Desbele, Kacper
 */
class LeaderBoardAdapter(
    private val items: List<LeaderboardModel>
) : RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {

    /**
     * ViewHolder som håller referenser till vyerna i varje leaderboard-rad.
     * @param view Den inflaterade vyn för ett leaderboard-item.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /**
         * TextView som visar användarens namn.
         */
        val userNameText: TextView = view.findViewById(R.id.userNameText)

        /**
         * TextView som visar användarens poäng.
         */
        val userScoreText: TextView = view.findViewById(R.id.userScoreText)
    }

    /**
     * Skapar och returnerar en ny ViewHolder genom att inflatera
     * layouten för ett leaderboard-item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binder data från ett LeaderboardModel till en ViewHolder.
     *
     * @param holder ViewHolder som ska uppdateras.
     * @param position Positionen i listan som ska visas.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.userNameText.text = item.username
        holder.userScoreText.text = "${item.score} poäng"
    }

    /**
     * Returnerar totalt antal objekt i leaderboarden.
     */
    override fun getItemCount(): Int = items.size
}
