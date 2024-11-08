package com.example.inovax1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class FavoritosActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: ResultsAdapter
    private val favoritesList = mutableListOf<SearchResult>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favoritos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        favoritesRecyclerView = findViewById(R.id.favorites_recycler_view)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        favoritesAdapter = ResultsAdapter(favoritesList)
        favoritesRecyclerView.adapter = favoritesAdapter

        loadFavorites()
    }

    private fun loadFavorites() {
        db.collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                favoritesList.clear()
                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val siteName = document.getString("siteName") ?: ""
                    val link = document.getString("link") ?: ""

                    favoritesList.add(SearchResult(title, siteName, link))
                }
                favoritesAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao carregar favoritos", Toast.LENGTH_SHORT).show()
            }
    }
}
