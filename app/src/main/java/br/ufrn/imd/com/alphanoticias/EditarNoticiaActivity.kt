package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditarNoticiaActivity : AppCompatActivity() {
    private lateinit var refNoticias: DatabaseReference
    private lateinit var refUsers: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var btnEditNoticia: Button
    lateinit var edtTituloNoticia: EditText
    lateinit var edtDescricaoNoticia: EditText
    lateinit var noticia: Noticia

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_noticia)

        auth = FirebaseAuth.getInstance()
        refNoticias = FirebaseDatabase.getInstance().getReference("noticias")
        refUsers = FirebaseDatabase.getInstance().getReference("users")
        btnEditNoticia = findViewById(R.id.btn_edit_noticia)
        edtTituloNoticia = findViewById(R.id.editTitulo)
        edtDescricaoNoticia = findViewById(R.id.editDescricao)

        val dadosNoticia = intent.extras
        val idNoticia = dadosNoticia!!.getString("idNoticia")

        refNoticias.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    noticia = idNoticia?.let { p0.child(it).getValue(Noticia::class.java) }!!

                    edtTituloNoticia.setText(noticia.titulo)
                    edtDescricaoNoticia.setText(noticia.descricao)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btnEditNoticia.setOnClickListener {
            editNoticia(noticia)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun editNoticia(noticia: Noticia) {
        val titulo = edtTituloNoticia.text.toString()
        val descricao = edtDescricaoNoticia.text.toString()

        if (titulo.isEmpty()) {
            edtTituloNoticia.error = "Digite um titulo"
            return
        }
        if (descricao.isEmpty()) {
            edtDescricaoNoticia.error = "Digite uma descricao"
            return
        }

        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val dataHotaAtual: String = formatter.format(localDateTime)

        noticia.titulo = titulo
        noticia.descricao = descricao
        noticia.criada_as = dataHotaAtual
        refNoticias.child(noticia.id).setValue(noticia).addOnCompleteListener {
            Toast.makeText(
                applicationContext,
                "Notícia atualizada com sucesso",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}