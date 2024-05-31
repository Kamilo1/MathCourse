package pl.wsei.pam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.wsei.pam.lab01.R
class TopicsAdapter(private val onClick: (String) -> Unit) : RecyclerView.Adapter<TopicsAdapter.TopicViewHolder>() {

    private val topics = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    override fun getItemCount() = topics.size

    fun submitList(newTopics: List<String>) {
        topics.clear()
        topics.addAll(newTopics)
        notifyDataSetChanged()
    }

    class TopicViewHolder(itemView: View, val onClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val topicTextView: TextView = itemView.findViewById(R.id.topicTextView)
        private var currentTopic: String? = null

        init {
            itemView.setOnClickListener {
                currentTopic?.let { onClick(it) }
            }
        }

        fun bind(topic: String) {
            currentTopic = topic
            topicTextView.text = topic
        }
    }
}

