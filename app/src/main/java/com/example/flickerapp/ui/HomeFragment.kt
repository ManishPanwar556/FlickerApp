package com.example.flickerapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickerapp.utils.ConnectionLiveData
import com.example.flickerapp.databinding.FragmentHomeBinding
import com.example.flickerapp.retrofit.Photo
import com.example.flickerapp.viewModel.FlickerViewModel
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {
    lateinit var connectionLiveData: ConnectionLiveData
    lateinit var viewModel: FlickerViewModel
    lateinit var rev: RecyclerView
    private var list = arrayListOf<Photo>()
    var isScrolling: Boolean = false
    var scrolledOutItems = 0
    var totalItems = 0
    var visibleItems = 0
    lateinit var adapter: MyRecyclerAdapter
    lateinit var loadingProgressBar: ProgressBar
    lateinit var binding: FragmentHomeBinding
    var count = 0
    var isnetConnected: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        loadingProgressBar = binding.loadingProgressBar
        connectionLiveData = ConnectionLiveData(requireContext())
        rev = binding.rev
        adapter = MyRecyclerAdapter(list)
//        Pagination in RecyclerView
        paginateRecyclerView()
        rev.adapter = adapter
        rev.layoutManager = LinearLayoutManager(requireContext())
        viewModel = ViewModelProvider(this).get(FlickerViewModel::class.java)
        viewModel.response.observe(viewLifecycleOwner, Observer {
            loadingProgressBar.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            list.addAll(it)
            adapter.notifyDataSetChanged()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerNetwork()
    }

    private fun paginateRecyclerView() {
        rev.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
                visibleItems = layoutManager.childCount
                totalItems = layoutManager.itemCount
                if (isScrolling && scrolledOutItems + visibleItems == totalItems && isnetConnected) {
//                    fetch data
                    loadingProgressBar.visibility = View.VISIBLE
                    viewModel.updateLiveData()
                    isScrolling = false
                } else {
                    rev.setPadding(0, 0, 0, 0)
                }
            }

        })
    }

    private fun registerNetwork() {
        if (count == 0) {

            val snackBar =
                Snackbar.make(binding.homeRootView, "No Internet", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("Retry") {
                snackBar.dismiss()
                registerNetwork()
            }
            snackBar.show()
        }
        connectionLiveData.observe(requireActivity(), Observer {
            if (it) {
                Snackbar.make(binding.homeRootView, "Internet Connected", Snackbar.LENGTH_SHORT).show()
                count = 1
                viewModel.updateLiveData()
                isnetConnected = true
            } else {
                val snackBar = Snackbar.make(
                    binding.homeRootView,
                    "No Internet Connection",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackBar.setAction("Retry") {
                    snackBar.dismiss()
                    registerNetwork()
                }
                snackBar.show()
                isnetConnected = false
                count = 1
            }
        })
    }

}