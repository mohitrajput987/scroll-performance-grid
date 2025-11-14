package com.otp.scrollperformancegride.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.otp.scrollperformancegride.viewmodel.MainViewModel
import com.otp.scrollperformancegride.adapter.RowAdapter
import com.otp.scrollperformancegride.databinding.ActivityMainBinding
import com.otp.scrollperformancegride.util.EventObserver

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var rowAdapter: RowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        rowAdapter = RowAdapter(viewModel.rows) { rowIndex, squareIndex ->
            viewModel.removeSquare(rowIndex, squareIndex)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = rowAdapter
            setHasFixedSize(true)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeViewModel() {
        viewModel.onDataGenerated.observe(this, EventObserver { 
            rowAdapter.notifyDataSetChanged()
        })
        viewModel.onRowRemoved.observe(this, EventObserver { rowIndex ->
            rowAdapter.notifyItemRemoved(rowIndex)
        })
        viewModel.onSquareRemoved.observe(this, EventObserver { (rowIndex, squareIndex) ->
            rowAdapter.notifyItemChanged(rowIndex, squareIndex)
        })
    }
}