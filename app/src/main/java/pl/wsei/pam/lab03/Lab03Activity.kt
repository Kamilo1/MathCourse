package pl.wsei.pam.lab03

import android.animation.Animator
import android.animation.AnimatorSet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import pl.wsei.pam.lab01.R
import java.util.Stack
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.random.Random
import android.animation.ObjectAnimator
import android.app.Activity
import android.media.MediaPlayer
import android.view.animation.DecelerateInterpolator

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoardModel: MemoryBoardView
    lateinit var completionPlayer: MediaPlayer
    lateinit var losingPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)
        val columns = intent.getIntExtra("columns",2)
        val rows = intent.getIntExtra("rows",3)
        println(rows)
        println(columns)
        var mBoard : GridLayout = findViewById(R.id.MemoryBoard)
        mBoard.columnCount = columns
        mBoard.rowCount = rows
         mBoardModel = MemoryBoardView(mBoard, columns, rows)
        if (savedInstanceState != null) {
            val gameStateString = savedInstanceState.getString("gameState")
            val gameState = gameStateString?.split(",")?.map { it.toInt() } ?: listOf()
            mBoardModel.setState(gameState)
        }

        mBoardModel.setOnGameChangeListener { e ->
                    runOnUiThread {
                        when (e.state) {
                            GameStates.Matching -> {
                                e.tiles.forEach { tile ->
                                    tile.revealed = true
                                }
                            }
                            GameStates.Match -> {
                                e.tiles.forEach { tile ->
                                    tile.revealed = true
                                    completionPlayer.start()
                                    animatePairedButton(tile.button, Runnable {  })
                                }
                            }
                            GameStates.NoMatch -> {
                                e.tiles.forEach { tile ->
                                    tile.revealed = true
                                    losingPlayer.start()
                                    animateMismatchedPair(this@Lab03Activity, tile)
                                }
                                Timer().schedule(1000) {
                                    runOnUiThread {
                                        e.tiles.forEach { tile ->
                                            tile.revealed = false
                                        }
                                    }
                                }
                            }
                            GameStates.Finished -> {
                                Toast.makeText(this@Lab03Activity, "Game finished", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

    }
    override protected fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        losingPlayer = MediaPlayer.create(applicationContext, R.raw.losing)
    }


    override protected fun onPause() {
        super.onPause();
        completionPlayer.release()
        losingPlayer.release()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val gameState = mBoardModel.getState().joinToString(",")
        outState.putString("gameState", gameState)
    }
}
private fun animatePairedButton(button: ImageButton, action: Runnable ) {
    val set = AnimatorSet()
    val random = Random.Default
    button.pivotX = random.nextFloat() * 200f
    button.pivotY = random.nextFloat() * 200f

    val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
    val scallingX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 4f)
    val scallingY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 4f)
    val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)
    set.startDelay = 500
    set.duration = 2000
    set.interpolator = DecelerateInterpolator()
    set.playTogether(rotation, scallingX, scallingY, fade)
    set.addListener(object: Animator.AnimatorListener {

        override fun onAnimationStart(animator: Animator) {
        }

        override fun onAnimationEnd(animator: Animator) {
            button.scaleX = 1f
            button.scaleY = 1f
            button.alpha = 0.0f
            action.run();
        }

        override fun onAnimationCancel(animator: Animator) {
        }

        override fun onAnimationRepeat(animator: Animator) {
        }
    })
    set.start()
}
private fun animateMismatchedPair(context: Activity, tile: Tile) {
    val button = tile.button
    val animatorSet = AnimatorSet()
    val rotateLeft = ObjectAnimator.ofFloat(button, "rotation", 0f, -10f)
    val rotateRight = ObjectAnimator.ofFloat(button, "rotation", -10f, 10f)
    val rotateBack = ObjectAnimator.ofFloat(button, "rotation", 10f, 0f)
    rotateLeft.duration = 100
    rotateRight.duration = 200
    rotateBack.duration = 100

    animatorSet.playSequentially(rotateLeft, rotateRight, rotateBack)
    animatorSet.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            // Po zakończeniu animacji odwróć kartę tyłem
            context.runOnUiThread {
                tile.revealed = false
            }
        }

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {}
    })
    animatorSet.start()
}
data class Tile(val button: ImageButton, val tileResource: Int, val deckResource: Int) {
    init {
        button.setImageResource(deckResource)
    }
    private var _revealed: Boolean = false
    var revealed: Boolean
        get() {
            return _revealed
        }
        set(value){
            _revealed = value
            if(_revealed){
                button.setImageResource(tileResource)
            }
            else {
                button.setImageResource(deckResource)
            }
        }
    fun removeOnClickListener(){
        button.setOnClickListener(null)
    }
}
data class MemoryGameEvent(
    val tiles: List<Tile>,
    val state: GameStates) {
}
enum class GameStates {
    Matching, Match, NoMatch, Finished
}
class MemoryGameLogic(private val maxMatches: Int) {

