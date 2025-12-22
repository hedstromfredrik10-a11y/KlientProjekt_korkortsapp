package com.hfad.korkortsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [LeaderBoard.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeaderBoard : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vy = inflater.inflate(R.layout.fragment_leader_board, container, false)



        return vy
    }

}