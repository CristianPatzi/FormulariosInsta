package com.example.formularios

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import com.example.formularios.databinding.ActivityContactsBinding

lateinit var bindingContacts: ActivityContactsBinding
class ContactsActivity : AppCompatActivity() {
    var contador = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contacts)
        bindingContacts = DataBindingUtil.setContentView(this, R.layout.activity_contacts)

        bindingContacts.back.setOnClickListener {
            finish()
        }
        bindingContacts.subirContacts.setOnClickListener {
            //subir todos los contactos
        }
    }

    override fun onResume() {
        super.onResume()
        for (c in DataHolder.contactos) {
            Log.d("CONTACTO", "Nombre: ${c.nombre}, Tel: ${c.telefono}")
            contador=contador+1
        }
        bindingContacts.TotalTextView.text = "Total Contactos: $contador"
    }
}