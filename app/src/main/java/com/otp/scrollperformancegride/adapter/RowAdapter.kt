package com.otp.scrollperformancegride.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.otp.scrollperformancegride.data.Row
import com.otp.scrollperformancegride.data.Square
import com.otp.scrollperformancegride.databinding.RowItemBinding

class RowAdapter(
    private val onSquareClick: (Row, Square) -> Unit
) : ListAdapter<Row, RowAdapter.RowViewHolder>(RowDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowViewHolder(binding, onSquareClick)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RowViewHolder(
        binding: RowItemBinding,
        private val onSquareClick: (Row, Square) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val squareAdapter = SquareAdapter { square ->
            val row = getItem(bindingAdapterPosition)
            onSquareClick(row, square)
        }

        init {
            binding.squareRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = squareAdapter
                setRecycledViewPool(RecyclerView.RecycledViewPool())
            }
        }

        fun bind(row: Row) {
            squareAdapter.submitList(row.squares.toList())
        }
    }

    private class RowDiffCallback : DiffUtil.ItemCallback<Row>() {
        override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
            return oldItem == newItem
        }
    }
}
