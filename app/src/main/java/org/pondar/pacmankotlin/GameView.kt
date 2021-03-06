package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View


//note we now create our own view class that extends the built-in View class
class GameView : View {

    private var game: Game? = null
    private var h: Int = 0
    private var w: Int = 0 //used for storing our height and width of the view

    fun setGame(game: Game?) {
        this.game = game
    }


    /* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //In the onDraw we put all our code that should be
    //drawn whenever we update the screen.
    override fun onDraw(canvas: Canvas) {
        //Here we get the height and weight
        h = canvas.height
        w = canvas.width
        //update the size for the canvas to the game.
        game?.setSize(h, w)
        Log.d("GAMEVIEW", "h = $h, w = $w")

        //are the coins initiazlied?
        //if not initizlise them
        if (!(game!!.coinsInitialized))
            game?.initializeGoldcoins()


        if (!(game!!.enemyInitialized))
            game?.initializeEnemies()

        //Making a new paint object
        val paint = Paint()
        canvas.drawColor(Color.WHITE) //clear entire canvas to white color

        //draw the pacman
        canvas.drawBitmap(game!!.pacBitmap, game?.pacx!!.toFloat(),
                game?.pacy!!.toFloat(), paint)

        //TODO loop through the list of goldcoins and draw them here
        for (coin in game?.coins!!) {
            if(coin.taken == false)
            canvas.drawBitmap(game!!.coinBitmap, coin?.x!!.toFloat(),
                    coin?.y!!.toFloat(), paint)

        }
        // Loop through enemies
        for (enemy in game?.enemies!!) {
            if(enemy.isAlive == true)
                canvas.drawBitmap(game!!.enemyBitmap, enemy?.x!!.toFloat(),
                        enemy?.y!!.toFloat(), paint)

        }

        game?.doCollisionCheck()
        super.onDraw(canvas)
    }


    fun moveRight(x: Int) {
        //still within our boundaries?
        if (game!!.pacx + x + game!!.pacBitmap.width < w)
            game?.pacx = game!!.pacx + x
        invalidate() //redraw everything
    }
    fun moveUp(y: Int) {
        //still within our boundaries?
        if (game!!.pacy - y > 0)
            game?.pacy = game!!.pacy - y
        invalidate() //redraw everything
    }
    fun moveLeft(x: Int) {
        //still within our boundaries?
        if (game!!.pacx - x > 0)
            game?.pacx = game!!.pacx - x
        invalidate() //redraw everything
    }
    fun moveDown(y: Int) {
        //still within our boundaries?
        if (game!!.pacy + y + game!!.pacBitmap.height < h)
            game?.pacy = game!!.pacy + y
        invalidate() //redraw everything
    }

    fun moveRightEnemy(x: Int) {
        //still within our boundaries?
        for (enemy in game!!.enemies)
        {
            if (enemy.x + x + game!!.enemyBitmap.width < w)
                enemy.x = enemy.x + x
            invalidate() //redraw everything
            if (game!!.pacy < enemy.y)
                enemy.direction = game!!.up
            else if (game!!.pacx < enemy.x)
                enemy.direction = game!!.left
            else if (game!!.pacy > enemy.y)
                enemy.direction = game!!.down
        }

    }
    fun moveUpEnemy(y: Int) {
        //still within our boundaries?
        for (enemy in game!!.enemies) {
            if (enemy.y - y > 0)
                enemy.y = enemy.y - y
            invalidate() //redraw everything
            if (game!!.pacx > enemy.x)
                enemy.direction = game!!.right
            else if (game!!.pacx < enemy.x)
                enemy.direction = game!!.left
            else if (game!!.pacy > enemy.y)
                enemy.direction = game!!.down
        }
    }
    fun moveLeftEnemy(x: Int) {
        //still within our boundaries?
        for (enemy in game!!.enemies) {
            if (enemy.x - x > 0)
                enemy.x = enemy.x - x
            invalidate() //redraw everything
            if (game!!.pacy < enemy.y)
                enemy.direction = game!!.up
            else if (game!!.pacx > enemy.x)
                enemy.direction = game!!.right
            else if (game!!.pacy > enemy.y)
                enemy.direction = game!!.down
        }
    }
    fun moveDownEnemy(y: Int) {
        //still within our boundaries?
        for (enemy in game!!.enemies) {
            if (enemy.y + y + game!!.enemyBitmap.height < h)
                enemy.y = enemy.y + y
            invalidate() //redraw everything
            if (game!!.pacy < enemy.y)
                enemy.direction = game!!.up
            else if (game!!.pacx < enemy.x)
                enemy.direction = game!!.left
            else if (game!!.pacx > enemy.x)
                enemy.direction = game!!.right
        }
    }

}
