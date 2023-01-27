package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MinhasConsultasActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var layout_vertical: LinearLayout? = null
    var card: CardView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minhas_consultas)

        //Initialize Firebase DATABASE
        database = Firebase.database.reference

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Chamar tela anterior icone back
        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_minhas_consultas)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        layout_vertical = findViewById(R.id.coluna_vertical)
        database.child("paciente").child(auth.currentUser?.uid!!).child("agendamentos")
            .addListenerForSingleValueEvent(object : ValueEventListener {

                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val textview = TextView(this@MinhasConsultasActivity)
                            textview.setTextSize(1, 16F)
                            textview.setText(
                                "\n"+"  " +"Data :"+ i.child("data").value.toString() + "\n" + "  " +"Especialidade :"+ i.child(
                                    "especialidade"
                                ).value.toString() + "\n" + "  " +"Médico(a) :"+ i.child("medico").value.toString() + "\n"+"\n"+
                                        " --------------------------------------------------------------------------------------"
                            )
                            layout_vertical?.addView(textview)
                            /*System.out.println(i.child("data").value)
                            System.out.println(i.child("especialidade").value)
                            System.out.println(i.child("medico").value)*/

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }


}