package com.hadimas.githubapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadimas.githubapp.adapter.ListSearchUserAdapter
import com.hadimas.githubapp.databinding.FragmentFollowersBinding
import com.hadimas.githubapp.viewmodel.FollowersViewModel

class FollowersFragment : Fragment(R.layout.fragment_followers) {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: ListSearchUserAdapter
    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowersBinding.bind(view)

        adapter = ListSearchUserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvListuser.setHasFixedSize(true)
            rvListuser.layoutManager = LinearLayoutManager(activity)
            rvListuser.adapter = adapter
        }
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }
}