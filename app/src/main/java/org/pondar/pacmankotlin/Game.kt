package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.widget.TextView
import java.lang.Math.pow
import java.lang.Math.sqrt
import java.util.ArrayList
import kotlin.random.Random


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView,view2: TextView, view3: TextView) {

        private var pointsView: TextView = view
        private var timerView: TextView = view2
        private var levelView: TextView = view3
    var points : Int = 0
    var oldPoints : Int = 0
        //bitmap of the pacman
        var pacBitmap: Bitmap
        var pacx: Int = 0
        var pacy: Int = 0

        // bitmap of the coins
        var coinBitmap: Bitmap

        // directions
        var left: Int = 1
        var up: Int = 2
        var right: Int = 3
        var down: Int = 4

        var running = false
        var direction = right
        var timer: Int = 60

        //did we initialize the coins?
        var coinsInitialized = false
        var enemyInitialized = false

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>()
        var coinAmount = 1
        //a reference to the gameview
        private var gameView: GameView? = null
        private var h: Int = 0
        private var w: Int = 0 //height and width of screen

        // enemies
        var enemies = ArrayList<Enemy>();
        var enemyBitmap: Bitmap
        var enemyAmount = 1
        var enemyDirection = right

        var currentLevel: Int = 1


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
    }

    init {
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.gold_coin2)
    }

    init {
        enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy2)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins()
    {
        //DO Stuff to initialize the array list with some coins.
        for (i in 1..(coinAmount + (currentLevel*2)))
        coins.add(GoldCoin(Random.nextInt(200, w-200),Random.nextInt(200, h-200),false))

        coinsInitialized = true
    }

    fun initializeEnemies()
    {
        //DO Stuff to initialize the array list with some coins.
        for (i in 1..currentLevel)
            enemies.add(Enemy(Random.nextInt(200, w-200),Random.nextInt(200, h-200), true, right))

        enemyInitialized = true
    }

    fun distance(x1:Int,y1:Int,x2:Int,y2:Int): Double {
        // calculate distance and return it
       return sqrt(pow(((x2-x1).toDouble()), 2.0)+pow(((y2-y1).toDouble()), 2.0))
    }


        fun newGame() {
        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.
        //reset the points
        coinsInitialized = false
        coins = ArrayList<GoldCoin>()
        enemies = ArrayList<Enemy>()
        enemyInitialized = false
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        timer = 60
        timerView.text = "${context.resources.getString(R.string.time_left)} $timer"
        levelView.text = "${context.resources.getString(R.string.level)} $currentLevel"
        running = true
        gameView?.invalidate() //redraw screen

    }
    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        direction = right
    }

    fun movePacmanLeft(pixels: Int) {
        direction = left
    }

    fun movePacmanUp(pixels: Int) {
        direction = up
    }

    fun movePacmanDown(pixels: Int) {
        direction = down
    }

    fun stopGame() {
        running = false
    }

    fun continueGame() {
        running = true
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        if (running) {
            for (coin in coins) {
                if (distance(pacx, pacy, coin.x, coin.y) < 100 && coin.taken == false) {
                    coin.taken = true
                    points = points + 1
                    pointsView.text = "${context.resources.getString(R.string.points)} $points"
                }
            }
            for (enemy in enemies) {
                if (distance(pacx, pacy, enemy.x, enemy.y) < 100 && enemy.isAlive == true) {
                    timer = 1
                }
            }
            if (points == coinAmount+ (currentLevel*2) + oldPoints) {
                oldPoints = points
                coinAmount = coinAmount + (currentLevel*2)
                currentLevel++
                newGame()
            }
        }
    }


}