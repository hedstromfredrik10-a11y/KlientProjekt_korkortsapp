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

    private val repo = QuizRepository()
    private var quizId: String = ""

    companion object {
        var questionModelList: List<QuestionModel> = listOf()
        var time: String = ""
    }

    private lateinit var binding: ActivityQuizBinding

    private var currentQuestionIndex = 0
    private var selectedAnswer = ""
    private var score = 0
    private val list = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizId = intent.getStringExtra("QUIZ_ID") ?: ""
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
            backBtn.setOnClickListener(this@QuizActivity)
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
                var tiden = binding.timerIndicatorTextview.text
                if(tiden == "00:00") {
                    finishQuiz()
                }
            }

            override fun onFinish() {
                // valfritt: auto-avsluta quiz här
                // finishQuiz()
            }
        }.start()
    }

    private fun loadQuestions() {
        selectedAnswer = ""
        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        binding.apply {
            questionIndicatorTextview.text =
                "Fråga ${currentQuestionIndex + 1} av ${questionModelList.size} "

            questionProgressIndicator.progress =
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()

            questionTextview.text = questionModelList[currentQuestionIndex].question
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
            if (selectedAnswer == questionModelList[currentQuestionIndex].correct) {
                score++
                list.add(1)
                Log.i("Poäng", score.toString())
            } else {
                list.add(0)
            }

            currentQuestionIndex++
            loadQuestions()
        } else if (clickedBtn.id == R.id.back_btn) {
            if (currentQuestionIndex == 0) return

            currentQuestionIndex--

            if (list[currentQuestionIndex] == 1) {
                score--
            }

            loadQuestions()
        } else {
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }

        Log.i("Poäng", currentQuestionIndex.toString())
    }

    private fun finishQuiz() {
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val username = UserSession.getUsername(this) ?: "okand"
        val userId = username

        repo.saveQuizResult(
            quizId = quizId,
            userId = userId,
            username = username,
            score = score,
            onSuccess = { Log.d("QUIZ", "Resultat sparat") },
            onError = { Log.e("QUIZ", "Kunde inte spara: ${it.message}") }
        )

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            scoreProgressText.text = "$percentage %"

            var tiden = binding.timerIndicatorTextview.text
            if(tiden == "00:00") {
                scoreTitle.text = "Tiden gick ut"
                scoreTitle.setTextColor(Color.RED)
            }

            if (percentage > 60) {
                scoreTitle.text = "Grattis! Du klarade provet"
                scoreTitle.setTextColor(Color.GREEN)
            } else {
                scoreTitle.text = "Inte godkänt"
                scoreTitle.setTextColor(Color.RED)
            }

            scoreSubtitle.text = "$score av $totalQuestions är korrekt"
            finishBtn.setOnClickListener { finish() }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()
    }

}
