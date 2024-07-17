package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordRepeat = binding.etPasswordRepeat.text.toString()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
                Toast.makeText(this, "All data must filled!", Toast.LENGTH_SHORT).show()
            } else {
                if (!db.checkEmail(email)) {
                    if (password == passwordRepeat) {
                        val user = User(email, password, fullName)

                        db.registerUser(user)

                        Intent(this, LoginActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }

                        Toast.makeText(this, "Account Registered!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Password not Match", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Email already used!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        db.close()
        
        binding.tvLogin.setOnClickListener { 
            Intent(this, LoginActivity::class.java).also { 
                startActivity(it)
                finish()
            }
        }
    }
}