package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ConfigActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private lateinit var btnCriarNoticia: Button
    private lateinit var btnToConvidar: Button
    private lateinit var btnLogout: Button
    private var userCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        auth = FirebaseAuth.getInstance()
        refUsers = FirebaseDatabase.getInstance().getReference("users")

        btnCriarNoticia = findViewById(R.id.btnCriarNoticia)
        btnCriarNoticia.setOnClickListener {
            val intent = Intent(this, CreateNoticiaActivity::class.java)
            startActivity(intent)
        }

        btnToConvidar = findViewById(R.id.btnToConvidar)
        btnToConvidar.setOnClickListener {
            val intent = Intent(this, ConvidarActivity::class.java)
            startActivity(intent)
        }

        btnLogout = findViewById(R.id.btnLogout)
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
                    if (userCategory != "viewer"){
                        btnCriarNoticia.visibility = View.VISIBLE;
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        
    }
}