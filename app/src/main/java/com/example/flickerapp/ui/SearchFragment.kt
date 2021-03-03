package com.example.flickerapp.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickerapp.utils.ConnectionLiveData
import com.example.flickerapp.rxjava.RxSearchObservable
import com.example.flickerapp.databinding.FragmentSearchBinding
import com.example.flickerapp.viewModel.FlickerViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {
    private lateinit var viewModel: FlickerViewModel
    private lateinit var binding: FragmentSearchBinding
    private lateinit var connectionLiveData: ConnectionLiveData
    var count = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        connectionLiveData = ConnectionLiveData(requireContext())
        setUpRecyclerView()
        viewModel = ViewModelProvider(requireActivity()).get(FlickerViewModel::class.java)
        viewModel.searchResponse.observe(requireActivity(), Observer {
            binding.searchRecyclerView.adapter = MyRecyclerAdapter(it)
        })
        unsubscribe()
        return binding.root
    }

    private fun unsubscribe() {
        viewModel.networkStatus.observe(viewLifecycleOwner, Observer {
            setRxJavaObservable(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerNetwork()
    }

    private fun setRxJavaObservable(status: Boolean) {
        setUpRecyclerView()
        val res = RxSearchObservable.fromView(binding.searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter(object : io.reactivex.rxjava3.functions.Predicate<String> {
                override fun test(t: String?): Boolean {
                    if (t == null) {
                        return false
                    } else if (t.isEmpty()) {
                        return false
                    }
                    return true

                }

            }).distinctUntilChanged()
            .switchMap { t ->
                viewModel.getSearchFromNetwork(t!!).doOnError {
                    Log.e("Error", "$it")
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                viewModel.updateSearchResponse(it)
            }
        if (!status) {
            res.dispose()
        }

    }

    private fun setUpRecyclerView() {
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun registerNetwork() {
        if (count == 0) {
            val snackBar =
                Snackbar.make(binding.rootView, "No Internet", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("Retry") {
                snackBar.dismiss()
                registerNetwork()
            }
            snackBar.show()
        }
        connectionLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.rootView, "Internet Connected", Snackbar.LENGTH_SHORT).show()
                viewModel.updateNetworkStatus(true)
                Log.e("Search", "true")
                count = 1
            } else {
                viewModel.updateNetworkStatus(false)
                val snackBar = Snackbar.make(
                    binding.rootView,
                    "No Internet Connection",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackBar.setAction("Retry") {
                    snackBar.dismiss()
                    registerNetwork()
                }
                snackBar.show()

                count = 1
            }
        })
    }
}




