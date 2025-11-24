package com.example.spendwise.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spendwise.R

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
            if (name.text.isEmpty() || email.text.isEmpty() ||
                pw1.text.isEmpty() || pw2.text.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            } else if (pw1.text.toString() != pw2.text.toString()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}