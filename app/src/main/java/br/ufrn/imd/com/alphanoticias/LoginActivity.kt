package br.ufrn.imd.com.alphanoticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val btnToRegister: TextView = findViewById(R.id.btnToRegister)
        btnToRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginEditEmail: EditText = findViewById(R.id.loginEditEmail)
        val loginEditPassword: EditText = findViewById(R.id.loginEditPassword)

        val btnLogin: Button = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener {
            if(loginEditEmail.text.trim().toString().isNotEmpty() || loginEditPassword.text.trim().toString().isNotEmpty()){
                signInUser(loginEditEmail.text.trim().toString(), loginEditPassword.text.trim().toString())
            }else{
                Toast.makeText(this, "Você deve preencher todos os campos corretamente!", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun signInUser(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task->
            if(task.isSuccessful){
                Log.e("Task Message", "Login Successful...")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish();
            }else{
                Toast.makeText(this, "Usuário não encontrado ou senha incorreta!", Toast.LENGTH_LONG).show()
                Log.e("Task Message", "Login Error..." + task.exception)
            }
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
}