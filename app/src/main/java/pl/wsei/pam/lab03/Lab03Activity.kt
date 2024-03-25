package pl.wsei.pam.lab03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import pl.wsei.pam.lab01.R
import java.util.Stack

class Lab03Activity : AppCompatActivity() {
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
        val mBoardModel = MemoryBoardView(mBoard, columns, rows)

            /*for(row in 0 until rows) {
               for(col in 0 until columns) {
                     val btn = ImageButton(this).also {
                       it.tag = "${row}x${col}"
                       val layoutParams = GridLayout.LayoutParams()
                       it.setImageResource(R.drawable.baseline_audiotrack_24)
                       layoutParams.width = 0
                       layoutParams.height = 0
                       layoutParams.setGravity(Gravity.CENTER)
                       layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                       layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                       it.layoutParams = layoutParams
                       mBoard.addView(it)
                   }
               }
            }*/

        }
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
        R.drawable.baseline_alarm_24,
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

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val btn = ImageButton(gridLayout.context).apply {
                    tag = "$row$col"
                    val layoutParams = GridLayout.LayoutParams()
                    layoutParams.width = 0
                    layoutParams.height = 0
                    layoutParams.setGravity(Gravity.CENTER)
                    layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                    layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                    this.layoutParams = layoutParams
                    setOnClickListener { onClickTile(this) }
                }
                gridLayout.addView(btn)
                addTile(btn, shuffledIcons.removeAt(0))
            }
        }
    }
    private val deckResource: Int = R.drawable.baseline_back_hand_24
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag]
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
}