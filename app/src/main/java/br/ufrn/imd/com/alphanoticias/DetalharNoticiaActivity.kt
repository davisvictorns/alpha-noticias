package br.ufrn.imd.com.alphanoticias

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class DetalharNoticiaActivity : AppCompatActivity() {
    private lateinit var refNoticias: DatabaseReference
    private lateinit var refUsers: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhar_noticia)

        refNoticias = FirebaseDatabase.getInstance().getReference("noticias")
        refUsers = FirebaseDatabase.getInstance().getReference("users")

        val dadosNoticia = intent.extras
        val idNoticia = dadosNoticia!!.getString("idNoticia")

        refNoticias.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val noticia = idNoticia?.let { p0.child(it).getValue(Noticia::class.java) }

                    val txtTituloNoticia: TextView = findViewById(R.id.txt_titulo_noticia)
                    val txtDescricaoNoticia: TextView = findViewById(R.id.txt_detalhe_noticia)
                    val imageViewer: ImageView = findViewById(R.id.imageViewer)
                    val txtAutorNoticia: TextView = findViewById(R.id.txt_autor_noticia)
                    val txtDthrPublicacao: TextView = findViewById(R.id.txt_dthr_publicacao)

                    if (noticia != null) {
                        val user = p0.child(noticia.autor_id ?: "").getValue(User::class.java)

                        txtTituloNoticia.text = noticia.titulo
                        txtDescricaoNoticia.text = noticia.descricao
                        if (user != null) txtAutorNoticia.text = user.name
                        txtDthrPublicacao.text = noticia.criada_as

                        if(noticia.urlImagem != "") {
                            Picasso.get().load(noticia.urlImagem).into(imageViewer)
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}