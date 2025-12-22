package com.hfad.korkortsapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class QuizRepository {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun saveQuizResult(
        quizId: String,
        userId: String,
        username: String,
        score: Int,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        val result = LeaderboardModel(
            username = username,
            score = score,
            timestamp = System.currentTimeMillis()
        )

        db.child("quizResults")
            .child(quizId)
            .child(userId)
            .setValue(result)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun listenTop10(
        quizId: String,
        onUpdate: (List<Pair<String, LeaderboardModel>>) -> Unit,
        onError: (DatabaseError) -> Unit = {}
    ): ValueEventListener {
        val query = top10Query(quizId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Pair<String, LeaderboardModel>>()

                for (child in snapshot.children) {
                    val uid = child.key ?: continue
                    val result = child.getValue(LeaderboardModel::class.java) ?: continue
                    list.add(uid to result)
                }

                list.sortByDescending { it.second.score }
                onUpdate(list)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        }

        query.addValueEventListener(listener)
        return listener
    }

    fun removeTop10Listener(quizId: String, listener: ValueEventListener) {
        top10Query(quizId).removeEventListener(listener)
    }

    fun saveQuizResultForCurrentUser(
        quizId: String,
        username: String,
        score: Int,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: return onError(IllegalStateException("Ingen inloggad user (FirebaseAuth)."))

        saveQuizResult(
            quizId = quizId,
            userId = uid,
            username = username,
            score = score,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    private fun top10Query(quizId: String): Query {
        return db.child("quizResults")
            .child(quizId)
            .orderByChild("score")
            .limitToLast(10)
    }
}
