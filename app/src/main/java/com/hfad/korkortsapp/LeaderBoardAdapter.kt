package com.hfad.korkortsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.korkortsapp.LeaderBoardItem
import com.hfad.korkortsapp.R

class LeaderBoardAdapter(
    private val items: List<LeaderBoardItem>
) : RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameText: TextView = view.findViewById(R.id.userNameText)
        val userScoreText: TextView = view.findViewById(R.id.userScoreText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.userNameText.text = item.userName
        holder.userScoreText.text = "${item.score} poäng"
    }

    override fun getItemCount(): Int = items.size
}
