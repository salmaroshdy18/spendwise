package com.example.spendwise.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwise.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Add your logic here later
    }
}
