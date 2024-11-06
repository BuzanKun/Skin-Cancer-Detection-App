package com.dicoding.asclepius.view.news

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.google.android.material.snackbar.Snackbar

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding

    private val factory: NewsViewModelFactory = NewsViewModelFactory.getInstance()
    private val viewModel: NewsViewModel by viewModels {
        factory
    }
    private val newsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayData()
    }

    override fun onResume() {
        super.onResume()
        displayData()
    }

    private fun displayData() {
        viewModel.getNews().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        Log.d("LOADING", "Loading")
                        binding?.progressBar?.visibility = View.VISIBLE
                        var progress = 0
                        val handler = Handler(Looper.getMainLooper())
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                progress += 10
                                if (progress <= 100) {
                                    binding?.progressBar?.progress = progress
                                    handler.postDelayed(this, 100) // Update every 300ms
                                }
                            }
                        }, 100)
                    }

                    is Result.Success -> {
                        Log.d("SUCCESS", "Success")
                        binding?.progressBar?.visibility = View.GONE
                        val newsData = result.data
                        newsAdapter.submitList(newsData)
                        binding!!.rvNewsList.apply {
                            layoutManager = LinearLayoutManager(context)
                            setHasFixedSize(true)
                            adapter = newsAdapter
                        }
                    }

                    is Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        view?.let {
                            Snackbar.make(
                                requireActivity(),
                                it,
                                "Error Occurred: ${result.error}",
                                Snackbar.LENGTH_SHORT
                            ).setAction("Dismiss") {
                            }.show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}