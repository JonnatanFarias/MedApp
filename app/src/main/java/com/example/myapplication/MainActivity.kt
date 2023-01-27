package com.example.myapplication

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()  , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawer:DrawerLayout
    private lateinit var toogle:ActionBarDrawerToggle

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize DATABASE
        database = Firebase.database.reference

        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)


        drawer = findViewById(R.id.drawer_layout)
        toogle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val navigationView:NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val cardMarcarConsulta = findViewById<CardView>(R.id.cardViewMarcarConsulta)
        val cardMinhasConsultas = findViewById<CardView>(R.id.cardViewMinhasConsultas)



        /*------------------------------------------*/

        /*ABRE ACTIVITY CADASTRAR CONSULTAS*/
        cardMarcarConsulta.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CadastrarConsultaActivity::class.java)
            startActivity(intent)
        })

        /*ABRE ACTIVITY MINHAS CONSULTAS*/
        cardMinhasConsultas.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MinhasConsultasActivity::class.java)
            startActivity(intent)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.nav_item_logout-> {
               auth.signOut()
               Log.d(ContentValues.TAG, auth.signOut().toString())
               val intent = Intent(this, LoginActivity::class.java)
               startActivity(intent)
               finish()
           }
           R.id.nav_item_sincronizarUser-> {

               val progressDialog = ProgressDialog(this)
               progressDialog.setTitle("Sincronizando...")
               progressDialog.setMessage("Realizando a sincronização de usuário.")
               progressDialog.show()

                   /*seta usuario logado no textview*/
                   val textuser = findViewById<TextView>(R.id.textViewNomeUserInicio)
                   val textuserEmail = findViewById<TextView>(R.id.textViewNomeUserEmailInicio)
                   database.child("paciente").child(auth.currentUser?.uid.toString()).child("username")
                       .get()
                       .addOnSuccessListener {
                           val usuario = "${it.value}"
                           textuser.text = "Usuário: "+usuario
                           textuserEmail.text ="E-mail: "+auth.currentUser?.email
                           progressDialog.dismiss()
                           Toast.makeText(this, "usuário sincronizado com sucesso!.", Toast.LENGTH_SHORT).show()
                       }.addOnFailureListener {
                           Toast.makeText(this, "Erro ao obter dados " + it, Toast.LENGTH_SHORT).show()
                }
           }

           R.id.nav_item_agendarConsulta-> {
               val intent = Intent(this, CadastrarConsultaActivity::class.java)
               startActivity(intent)
           }
           R.id.nav_item_meusAgendamentos-> {
               val intent = Intent(this, MinhasConsultasActivity::class.java)
               startActivity(intent)
           }

           R.id.nav_item_sobre-> {
               val builder = AlertDialog.Builder(this)
               builder.setCancelable(false)
               builder.setIcon(R.drawable.logtemp)
               builder.setTitle("Sobre")
               builder.setMessage("Este app é de total aprendizagem acadêmica,"
                       +" pois nós queríamos testar nossos conhecimentos"
                       +"em programação web , programação mobile e banco de dados cloud."+"\n"+"\n"
                       +"O aplicativo é destinado para clinicas médicas onde o paciência poderá"
                       +"fazer os seus agendamentos de consultas como também visualizar as consultas"
                       +"de acordo com as especialidades listadas no app. "+"\n"
                       +"Além do app também densenvolvemos uma versão web onde o usuário também pode"
                       +"optar por fazer seus agendamentos com uma melhor visão do painel da clinica."+"\n"+"\n"
                       +"Desenvolvedores"+"\n"+"\n"
                       +"Dev: Jonnatan Farias"+"\n"
                       +"Dev: Klisman Mateus"+"\n"
                       +"Dev: Marcos Paulo")
               builder.setPositiveButton("TUDO BEM") { dialog, which ->
                   Toast.makeText(applicationContext,
                       "Obrigado por usar nosso App.", Toast.LENGTH_SHORT).show()
               }
               builder.show()
           }
       }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /*val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)*/
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)

    }

}