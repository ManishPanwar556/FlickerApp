package com.example.flickerapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.flickerapp.databinding.FragmentHomeBinding
import com.example.flickerapp.viewModel.FlickerViewModel


class HomeFragment : Fragment() {

    lateinit var viewModel:FlickerViewModel
    lateinit var rev:RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding=FragmentHomeBinding.inflate(inflater)
        val snapHelper=LinearSnapHelper()
        rev=binding.rev
        viewModel=ViewModelProvider(this).get(FlickerViewModel::class.java)
        viewModel.response.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility=View.GONE
            rev.adapter= MyRecyclerAdapter(it)
            rev.layoutManager=LinearLayoutManager(requireContext())
        })

        return binding.root
    }


}