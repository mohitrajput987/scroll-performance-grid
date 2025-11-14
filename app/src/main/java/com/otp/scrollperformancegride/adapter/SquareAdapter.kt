package com.otp.scrollperformancegride.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.otp.scrollperformancegride.data.Square
import com.otp.scrollperformancegride.databinding.SquareItemBinding

class SquareAdapter(
    private val squares: List<Square>,
    private val onSquareClick: (squareIndex: Int) -> Unit
) : RecyclerView.Adapter<SquareAdapter.SquareViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquareViewHolder {
        val binding = SquareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SquareViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SquareViewHolder, position: Int) {
        holder.bind(squares[position])
    }

    override fun getItemCount(): Int = squares.size

    inner class SquareViewHolder(private val binding: SquareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onSquareClick(bindingAdapterPosition)
                }
            }
        }

        fun bind(square: Square) {
            binding.squareTextView.text = square.id.toString()
            binding.root.setBackgroundColor(square.color)
        }
    }
}
