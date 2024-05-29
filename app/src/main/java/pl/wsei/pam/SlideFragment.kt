package pl.wsei.pam

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.wsei.pam.lab01.R

class SlideFragment : Fragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_CONTENT = "content"
        private const val ARG_IS_LAST_SLIDE = "is_last_slide"

        fun newInstance(title: String, content: String, isLastSlide: Boolean): SlideFragment {
            val fragment = SlideFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_CONTENT, content)
            args.putBoolean(ARG_IS_LAST_SLIDE, isLastSlide)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_slide, container, false)
        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val contentTextView: TextView = view.findViewById(R.id.contentTextView)
        val startQuizButton: Button = view.findViewById(R.id.startQuizButton)

        val title = arguments?.getString(ARG_TITLE)
        val content = arguments?.getString(ARG_CONTENT)
        val isLastSlide = arguments?.getBoolean(ARG_IS_LAST_SLIDE) ?: false

        titleTextView.text = title
        contentTextView.text = content

        if (isLastSlide) {
            startQuizButton.visibility = View.VISIBLE
            startQuizButton.setOnClickListener {
                val intent = Intent(activity, QuizActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }
}
