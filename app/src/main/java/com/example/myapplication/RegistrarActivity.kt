package com.example.myapplication

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegistrarActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // INICIA INSTANTIA FIREBASE DATASE
        database = Firebase.database.reference

        /*deixa tela de login cheia*/
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val textviewCadastrarUserName =
            findViewById(R.id.EditTextCadastrarUser) as TextInputEditText

        val textviewCadastrarUserEmail =
            findViewById(R.id.EditTextCadastrarEmail) as TextInputEditText

        val textviewCadastrarUserSenha =
            findViewById(R.id.EditTextCadastrarSenha) as TextInputEditText

        val textviewValidarUserSenha =
            findViewById(R.id.EditTextValidarSenha) as TextInputEditText

        val textInputSenha =
            findViewById(R.id.textInputLayoutSenha) as TextInputLayout

        val textInputValidaSenha =
            findViewById(R.id.textInputLayoutValidar) as TextInputLayout

        val buttoCadastrarUser = findViewById(R.id.buttonCadastrarUser) as Button


        /*ação do campo, valida senha em tempo real*/
        textviewCadastrarUserSenha.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (textviewCadastrarUserSenha.text.toString().length < 6) {
                    textInputSenha.helperText = "Senha deve conter no minimo 6 caracteres*"

                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorError)
                        ?.let { textInputSenha.setHelperTextColor(it) }
                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorError)
                        ?.let { textInputSenha.setBoxStrokeColorStateList(it) }

                } else if (textviewCadastrarUserSenha.text.toString().length > 6) {
                    textInputSenha.helperText = ""
                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorOk)
                        ?.let { textInputSenha.setBoxStrokeColorStateList(it) }
                } else if (textviewCadastrarUserSenha.text.toString().length == 6) {
                    textInputSenha.helperText = ""
                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorOk)
                        ?.let { textInputSenha.setBoxStrokeColorStateList(it) }
                }
            }
        })

        /*ação do campo, valida senha em tempo real*/
        textviewValidarUserSenha.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (textviewValidarUserSenha.text.toString() != textviewCadastrarUserSenha.text.toString()) {
                    textInputValidaSenha.helperText = "A senha não corresponde"

                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorError)
                        ?.let { textInputValidaSenha.setHelperTextColor(it) }
                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorError)
                        ?.let { textInputValidaSenha.setBoxStrokeColorStateList(it) }
                } else {
                    /*SETA HELPER TEXT*/
                    textInputValidaSenha.helperText = "A senha corresponde"
                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorOk)
                        ?.let { textInputValidaSenha.setHelperTextColor(it) }
                    /*SETAR COR OK*/
                    ContextCompat.getColorStateList(this@RegistrarActivity, R.color.colorOk)
                        ?.let { textInputValidaSenha.setBoxStrokeColorStateList(it) }
                }
            }
        })


        /*ação do button cadastrar usuario*/
        buttoCadastrarUser.setOnClickListener(View.OnClickListener {
            if(textviewCadastrarUserName.text.toString().isEmpty()||
                textviewCadastrarUserEmail.text.toString().isEmpty()||
                textviewCadastrarUserSenha.text.toString().isEmpty()||
                textviewValidarUserSenha.text.toString().isEmpty()){

                Toast.makeText(this@RegistrarActivity, "Existem campos em branco, por favor preencha.", Toast.LENGTH_LONG).show()

            }else if(textviewValidarUserSenha.text.toString() != textviewCadastrarUserSenha.text.toString()){
                Toast.makeText(this@RegistrarActivity, "A senha não se coincidem com a outra.", Toast.LENGTH_LONG).show()
            }else{
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Registrando...")
                progressDialog.setMessage("Realizando o seu cadastro, por favor aguarde.")
                progressDialog.show()

                auth.createUserWithEmailAndPassword(
                    textviewCadastrarUserEmail.text.toString(),
                    textviewCadastrarUserSenha.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            val email = user?.email
                            Toast.makeText(
                                baseContext, "E-mail cadastrado >> " + email,
                                Toast.LENGTH_SHORT
                            ).show()

                            writeNewUser(
                                textviewCadastrarUserName.text.toString(),
                                textviewCadastrarUserEmail.text.toString()
                            )
                            textviewCadastrarUserName.setText("")
                            textviewCadastrarUserEmail.setText("")
                            textviewCadastrarUserSenha.setText("")
                            textviewValidarUserSenha.setText("")

                            /*chama tela Login*/
                            auth.signOut()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialog.dismiss()
                        }
                    }

            }
        })
    }

    @IgnoreExtraProperties
    data class User(val username: String? = null, val email: String? = null) {
    }

    fun writeNewUser(name: String, email: String) {
        val user = User(name, email)
        database.child("paciente").child(auth.currentUser?.uid.toString())
            .setValue(user)

    }
}