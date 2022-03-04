package com.hadimas.githubapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hadimas.githubapp.databinding.RowUserBinding

class ListHeroAdapter (private val listUser: ArrayList<User>) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: RowUserBinding) : RecyclerView.ViewHolder(binding.root)
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, company, photo) = listUser[position]
        holder.binding.itemAvatar.setImageResource(photo)
        holder.binding.tvUsername.text = username
        holder.binding.tvName.text = name
        holder.binding.tvCompany.text = company

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

}