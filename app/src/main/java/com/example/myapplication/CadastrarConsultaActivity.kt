package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class CadastrarConsultaActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var editTextData: TextInputEditText? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_consulta)


        //Initialize Firebase DATABASE
        database = Firebase.database.reference

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Chamar tela anterior icone back
        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_agendar_consulta)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val textNomeUser = findViewById<TextView>(R.id.textViewNomeUser)
        val textEmailUser = findViewById<TextView>(R.id.textViewEmail)
        val listaEspecialidades = findViewById<Spinner>(R.id.spinnerEspecialidades)
        val listaMedicos = findViewById<Spinner>(R.id.spinnerMedicos)
        editTextData = findViewById(R.id.EditTextData)
        val buttonSelecionaData = findViewById<Button>(R.id.buttonSelecionarData)
        val buttonMarcarConsulta = findViewById<Button>(R.id.buttonMarcar)


        /*seta usuario logado no textview*/
        database.child("paciente").child(auth.currentUser?.uid.toString()).child("username").get()
            .addOnSuccessListener {
                val usuario = "${it.value}"
                textNomeUser.text = usuario
            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao obter dados " + it, Toast.LENGTH_SHORT).show()
            }

        /*seta email logado no textview*/
        database.child("paciente").child(auth.currentUser?.uid.toString()).child("email").get()
            .addOnSuccessListener {
                val usuarioemail = "${it.value}"
                textEmailUser.text = usuarioemail
            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao obter dados " + it, Toast.LENGTH_SHORT).show()
            }

        /*CARREGA LISTA DE ESPECIALIDADES*/
        val adapter = ArrayAdapter.createFromResource(
            this@CadastrarConsultaActivity,
            R.array.especialidades,
            androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
        listaEspecialidades.setAdapter(adapter)
        /*AÇÃO LISTA DE ESPECIALIDADES*/
        listaEspecialidades.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (listaEspecialidades.selectedItem.toString() == "Cardiologia") {
                    /*CARREGA LISTA DE MEDICOS CONFORME ESPECIALIDADE*/
                    val adapterMedicos = ArrayAdapter.createFromResource(
                        this@CadastrarConsultaActivity,
                        R.array.medicosCardiologia,
                        androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item
                    )
                    adapterMedicos.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
                    listaMedicos.setAdapter(adapterMedicos)
                } else if (listaEspecialidades.selectedItem.toString() == "Dermatologia") {
                    /*CARREGA LISTA DE MEDICOS CONFORME ESPECIALIDADE*/
                    val adapterMedicos = ArrayAdapter.createFromResource(
                        this@CadastrarConsultaActivity,
                        R.array.medicosDermatologia,
                        androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item
                    )
                    adapterMedicos.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
                    listaMedicos.setAdapter(adapterMedicos)
                } else if (listaEspecialidades.selectedItem.toString() == "Ginecologia e Obstetrícia") {
                    /*CARREGA LISTA DE MEDICOS CONFORME ESPECIALIDADE*/
                    val adapterMedicos = ArrayAdapter.createFromResource(
                        this@CadastrarConsultaActivity,
                        R.array.medicosGinecologiaEObstetricia,
                        androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item
                    )
                    adapterMedicos.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
                    listaMedicos.setAdapter(adapterMedicos)
                } else {
                    listaMedicos.setSelection(0)
                    Toast.makeText(this@CadastrarConsultaActivity,"Ainda não existe médicos disponiveis para essa especialidade.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        //--------------------------------------------------------------------------------------------
        /*AÇÃO DO BOTÃO DE SELECIONAR DATA*/
        buttonSelecionaData.setOnClickListener(View.OnClickListener {
            showDatePickerDailog()
        })
        // AÇÃO DO BOTÃO MARCAR CONSULTA
        buttonMarcarConsulta.setOnClickListener(View.OnClickListener {
            if(listaEspecialidades.selectedItem.toString()=="Nenhuma especialidade selecionada"){
                Toast.makeText(this@CadastrarConsultaActivity, "Você precisar selecionar um especialidade.", Toast.LENGTH_SHORT).show()
            }else if (editTextData?.text.toString().isEmpty()){
                Toast.makeText(this@CadastrarConsultaActivity, "Você precisar escolher uma data para finalizar seu agendamento.", Toast.LENGTH_SHORT).show()

            }else{
                writeNewUser(
                    auth.currentUser?.uid.toString(),
                    listaEspecialidades.selectedItem.toString(),
                    listaMedicos.selectedItem.toString(),
                    editTextData?.text.toString()
                )
                Toast.makeText(this@CadastrarConsultaActivity, "Seu agendamento foi registrado com sucesso.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showDatePickerDailog() {
        val datePickerDialog = DatePickerDialog(
            this, this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        var mes = month + 1
        if (dayOfMonth < 10) {
            val date = "0$dayOfMonth/0$mes/$year"
            editTextData?.setText(date)
        } else if (mes < 10) {
            val date = "$dayOfMonth/0$mes/$year"
            editTextData?.setText(date)
        } else {
            val date = "$dayOfMonth/$mes/$year"
            editTextData?.setText(date)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @IgnoreExtraProperties
    data class UserAgendamento(
        val uid: String? = null,
        val especialidade: String? = null,
        val medico: String? = null,
        val data: String? = null
    )

    fun writeNewUser(uid: String,especialidade: String, medico: String, data: String) {
        val userAgendamento = UserAgendamento(uid,especialidade, medico, data)
        auth = FirebaseAuth.getInstance()
        database.child("paciente").child(auth.currentUser?.uid.toString()).child("agendamentos").push().setValue(userAgendamento)

    }

}