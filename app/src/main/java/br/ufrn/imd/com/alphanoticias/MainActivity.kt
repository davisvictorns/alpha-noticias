package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var refNoticias: DatabaseReference
    private lateinit var refUsers: DatabaseReference
    private lateinit var btnConfiguracoes: Button
    lateinit var listView: ListView
    lateinit var noticiaList: MutableList<Noticia>
    private var userCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        noticiaList = mutableListOf()
        refNoticias = FirebaseDatabase.getInstance().getReference("noticias")
        refUsers = FirebaseDatabase.getInstance().getReference("users")

        listView = findViewById(R.id.listViewNoticias)

        btnConfiguracoes = findViewById(R.id.btnConfiguracoes)
        btnConfiguracoes.setOnClickListener {
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }

        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.child(auth.uid?: "").getValue(User::class.java)
                    Log.d("SelectUsuarioMain", "Got value $user")
                    userCategory = user?.category.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        refNoticias.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val noticia = h.getValue(Noticia::class.java)
                        noticiaList.add(noticia!!)
                    }

                    Thread.sleep(1000)
                    val adapter = NoticiaAdapter(applicationContext, R.layout.noticias, noticiaList, userCategory)
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

        noticiaList = mutableListOf()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}