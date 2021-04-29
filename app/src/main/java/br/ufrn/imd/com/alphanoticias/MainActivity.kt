package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var btnListarNoticias: Button
    lateinit var listView: ListView
    lateinit var noticiaList: MutableList<Noticia>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        noticiaList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("noticias")

        btnListarNoticias = findViewById(R.id.btnListarNoticias)
        listView = findViewById(R.id.listViewNoticias)

        val btnLogout: Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnListarNoticias.setOnClickListener {
            val intent = Intent(this, CreateNoticiaActivity::class.java)
            startActivity(intent)
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val noticia = h.getValue(Noticia::class.java)
                        noticiaList.add(noticia!!)
                    }

                    val adapter = NoticiaAdapter(applicationContext, R.layout.noticias, noticiaList)
                    listView.adapter = adapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}