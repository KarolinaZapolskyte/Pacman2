package org.pondar.pacmankotlin

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.widget.TextView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity() : AppCompatActivity() {

    //reference to the game class.
    private var game: Game? = null

    private var myTimer: Timer = Timer()
    private var myTimer2: Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        game = Game(this,pointsView)

        //intialize the game view clas and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        moveRight.setOnClickListener {
            game?.movePacmanRight(100)
        }
        moveLeft.setOnClickListener {
            game?.movePacmanLeft(100)
        }
        moveUp.setOnClickListener {
            game?.movePacmanUp(100)
        }
        moveDown.setOnClickListener {
            game?.movePacmanDown(100)
        }
        stopGame.setOnClickListener {
            game?.stopGame()
        }
        continueGame.setOnClickListener {
            game?.continueGame()
        }



        game?.running = true

        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }

        }, 0, 200)

        myTimer2.schedule(object : TimerTask() {
            override fun run() {
                timerMethod2()
            }

        }, 0, 1000)

    }

    private fun timerMethod2() {

        this.runOnUiThread(timerTick2)
    }

    private fun timerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //we could do updates here TO GAME LOGIC,
        // but not updates TO ACTUAL UI

        // gameView.move(20)  // BIG NO NO TO DO THIS - WILL CRASH ON OLDER DEVICES!!!!


        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(timerTick)
    }

    @SuppressLint("StringFormatInvalid")
    private val timerTick2 = Runnable {

        if (game!!.running) {
            game?.timer = game!!.timer - 1

            timerView.text = getString(R.string.time_left,game?.timer.toString()) + game?.timer
        }
        if (game!!.timer == 0) {
            game?.running = false
            timerView.text = getString(R.string.time_left,game?.timer.toString()) + "Game over"
        }
    }

    private val timerTick = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game!!.running) {
//            //update the counter - notice this is NOT seconds in this example
//            //you need TWO counters - one for the timer count down that will
//            // run every second and one for the pacman which need to run
//            //faster than every second
            if (game?.direction==game?.right)
            { // move right
                gameView.moveRight(20)
                //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this - you have already made that
            }
            else if (game?.direction==game?.up)
            {
                gameView.moveUp(20)
            }
            else if (game?.direction==game?.left)
            {
                gameView.moveLeft(20)
            }
            else {
                gameView.moveDown(20)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
