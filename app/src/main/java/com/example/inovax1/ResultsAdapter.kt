package com.example.inovax1

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class SearchResult(val title: String, val siteName: String, val link: String)

class ResultsAdapter(private val results: List<SearchResult>) :
    RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int = results.size

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.title_text)
        private val siteNameText: TextView = itemView.findViewById(R.id.site_name_text)
        private val linkText: TextView = itemView.findViewById(R.id.link_text)
        private val favoriteButton: Button = itemView.findViewById(R.id.add_to_favorites_button)

        fun bind(result: SearchResult) {
            titleText.text = result.title
            siteNameText.text = result.siteName
            linkText.text = result.link

            // Configura o clique para abrir o link no navegador
            val openLink = View.OnClickListener {
                val context = itemView.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.link))
                context.startActivity(intent)
            }

            titleText.setOnClickListener(openLink)
            linkText.setOnClickListener(openLink)

            // Configura o botão de favorito para adicionar aos favoritos no Firebase
            favoriteButton.setOnClickListener {
                addToFavorites(result, itemView.context)
            }
        }

        private fun addToFavorites(result: SearchResult, context: Context) {
            // Lógica para adicionar o item aos favoritos no Firebase
            val db = FirebaseFirestore.getInstance()
            val favorite = hashMapOf(
                "title" to result.title,
                "siteName" to result.siteName,
                "link" to result.link
            )
            db.collection("favorites")
                .add(favorite)
                .addOnSuccessListener {
                    Toast.makeText(context, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Erro ao adicionar aos favoritos", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
