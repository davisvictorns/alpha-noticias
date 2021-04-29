package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var refNoticias: DatabaseReference
    private lateinit var refUsers: DatabaseReference
    private lateinit var btnListarNoticias: Button
    private lateinit var btnToConvidar: Button
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

        btnListarNoticias = findViewById(R.id.btnListarNoticias)
        btnListarNoticias.setOnClickListener {
            val intent = Intent(this, CreateNoticiaActivity::class.java)
            startActivity(intent)
        }
        listView = findViewById(R.id.listViewNoticias)

        btnToConvidar = findViewById(R.id.btnToConvidar)
        btnToConvidar.setOnClickListener {
            val intent = Intent(this, ConvidarActivity::class.java)
            startActivity(intent)
        }

        val btnLogout: Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.child(auth.uid?: "").getValue(User::class.java)
                    Log.d("SelectUsuarioMain", "Got value $user")
                    userCategory = user?.category.toString()
                    if(userCategory != "viewer"){
                        btnListarNoticias.visibility = View.VISIBLE;
                    }
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

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}