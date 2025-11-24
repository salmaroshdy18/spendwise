package com.example.spendwise.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwise.R
import android.util.Patterns


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val pw1 = findViewById<EditText>(R.id.etPassword)
        val pw2 = findViewById<EditText>(R.id.etPassword2)
        val btn = findViewById<Button>(R.id.btnCreateAccount)

        btn.setOnClickListener {
            val fullName = name.text.toString().trim()
            val emailText = email.text.toString().trim()
            val pw1Text = pw1.text.toString()
            val pw2Text = pw2.text.toString()

            if (fullName.isEmpty() || emailText.isEmpty() ||
                pw1Text.isEmpty() || pw2Text.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pw1Text.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pw1Text != pw2Text) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}