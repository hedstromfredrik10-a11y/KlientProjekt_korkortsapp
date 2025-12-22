package com.hfad.korkortsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class LoginFragment(
    private val onLoggedIn: () -> Unit
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        val saveBtn = view.findViewById<Button>(R.id.saveUsernameBtn)

        saveBtn.setOnClickListener {
            val name = usernameInput.text?.toString()?.trim().orEmpty()
            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Skriv ett användarnamn", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserSession.setUsername(requireContext(), name)
            UserSession.getOrCreateUserId(requireContext()) // säkerställ stabilt userId
            onLoggedIn()
        }

        return view
    }
}
