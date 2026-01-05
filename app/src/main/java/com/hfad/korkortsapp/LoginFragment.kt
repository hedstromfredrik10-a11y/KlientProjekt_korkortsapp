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

class LoginFragment : Fragment() {

    interface Listener {
        fun onLoggedIn()
    }

    private val db by lazy { FirebaseDatabase.getInstance().reference }

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

    private fun normalize(raw: String): String =
        raw.trim().lowercase().replace(" ", "")

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
