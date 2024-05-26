package pl.wsei.pam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val departments = MutableLiveData(listOf("Arithmetic", "Algebra", "Geometry", "Calculus"))
    val topics = MutableLiveData<List<String>>()

    fun updateTopics(department: String) {
        topics.value = when (department) {
            "Arithmetic" -> listOf("Addition", "Subtraction", "Multiplication", "Division")
            "Algebra" -> listOf("Linear Equations", "Quadratic Equations", "Polynomials")
            "Geometry" -> listOf("Triangles", "Circles", "Angles")
            "Calculus" -> listOf("Limits", "Derivatives", "Integrals")
            else -> emptyList()
        }
    }
}