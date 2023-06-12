package com.rapptrlabs.androidtest.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rapptrlabs.androidtest.CircleTransform
import com.rapptrlabs.androidtest.R
import com.rapptrlabs.androidtest.chat.model.Data
import com.squareup.picasso.Picasso

class ChatAdapter(private var messages: List<Data> = mutableListOf()) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        private var messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private var userName: TextView = itemView.findViewById(R.id.user_name)

        fun bind(message: Data) {
            Picasso.with(itemView.context).load(message.avatar_url).fit().centerCrop()
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder).transform(CircleTransform())
                .into(avatarImageView)
            messageTextView.text = message.message
            userName.text = message.name
        }
    }
}