package com.hfad.korkortsapp

import com.google.firebase.database.*

/**
 * Repository-klass som hanterar all kommunikation med Firebase
 * kopplat till quizresultat och leaderboard.
 * @author Fredrik, Matias, Desbele, Kacper
 */
class QuizRepository {

    /**
     * Referens till Firebase Realtime Database.
     */
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    /**
     * Sparar ett quizresultat för en användare.
     *
     * @param quizId ID för quizet.
     * @param userId Användarens unika ID (användarnamn).
     * @param username Användarens namn som visas i leaderboard.
     * @param score Användarens poäng.
     * @param onSuccess Callback som anropas vid lyckad lagring.
     * @param onError Callback som anropas vid fel.
     */
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

    /**
     * Lyssnar på och hämtar Top 10-resultat för ett quiz.
     *
     * Resultaten sorteras lokalt i fallande ordning baserat på poäng.
     *
     * @param quizId ID för quizet.
     * @param onUpdate Callback som anropas när data uppdateras.
     * @param onError Callback som anropas vid databasfel.
     * @return En [ValueEventListener] som kan tas bort vid behov.
     */
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

    /**
     * Skapar en Firebase-query som hämtar de 10 högsta poängen
     * för ett specifikt quiz.
     *
     * @param quizId ID för quizet.
     * @return Firebase-query för Top 10.
     */
    private fun top10Query(quizId: String): Query {
        return db.child("quizResults")
            .child(quizId)
            .orderByChild("score")
            .limitToLast(10)
    }

    /**
     * Hämtar användarens tidigare högsta poäng för ett quiz.
     *
     * @param quizId ID för quizet.
     * @param userId Användarens ID.
     * @param onSuccess Callback som returnerar highscore.
     * @param onError Callback som anropas vid fel.
     */
    fun getUserHighScore(
        quizId: String,
        userId: String,
        onSuccess: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.child("quizResults")
            .child(quizId)
            .child(userId)
            .child("score")
            .get()
            .addOnSuccessListener { snapshot ->
                val highScore = snapshot.getValue(Int::class.java) ?: 0
                onSuccess(highScore)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }
}
