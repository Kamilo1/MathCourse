package pl.wsei.pam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.wsei.pam.lab01.R
class TopicsAdapter(private val onClick: (String) -> Unit) : RecyclerView.Adapter<TopicsAdapter.TopicViewHolder>() {

    private var topics: List<String> = emptyList()

    fun submitList(newTopics: List<String>) {
        topics = newTopics
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    override fun getItemCount(): Int = topics.size

    class TopicViewHolder(itemView: View, private val onClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.topicTextView)

        fun bind(topic: String) {
            textView.text = topic
            itemView.setOnClickListener {
                onClick(topic)
            }
        }
    }
}
