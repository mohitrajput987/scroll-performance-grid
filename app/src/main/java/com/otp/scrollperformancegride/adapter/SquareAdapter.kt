package com.otp.scrollperformancegride.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.otp.scrollperformancegride.data.Square
import com.otp.scrollperformancegride.databinding.SquareItemBinding

class SquareAdapter(
    private val onSquareClick: (Square) -> Unit
) : ListAdapter<Square, SquareAdapter.SquareViewHolder>(SquareDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquareViewHolder {
        val binding = SquareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SquareViewHolder(binding, onSquareClick)
    }

    override fun onBindViewHolder(holder: SquareViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SquareViewHolder(
        private val binding: SquareItemBinding,
        private val onSquareClick: (Square) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(square: Square) {
            binding.root.setOnClickListener { onSquareClick(square) }
            binding.squareTextView.text = square.id.toString()
            binding.root.setBackgroundColor(square.color)
        }
    }

    private class SquareDiffCallback : DiffUtil.ItemCallback<Square>() {
        override fun areItemsTheSame(oldItem: Square, newItem: Square): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Square, newItem: Square): Boolean {
            return oldItem == newItem
        }
    }
}
