package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.google.android.material.snackbar.Snackbar

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: HistoryViewModelFactory =
            HistoryViewModelFactory.getInstance(requireActivity())
        val viewModel: HistoryViewModel by viewModels {
            factory
        }

        val historyAdapter = HistoryAdapter { result ->
            viewModel.deleteResult(result)
        }

        viewModel.getAllResult().observe(viewLifecycleOwner) { result ->
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
                        val resultData = result.data
                        historyAdapter.submitList(resultData)
                        binding!!.rvNewsList.apply {
                            layoutManager = GridLayoutManager(context, 2)
                            setHasFixedSize(true)
                            adapter = historyAdapter
                        }
                    }

                    is Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        view.let {
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
}