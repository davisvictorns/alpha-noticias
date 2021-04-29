package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ConvidarActivity : AppCompatActivity() {

    lateinit var editEmailConvidado: EditText
    lateinit var btnConvidar: Button
    lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convidar)

        database = FirebaseDatabase.getInstance()
        ref = database.getReference("convites")

        editEmailConvidado = findViewById(R.id.editEmailConvidado)
        btnConvidar = findViewById(R.id.btnConvidar)

        btnConvidar.setOnClickListener {
            saveConvite()
        }

    }

    private fun saveConvite(){
        val email = editEmailConvidado.text.toString()

        if (email.isEmpty()) {
            editEmailConvidado.error = "Digite um email"
            return
        }

        val conviteId = ref.push().key
        val convite = conviteId?.let { Convite(it, email) }

        if (conviteId != null) {
            ref.child(conviteId).setValue(convite).addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Convite realizado!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}