package pl.wsei.pam.lab02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import pl.wsei.pam.lab01.Lab01Activity
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab03.Lab03Activity

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
        val rows: Int = Character.getNumericValue(boardSize[0])
        val columns: Int = Character.getNumericValue(boardSize[4])
        val intent = Intent(this, Lab03Activity::class.java)
        intent.putExtra("rows",rows)
        intent.putExtra("columns",columns)
        startActivity(intent)
        Toast.makeText(this, "Creating a $rows x $columns board", Toast.LENGTH_SHORT).show()
    }

}