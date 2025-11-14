package com.otp.scrollperformancegride.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otp.scrollperformancegride.data.Row
import com.otp.scrollperformancegride.databinding.RowItemBinding

class RowAdapter(
    private val rows: List<Row>,
    private val onSquareClick: (rowIndex: Int, squareIndex: Int) -> Unit
) : RecyclerView.Adapter<RowAdapter.RowViewHolder>() {
    private val recycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        holder.bind(rows[position])
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val squareIndex = payloads[0] as Int
            holder.squareAdapter.notifyItemRemoved(squareIndex)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int = rows.size

    inner class RowViewHolder(private val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        lateinit var squareAdapter: SquareAdapter

        init {
            binding.squareRecyclerView.setRecycledViewPool(recycledViewPool)
            binding.squareRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        }

        fun bind(row: Row) {
            squareAdapter = SquareAdapter(row.squares) { squareIndex ->
                onSquareClick(bindingAdapterPosition, squareIndex)
            }
            binding.squareRecyclerView.adapter = squareAdapter
        }
    }
}
