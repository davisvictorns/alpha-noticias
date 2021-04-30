package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ConvidarActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var editEmailConvidado: EditText
    lateinit var btnConvidar: Button
    lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convidar)

        database = FirebaseDatabase.getInstance()
        ref = database.getReference("convites")

        auth = FirebaseAuth.getInstance()

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

        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dataHotaAtual: String = formatter.format(localDateTime)


        val conviteId = ref.push().key
        val convite = conviteId?.let { Convite(it, email,"viewer", false, auth.uid, dataHotaAtual) }

        if (conviteId != null) {
            ref.child(conviteId).setValue(convite).addOnCompleteListener {
                Log.d("RegisterActivity", "Convite realizado!")
                Toast.makeText(
                    applicationContext,
                    "Convite realizado!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}