package com.example.finalproject

import android.app.DatePickerDialog
import android.content.Intent
// import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finalproject.databinding.ActivityAddNoteBinding
import java.util.Calendar

class AddNoteActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)

        val userId = intent.getIntExtra("userId", -1)
        binding.btnShowCalendar.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnSaveNote.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val date = binding.etDate.text.toString()
            val content = binding.etContent.text.toString()

            val note = Note(-1, userId, title, date, content)

            db.insertNote(note)

            Intent(this, MainActivity::class.java).apply {
                putExtra("userId", userId)
            }.also {
                startActivity(it)
                finish()
            }

            Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, dayOfMonth).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = "$year-${month + 1}-$dayOfMonth"
        binding.etDate.setText(selectedDate)
    }
}