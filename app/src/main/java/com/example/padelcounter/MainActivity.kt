package com.example.padelcounter

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.padelcounter.databinding.ActivityMainBinding
import mu.KotlinLogging


class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var view: View

    private val logger = KotlinLogging.logger {}

    private var point_score = mutableMapOf("red" to 0, "green" to 0)
    private var game_score = mutableMapOf("red" to 0, "green" to 0)

    private var winning_games = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        view = binding.root
        setContentView(view)

        view.findViewById<View>(R.id.red_button).setOnClickListener {
            increaseScore("red", "green")
            updateTextScore()
        }

        view.findViewById<View>(R.id.green_button).setOnClickListener {
            increaseScore("green", "red")
            updateTextScore()
        }
    }

    private fun increaseScore(scoring_team: String, losing_team: String) {
        when (point_score[scoring_team]) {
            0 -> point_score[scoring_team] = 15
            15 -> point_score[scoring_team] = 30
            30 -> point_score[scoring_team] = 40
            40 -> {
                if (point_score[losing_team]!! < 40) {
                    point_score[scoring_team] = 0
                    point_score[losing_team] = 0

                    increaseGame(scoring_team, losing_team)

                } else if (point_score[losing_team] == 40) {
                    point_score[scoring_team] = 50

                } else if (point_score[losing_team] == 50) {
                    point_score[scoring_team] = 40
                    point_score[losing_team] = 40
                }
            }
            50 -> {
                point_score[scoring_team] = 0
                point_score[losing_team] = 0

                increaseGame(scoring_team, losing_team)
            }
        }
    }

    private fun increaseGame(scoring_team: String, losing_team: String) {
        game_score[scoring_team] = game_score[scoring_team]!! + 1
        
        if (game_score[scoring_team] == winning_games) {
            if (game_score[losing_team]!! <= (winning_games - 2)) {
                game_score[scoring_team] = 0
                game_score[losing_team] = 0
                if (scoring_team == "red") showGameLost() else showGameWon()

            } else {
                winning_games += 1
            }
        }
    }

    private fun updateTextScore() {
        val green_team_button: Button = view.findViewById(R.id.green_button)
        val red_team_button: Button = view.findViewById(R.id.red_button)
        val game_score_text: TextView = view.findViewById(R.id.game_score_text)

        updateTextTeamPointScore(green_team_button, "green")
        updateTextTeamPointScore(red_team_button, "red")

        game_score_text.text = "${game_score["green"]} - ${game_score["red"]}"
    }

    private fun updateTextTeamPointScore(button: Button, team: String) {
        when (point_score[team]) {
            0 -> button.text = "00"
            15 -> button.text = "15"
            30 -> button.text = "30"
            40 -> button.text = "40"
            50 -> button.text = "Adv"
        }
    }

    private fun showGameWon() {
        AlertDialog.Builder(this)
            .setTitle("GAME WON!!!")
            .setMessage("Your team has won this game")
            .setNegativeButton("Dismiss", null)
            .setIcon(android.R.drawable.btn_star_big_on)
            .show()
    }

    private fun showGameLost() {
        AlertDialog.Builder(this)
            .setTitle("GAME LOST...")
            .setMessage("Your team has lost this game")
            .setNegativeButton("Dismiss", null)
            .setIcon(android.R.drawable.ic_delete)
            .show()
    }
}