    private var valueFunctions: MutableList<() -> Int> = mutableListOf()

    private var matches: Int = 0

    fun process(value: () -> Int):  GameStates{
        if (valueFunctions.size < 1) {
            valueFunctions.add(value)
            return GameStates.Matching
        }
        valueFunctions.add(value)
        val result = valueFunctions[0]() == valueFunctions[1]()
        matches += if (result) 1 else 0
        valueFunctions.clear()
        return when (result) {
            true -> if (matches == maxMatches) GameStates.Finished else GameStates.Match
            false -> GameStates.NoMatch
        }
    }
}
class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val icons: List<Int> = listOf(
        R.drawable.baseline_audiotrack_24,
        R.drawable.baseline_rocket_launch_24,
        R.drawable.baseline_alarm_24,
        R.drawable.baseline_agriculture_24,
        R.drawable.gradient_1,
        R.drawable.baseline_back_hand_24,
        R.drawable.baseline_battery_alert_24,
        R.drawable.baseline_beach_access_24,
        R.drawable.baseline_boy_24,
        R.drawable.baseline_child_care_24,
        R.drawable.baseline_celebration_24,
        R.drawable.baseline_currency_bitcoin_24,
        R.drawable.baseline_emoji_events_24,
        R.drawable.baseline_extension_24,
        R.drawable.baseline_headphones_24,
        R.drawable.baseline_language_24,
        R.drawable.baseline_local_airport_24,
        R.drawable.baseline_local_dining_24
        // dodaj kolejne identyfikatory utworzonych ikon
    )

    init {
        val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
            it.addAll(icons.subList(0, cols * rows / 2))
            it.addAll(icons.subList(0, cols * rows / 2))
            it.shuffle()
        }

        for(row in 0 until rows)
        {
            for(col in 0 until cols)
            {
                val tag = "${row}x${col}"
                val btn = ImageButton(gridLayout.context).also {
                    it.tag = tag
                    val layoutParams = GridLayout.LayoutParams()
                    it.setImageResource(R.drawable.baseline_audiotrack_24)
                    layoutParams.width = 0
                    layoutParams.height = 0
                    layoutParams.setGravity(Gravity.CENTER)
                    layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                    layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                    it.layoutParams = layoutParams
                    it.setImageResource(R.drawable.baseline_back_hand_24)
                    gridLayout.addView(it)
                }
                tiles[tag] = (Tile(btn, shuffledIcons.get(0), R.drawable.baseline_rocket_launch_24))
                addTile(btn, shuffledIcons.removeAt(0))
            }
        }
        }

    private val deckResource = R.drawable.baseline_back_hand_24
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag]
        tile?.revealed = !(tile?.revealed ?: false)
        matchedPair.push(tile)
        val matchResult = logic.process {
            tile?.tileResource?:-1
        }
        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))
        if (matchResult != GameStates.Matching) {
            matchedPair.clear()
        }
    }

    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }
    fun getState(): List<Int> {
        return tiles.values.map { tile ->
            if (tile.revealed) tile.tileResource else -1
        }
    }

    fun setState(state: List<Int>) {
        tiles.values.forEachIndexed { index, tile ->
            val tileState = state[index]
            tile.revealed = tileState != -1
            if (tile.revealed) {
                tile.button.setImageResource(tileState)
            } else {
                tile.button.setImageResource(tile.deckResource)
            }
        }
    }


}
