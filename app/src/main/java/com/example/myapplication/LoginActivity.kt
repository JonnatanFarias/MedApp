package com.example.myapplication

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        /*deixa tela de login cheia*/
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()


        val textviewRegistrar = findViewById<TextView>(R.id.textViewRegistrar)
        val textUserEmail = findViewById<TextInputEditText>(R.id.EditTextEmail)
        val textUserSenha = findViewById<TextInputEditText>(R.id.EditTextSenha)
        val buttonEntrar = findViewById<Button>(R.id.buttonEntrar)

        /*chama tela registrar*/
        textviewRegistrar.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        })


        buttonEntrar.setOnClickListener(View.OnClickListener {
            if (textUserEmail.text.toString().isEmpty() || textUserSenha.text.toString()
                    .isEmpty()
            ) {

                Toast.makeText(
                    baseContext, "Nenhum campo pode está em branco.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Entrando...")
                progressDialog.setMessage("Realizando o login, por favor aguarde.")
                progressDialog.show()
                auth.signInWithEmailAndPassword(
                    textUserEmail.text.toString(),
                    textUserSenha.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            Toast.makeText(
                                baseContext, "Login realizado com sucesso." + user?.email,
                                Toast.LENGTH_SHORT
                            ).show()
                            /*chama tela principal*/
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "A autenticação falhou, dados de e-mail ou senha errada ou você não tem conta.?",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialog.dismiss()
                        }
                    }
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {

        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}