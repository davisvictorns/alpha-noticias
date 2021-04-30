package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateNoticiaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
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

        auth = FirebaseAuth.getInstance()

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

        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dataHotaAtual: String = formatter.format(localDateTime)

        val noticiaId = ref.push().key
        val noticia = noticiaId?.let { Noticia(it, titulo, descricao, auth.uid, "", dataHotaAtual) }

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