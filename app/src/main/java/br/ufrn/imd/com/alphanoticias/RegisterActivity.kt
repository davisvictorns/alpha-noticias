package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime

class RegisterActivity : AppCompatActivity() {
    lateinit var btnToLogin : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var refConvites: DatabaseReference
    var convidado: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val thisRef = this

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        refConvites = FirebaseDatabase.getInstance().getReference("convites")

        btnToLogin = findViewById(R.id.btnToLogin)
        btnToLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish();
        }

        val registerEditName: EditText = findViewById(R.id.registerEditName)
        val registerEditEmail: EditText = findViewById(R.id.registerEditEmail)
        val registerEditPassword: EditText = findViewById(R.id.registerEditPassword)
        val registerEditConfirmPassword: EditText = findViewById(R.id.registerEditConfirmPassword)

        val emailConvidado = registerEditEmail.text.trim().toString()

        val btnRegister: Button = findViewById(R.id.btn_register)
        btnRegister.setOnClickListener {
            if(registerEditName.text.trim().toString().isNotEmpty() ||
                registerEditEmail.text.trim().toString().isNotEmpty() ||
                registerEditPassword.text.trim().toString().isNotEmpty() ||
                registerEditConfirmPassword.text.trim().toString().isNotEmpty()){

                if(registerEditPassword.text.trim().toString() != registerEditConfirmPassword.text.trim().toString()){
                    Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_LONG).show()
                }else if (registerEditPassword.text.trim().toString().length < 6){
                    Toast.makeText(this, "A senha deve conter 6 ou mais caracteres!", Toast.LENGTH_LONG).show()
                }else{
                    refConvites.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                for (h in p0.children) {
                                    var convite = h.getValue(Convite::class.java)
                                    Log.d("RegisterActivity", "Invite ${convite?.email}")
                                    Log.d("RegisterActivity", "Guest ${registerEditEmail.text.trim().toString()}")

                                    if (convite != null) {
                                        if(convite.email == registerEditEmail.text.trim().toString()){
                                            convite.aceito = true;
                                            convidado = true

                                            conviteAceito(convite)
                                            createUser(registerEditEmail.text.trim().toString(), registerEditPassword.text.trim().toString())
                                        }
                                    }
                                }
                                if(convidado == false){
                                    Toast.makeText(thisRef, "Infelizmente você ainda não foi convidado!", Toast.LENGTH_LONG).show()
                                }
                            }else{
                                Toast.makeText(thisRef, "Infelizmente você ainda não foi convidado!", Toast.LENGTH_LONG).show()
                                Log.d("RegisterActivity", "Não há registros em convites")
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }else{
                Toast.makeText(this, "Você deve preencher todos os campos corretamente!", Toast.LENGTH_LONG).show()
            }

        }

        convidado = false
    }

    private fun createUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task->
            if(task.isSuccessful){
                Log.e("Task Message", "Successful...")

                saveUserToFirebaseDatabase()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish();
            }else{
                Log.e("Task Message", "Failed..." + task.exception)
            }
        }
    }

    private fun saveUserToFirebaseDatabase(){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val name: EditText = findViewById(R.id.registerEditName)
        val user = User(uid, name.text.trim().toString(), "viewer")

        ref.setValue(user).addOnCompleteListener{
            Log.d("RegisterActivity", "Usuario adicionado ao banco Firebase")
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish();
        }
    }

    private fun conviteAceito(convite: Convite){
        refConvites.child(convite.id).setValue(convite).addOnCompleteListener{
            Log.d("RegisterActivity", "Convite Aceito!")
        }
    }
}