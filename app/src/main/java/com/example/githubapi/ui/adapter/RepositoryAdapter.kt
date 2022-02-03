package com.example.githubapi.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.R
import com.example.githubapi.databinding.ItemSearchRepositoryBinding
import com.example.githubapi.ui.model.RepoItem

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryHolder>() {

    private var items : MutableList<RepoItem> = mutableListOf()
    private val placeholder = ColorDrawable(Color.GRAY)

    var onItemClick : ((repoItem : RepoItem) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoryAdapter.RepositoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_repository, parent, false)

        return RepositoryHolder(ItemSearchRepositoryBinding.bind(view))
    }

    inner class RepositoryHolder(val binding: ItemSearchRepositoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        items[position].let { repo ->
            with(holder.itemView) {
                Glide.with(context)
                    .load(repo.owner.ownerUrl)
                    .placeholder(placeholder)
                    .into(holder.binding.ivProfile)

                holder.binding.title.text = repo.title
                holder.binding.language.text = if(TextUtils.isEmpty(repo.language))
                    context.getText(R.string.no_language_specified)
                else
                    repo.language
            }
        }
    }

    override fun getItemCount() = items.size

    fun setItems(items : List<RepoItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

}