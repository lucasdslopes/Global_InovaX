package com.example.inovax1

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private var botaoTeste: Button? = null
    private lateinit var bancoFb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialização do Firebase
        try {
            FirebaseApp.initializeApp(this)
            bancoFb = FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            System.out.println("Erro ao inicializar o Firebase: ${e.message}")
        }

        botaoTeste = findViewById(R.id.ID_testabutton)
        botaoTeste?.setOnClickListener {
            val user = hashMapOf(
                "nome" to "Joao",
                "idade" to 58,
                "cidade" to "São Paulo"
            )
            bancoFb.collection("users").add(user)
                .addOnSuccessListener { documentReference ->
                    System.out.println("Usuario adicionado com ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    System.out.println("Erro ao adicionar usuario: $e")
                }
        }
    }
}
