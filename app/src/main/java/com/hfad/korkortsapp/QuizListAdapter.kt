package com.hfad.korkortsapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.korkortsapp.databinding.QuizItemRecyclerRowBinding
import kotlin.jvm.java

/**
 * RecyclerView-adapter som visar en lista med tillgängliga quiz.
 *
 * Varje rad innehåller titel, beskrivning och tidsgräns för ett quiz.
 * När användaren klickar på ett quiz startas QuizActivity.
 *
 * @param quizModelList Lista med quiz som ska visas.
 * @author Fredrik, Matias, Desbele, Kacper
 */
class QuizListAdapter(private val quizModelList: List<QuizModel>) :
    RecyclerView.Adapter<QuizListAdapter.MyViewHolder>() {

    /**
     * ViewHolder som ansvarar för att binda ett QuizModel till layouten.
     */
    class MyViewHolder(private val binding: QuizItemRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binder data från ett quiz till vyerna i listan.
         *
         * Sätter titel, undertitel och tid samt startar QuizActivity
         * när användaren klickar på raden.
         *
         * @param model Quiz som ska visas i raden.
         */
        fun bind(model: QuizModel) {
            binding.apply {
                quizTitleText.text = model.title
                quizSubtitleText.text = model.subtitle
                quizTimeText.text = model.time + " min"
                root.setOnClickListener {
                    val intent = Intent(root.context, QuizActivity::class.java)
                    intent.putExtra("QUIZ_ID", model.id)
                    QuizActivity.questionModelList = model.questionList
                    QuizActivity.time = model.time
                    root.context.startActivity(intent)
                }
            }
        }
    }

    /**
     * Skapar och returnerar en ny MyViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    /**
     * Returnerar antalet quiz i listan.
     */
    override fun getItemCount(): Int {
        return quizModelList.size
    }

    /**
     * Binder ett quiz till en ViewHolder baserat på position.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}
