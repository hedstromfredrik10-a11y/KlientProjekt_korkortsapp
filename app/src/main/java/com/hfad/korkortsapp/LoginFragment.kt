package com.hfad.korkortsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase

/**
 * Fragment som hanterar inloggning och registrering av användare.
 *
 * Användardata lagras i Firebase Realtime Database under noden "simpleUsers".
 * Vid lyckad inloggning eller registrering sparas användarnamnet lokalt via
 * [UserSession] och värd-aktiviteten notifieras.
 */
class LoginFragment : Fragment() {

    /**
     * Interface som används för att kommunicera med värd-aktiviteten
     * när användaren har loggat in eller registrerat sig.
     */
    interface Listener {
        /**
         * Anropas när användaren är korrekt inloggad.
         */
        fun onLoggedIn()
    }

    /**
     * Referens till Firebase Realtime Database.
     */
    private val db by lazy { FirebaseDatabase.getInstance().reference }

    /**
     * Skapar och returnerar fragmentets vy.
     *
     * Initierar inmatningsfält för användarnamn och lösenord samt
     * knappar för inloggning och registrering.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val passwordInput = view.findViewById<EditText>(R.id.passwordInput)
        val loginBtn = view.findViewById<Button>(R.id.loginBtn)
        val registerBtn = view.findViewById<Button>(R.id.registerBtn)

        loginBtn.setOnClickListener {
            val username = normalize(usernameInput.text?.toString().orEmpty())
            val pass = passwordInput.text?.toString().orEmpty()

            if (username.isBlank() || pass.isBlank()) {
                toast("Fyll i användarnamn och lösenord")
                return@setOnClickListener
            }

            login(username, pass)
        }

        registerBtn.setOnClickListener {
            val username = normalize(usernameInput.text?.toString().orEmpty())
            val pass = passwordInput.text?.toString().orEmpty()

            if (username.isBlank() || pass.isBlank()) {
                toast("Fyll i användarnamn och lösenord")
                return@setOnClickListener
            }

            register(username, pass)
        }

        return view
    }

    /**
     * Registrerar en ny användare i Firebase.
     *
     * Kontrollerar först om användarnamnet redan finns.
     * Om det är ledigt sparas användarnamn, lösenord och skapandedatum.
     *
     * @param username Användarens valda användarnamn.
     * @param password Användarens lösenord.
     */
    private fun register(username: String, password: String) {
        val ref = db.child("simpleUsers").child(username)

        ref.get().addOnSuccessListener { snap ->
            if (snap.exists()) {
                toast("Användarnamn finns redan")
                return@addOnSuccessListener
            }

            val data = mapOf(
                "password" to password,
                "createdAt" to System.currentTimeMillis()
            )

            ref.setValue(data)
                .addOnSuccessListener {
                    UserSession.setUsername(requireContext(), username)
                    toast("Konto skapat!")
                    (activity as? Listener)?.onLoggedIn()
                }
                .addOnFailureListener { e ->
                    toast("Kunde inte skapa konto: ${e.message}")
                }
        }.addOnFailureListener { e ->
            toast("Fel: ${e.message}")
        }
    }

    /**
     * Loggar in en befintlig användare.
     *
     * Hämtar användardata från Firebase och jämför sparat lösenord
     * med det lösenord som användaren angivit.
     *
     * @param username Användarnamnet som ska loggas in.
     * @param password Lösenordet som ska verifieras.
     */
    private fun login(username: String, password: String) {
        val ref = db.child("simpleUsers").child(username)

        ref.get().addOnSuccessListener { snap ->
            if (!snap.exists()) {
                toast("Kontot finns inte")
                return@addOnSuccessListener
            }

            val saved = snap.child("password").getValue(String::class.java)
            if (saved == password) {
                UserSession.setUsername(requireContext(), username)
                toast("Inloggad!")
                (activity as? Listener)?.onLoggedIn()
            } else {
                toast("Fel lösenord")
            }
        }.addOnFailureListener { e ->
            toast("Fel: ${e.message}")
        }
    }

    /**
     * Normaliserar användarnamn genom att:
     * - ta bort mellanslag
     * - trimma blanksteg
     * - konvertera till små bokstäver
     *
     * @param raw Ursprunglig text från inmatningsfältet.
     * @return Normaliserat användarnamn.
     */
    private fun normalize(raw: String): String =
        raw.trim().lowercase().replace(" ", "")

    /**
     * Visar ett kort Toast-meddelande.
     *
     * @param msg Text som ska visas för användaren.
     */
    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
