package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notes: List<Note>, context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: DBHelper = DBHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNoteTitle: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val tvNoteDate: TextView = itemView.findViewById(R.id.tvNoteDate)
        val tvNoteContent: TextView = itemView.findViewById(R.id.tvNoteContent)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDeleteNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return  NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesAdapter.NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.tvNoteTitle.text = note.title
        holder.tvNoteDate.text = note.date
        holder.tvNoteContent.text = note.content

        holder.btnDelete.setOnClickListener {
            db.deleteNote(note.noteId)
            refreshData(db.getNotesByUser(note.userId))
            Toast.makeText(holder.itemView.context, "Note Deleted!", Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnClickListener {
            Intent(holder.itemView.context, EditNoteActivity::class.java).apply {
                putExtra("noteId", note.noteId)
                putExtra("userId", note.userId)
            }.also {
                holder.itemView.context.startActivity(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

}