package com.example.spendwise

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwise.ui.ForgotPasswordActivity
import com.example.spendwise.ui.MainActivity
import com.example.spendwise.ui.RegisterActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.etEmail)
        val pw = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val tvForgot = findViewById<TextView>(R.id.tvForgot)

        // ▶ SIGN IN BUTTON
        btnLogin.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPw = pw.text.toString().trim()

            if (userEmail.isEmpty() || userPw.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TEMP LOGIN (no database yet)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // ▶ GO TO REGISTER
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // ▶ GO TO FORGOT PASSWORD
        tvForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}
