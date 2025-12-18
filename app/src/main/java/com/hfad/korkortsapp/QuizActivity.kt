package com.hfad.korkortsapp

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.hfad.korkortsapp.databinding.ActivityQuizBinding
import com.hfad.korkortsapp.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
    }

    lateinit var binding: ActivityQuizBinding

    var currentQuestionIndex = 0;
    var selectedAnswer = 0
    var score = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }
        loadQuestions()
        startTimer()
    }

    private fun startTimer() {
        val totalMineInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalMineInMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                binding.timerIndicatorTextview.text =
                    String.format("%02d:%02d", minutes, remainingSeconds)
            }

            override fun onFinish() {


            }

        }.start()
    }

    //Laddar frågorna
    private fun loadQuestions() {
        selectedAnswer = 0
        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        binding.apply {
            //Visar hur många frågor som är besvarade
            questionIndicatorTextview.text =
                "Fråga ${currentQuestionIndex + 1} av ${questionModelList.size} "
            //Visar progress bar
            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].text
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view: View?) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))

        }

        val clickedBtn = view as Button

        if (clickedBtn.id == R.id.next_btn) {
            //Kontrollerar om svaret är korrekt
            if (selectedAnswer == questionModelList[currentQuestionIndex].correctIndex) {
                score++
                Log.i("Poäng", score.toString())
            }

            //Om man trycker på next
            currentQuestionIndex++
            loadQuestions()
        } else {
            //Om man trycker på en alternativ
            selectedAnswer = 0
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }


    }

    private fun finishQuiz() {
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"
            if (percentage > 60) {
                scoreTitle.text = "Grattis! Du klarade provet"
                scoreTitle.setTextColor(Color.GREEN)
            } else {
                scoreTitle.text = "Inte godkänt"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score av $totalQuestions är korrekt"
            finishBtn.setOnClickListener {
                finish()
            }


        }
        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }
}