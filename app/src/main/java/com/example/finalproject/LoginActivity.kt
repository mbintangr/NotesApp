package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or Password is Empty!", Toast.LENGTH_SHORT).show()
            } else {
                val id = db.loginAccount(email, password)
                if (id != -1) {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("userId", id)
                    }
                    startActivity(intent)
                    db.close()

                    finish()
                } else {
                    Toast.makeText(this, "Wrong Email or Password!", Toast.LENGTH_SHORT).show()

                    db.close()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

    }
}