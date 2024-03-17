package pl.wsei.pam.lab02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import pl.wsei.pam.lab01.R

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab02)
        val favorites_grid: GridLayout = findViewById(R.id.favorites_grid)
        for (i in 0 until favorites_grid.childCount){
            val button: View = favorites_grid.getChildAt(i)
            if(button is Button)
            {
                button.setOnClickListener {
                    val boardSize: String = button.text.toString()
                    goToBoard(boardSize)
                }
            }
        }
    }
    private fun goToBoard(boardSize: String){
        val rows: Char = boardSize[0]
        println(rows)
        val columns: Char = boardSize[4]
        println(columns)
        Toast.makeText(this, "Creating a $rows x $columns board", Toast.LENGTH_SHORT).show()
    }

}