package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.database.*

class VisualizarUsuariosActivity : AppCompatActivity() {
    lateinit var listView: ListView
    private lateinit var refUsers: DatabaseReference
    lateinit var userList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_usuarios)

        refUsers = FirebaseDatabase.getInstance().getReference("users")
        listView = findViewById(R.id.listUsuarios)
        userList = mutableListOf()

        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val user = h.getValue(User::class.java)
                        userList.add(user!!)
                    }

                    val adapter = UserAdapter(applicationContext, R.layout.users, userList)
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

        userList = mutableListOf()
    }
}