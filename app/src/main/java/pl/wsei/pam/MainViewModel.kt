package pl.wsei.pam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val departments = MutableLiveData(listOf("Najpopularniejsze wzory", "Funkcje", "Ciągi", "Geometria"))
    val topics = MutableLiveData<List<String>>()

    fun updateTopics(department: String) {
        topics.value = when (department) {
            "Najpopularniejsze wzory" -> listOf("Wzory skróconego mnożenia", "Twierdzenie Pitagorasa", "Delta")
            "Funkcje" -> listOf("Funkcja liniowa", "Funkcja kwadratowa", "Funkcja wykładnicza", "Funkcja logarytmiczna")
            "Ciągi" -> listOf("Ciągi arytymetyczne", "Ciągi geometryczne")
            "Geometria" -> listOf("Odcinek", "Okrąg", "Symetria")
            else -> emptyList()
        }
    }
}