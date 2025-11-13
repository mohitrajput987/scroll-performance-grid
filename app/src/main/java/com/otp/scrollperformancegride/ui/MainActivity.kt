package com.otp.scrollperformancegride.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.otp.scrollperformancegride.viewmodel.MainViewModel
import com.otp.scrollperformancegride.adapter.RowAdapter
import com.otp.scrollperformancegride.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var rowAdapter: RowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeRows()
    }

    private fun observeRows() {
        viewModel.rows.observe(this) { rows ->
            rowAdapter.submitList(rows.toList())
        }
    }

    private fun setupRecyclerView() {
        rowAdapter = RowAdapter { row, square ->
            viewModel.removeSquare(row, square)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = rowAdapter
            // Performance optimizations
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }
}