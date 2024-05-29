package pl.wsei.pam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.wsei.pam.lab01.R

class SlideFragment : Fragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_CONTENT = "content"

        fun newInstance(title: String, content: String): SlideFragment {
            val fragment = SlideFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_CONTENT, content)
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

        val title = arguments?.getString(ARG_TITLE)
        val content = arguments?.getString(ARG_CONTENT)

        titleTextView.text = title
        contentTextView.text = content

        return view
    }
}
