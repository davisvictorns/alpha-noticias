package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class CreateNoticiaActivity : AppCompatActivity() {

    lateinit var editTextTitulo: EditText
    lateinit var editTextDescricao: EditText
    lateinit var btnCreateNoticia: Button
    lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_noticia)

        database = FirebaseDatabase.getInstance()
        ref = database.getReference("noticias")

        editTextTitulo = findViewById(R.id.createTitulo)
        editTextDescricao = findViewById(R.id.createDescricao)
        btnCreateNoticia = findViewById(R.id.btn_create_noticia)

        btnCreateNoticia.setOnClickListener {
            saveNoticia()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun saveNoticia() {
        val titulo = editTextTitulo.text.toString()
        val descricao = editTextDescricao.text.toString()

        if (titulo.isEmpty()) {
            editTextTitulo.error = "Digite um titulo"
            return
        }
        if (descricao.isEmpty()) {
            editTextDescricao.error = "Digite uma descricao"
            return
        }

        val noticiaId = ref.push().key
        val noticia = noticiaId?.let { Noticia(it, titulo, descricao) }

        if (noticiaId != null) {
            ref.child(noticiaId).setValue(noticia).addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Notícia cadastrada com sucesso",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}