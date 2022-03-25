package com.hadimas.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hadimas.githubapp.databinding.RowUserBinding
import com.hadimas.githubapp.response.UserItems

class ListSearchUserAdapter : RecyclerView.Adapter<ListSearchUserAdapter.ListViewHolder>()  {
    private val list = ArrayList<UserItems>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    fun setList(users: ArrayList<UserItems>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }
    inner class ListViewHolder(var binding: RowUserBinding) : RecyclerView.ViewHolder(binding.root){
//        set data
        fun bind(users : UserItems){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(users)
            }
            val name = "@${users.login}"
            binding.apply {
                tvUsername.text = name
                Glide.with(itemView)
                    .load(users.avatarUrl)
                    .centerCrop()
                    .into(itemAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = RowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface  OnItemClickCallback{
         fun onItemClicked(data: UserItems)
    }
}

