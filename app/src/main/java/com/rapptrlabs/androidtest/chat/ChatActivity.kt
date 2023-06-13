package com.rapptrlabs.androidtest.chat

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rapptrlabs.androidtest.R
import com.rapptrlabs.androidtest.chat.model.ChatsModel
import com.rapptrlabs.androidtest.databinding.ActivityChatBinding
import com.rapptrlabs.androidtest.getJsonDataFromAsset
import com.rapptrlabs.androidtest.isInternetAvailable
import com.rapptrlabs.androidtest.remote.responsemanager.ResponseStateHandler
import com.rapptrlabs.androidtest.repository.SharedViewModel
import org.koin.android.ext.android.inject

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val sharedViewModel: SharedViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.activity_chat_title)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        /**
         * checks if device is connected to internet.
         * If conditions are met, it makes a network request to get chats from remote api, else
         * it returns dummy chats
         */
        if (isInternetAvailable(this)) observeMessages() else initRecyclerView(getDummyChats(this))
    }

    private fun initRecyclerView(chats: ChatsModel) {
        chatAdapter = ChatAdapter(chats.data)
        binding.recyclerView.apply {
            adapter = chatAdapter
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    /**
     * Reads json file's content as string, convert it to an object using Gson and collects the chats
     */
    private fun getDummyChats(context: Context): ChatsModel {
        val jsonFileString = getJsonDataFromAsset(context, "DummyChatMessages.json")
        val gson = Gson()
        val chatMessageModel = object : TypeToken<ChatsModel>() {}.type
        return gson.fromJson(jsonFileString, chatMessageModel)
    }

    /**
     * Observe chats from remote api and update UI based on the returned response
     */
    private fun observeMessages() {
        sharedViewModel.getChats().observe(this) {
            when(it) {
                is ResponseStateHandler.Loading -> {
                    binding.progressBarContainer.visibility =  View.VISIBLE
                }
                is ResponseStateHandler.Success -> {
                    binding.progressBarContainer.visibility =  View.GONE
                    it.data?.let { chats -> initRecyclerView(chats) }
                }
                is ResponseStateHandler.Error -> {
                    binding.progressBarContainer.visibility =  View.GONE
                    Toast.makeText(this, "Failed to load chats, please try again", Toast.LENGTH_SHORT).show()
                }
                is ResponseStateHandler.Exception -> {
                    binding.progressBarContainer.visibility =  View.GONE
                    Toast.makeText(this, "Failed to load chats, please try again", Toast.LENGTH_SHORT).show()
                }
                is ResponseStateHandler.ThrowableError -> {
                    binding.progressBarContainer.visibility =  View.GONE
                    Toast.makeText(this, "Failed to load chats, please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}