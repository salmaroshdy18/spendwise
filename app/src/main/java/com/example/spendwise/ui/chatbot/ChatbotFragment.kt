package com.example.spendwise.ui.chatbot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spendwise.R
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

        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                addUserMessage(message)
                botReply(message)
                binding.etMessage.text.clear()
            }
        }

        binding.btnBudgetTips.setOnClickListener {
            addUserMessage("Budget Tips")
            botReply("budget tips")
        }

        binding.btnExpenseAdvice.setOnClickListener {
            addUserMessage("Expense Advice")
            botReply("expense advice")
        }

        binding.btnSavingTips.setOnClickListener {
            addUserMessage("Saving Tips")
            botReply("saving tips")
        }
    }

    private fun addUserMessage(msg: String) {
        val view = layoutInflater.inflate(
            R.layout.item_user_message,
            binding.chatContainer,
            false
        )
        view.findViewById<TextView>(R.id.tvUser).text = msg
        binding.chatContainer.addView(view)
        scrollToBottom()
    }

    private fun addBotMessage(msg: String) {
        val view = layoutInflater.inflate(
            R.layout.item_bot_message,
            binding.chatContainer,
            false
        )
        view.findViewById<TextView>(R.id.tvBot).text = msg
        binding.chatContainer.addView(view)
        scrollToBottom()
    }

    private fun scrollToBottom() {
        binding.chatScroll.post {
            binding.chatScroll.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun botReply(input: String) {
        val msg = input.lowercase()

        val reply = when {
            msg.contains("budget") ->
                "A great budget rule is the 50/30/20 method! Want me to break it down?"

            msg.contains("expense") ->
                "Try tracking expenses daily. Small habits prevent end-of-month surprises!"

            msg.contains("saving") ->
                "Try saving at least 10% of your income. I can help you set goals!"

            msg.contains("hello") || msg.contains("hi") ->
                "Hello! How can I help you today? 😊"

            msg.contains("help") ->
                "You can ask me about: Budget • Saving • Expense tips"

            else ->
                "I'm still learning! Try asking about Budget, Saving, or Expense tips."
        }

        Handler(Looper.getMainLooper()).postDelayed({
            addBotMessage(reply)
        }, 600) // small delay for typing effect
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
