package com.example.spendwise.ui.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.spendwise.databinding.FragmentChatbotBinding

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- SIMPLE STATIC CHATBOT (SALMA BOT) ---
        binding.btnSend.setOnClickListener {
            val userText = binding.etMessage.text.toString().trim()
            if (userText.isNotEmpty()) {
                val reply = getSalmaBotReply(userText)
                binding.tvChat.append("\n\nYou: $userText\nSalmaBot: $reply")
                binding.etMessage.text.clear()
            }
        }
    }

    // --- Very simple chatbot brain ---
    private fun getSalmaBotReply(msg: String): String {
        return when {
            msg.contains("hello", true) -> "Hi! I'm Salma 🤍 How can I help you manage your money today?"
            msg.contains("expense", true) -> "To add an expense, go to the Transactions tab and press +."
            msg.contains("income", true) -> "Income helps boost your savings! Track it regularly."
            msg.contains("save", true) -> "A good rule is 20% saving from your monthly income 💸"
            msg.contains("budget", true) -> "Set a weekly spending limit to stay in control 🔥"
            else -> "I'm here to help! Ask me anything about your spending or savings."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
