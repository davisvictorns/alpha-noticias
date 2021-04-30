package br.ufrn.imd.com.alphanoticias

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class DetalharNoticiaActivity : AppCompatActivity() {
    private lateinit var refNoticias: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhar_noticia)

        refNoticias = FirebaseDatabase.getInstance().getReference("noticias")

        val dadosNoticia = intent.extras
        val idNoticia = dadosNoticia!!.getString("idNoticia")

        refNoticias.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val noticia = idNoticia?.let { p0.child(it).getValue(Noticia::class.java) }

                    val txtTituloNoticia: TextView = findViewById(R.id.txt_titulo_noticia)
                    val txtDescricaoNoticia: TextView = findViewById(R.id.txt_detalhe_noticia)

                    if (noticia != null) {
                        txtTituloNoticia.text = noticia.titulo
                        txtDescricaoNoticia.text = noticia.descricao
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}