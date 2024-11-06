package com.dicoding.asclepius.view.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.ResultEntity
import com.dicoding.asclepius.databinding.HistoryRowBinding

class HistoryAdapter(private val onDeleteClick: (ResultEntity) -> Unit) :
    ListAdapter<ResultEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result)

        holder.itemView.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context).apply {
                setTitle("Delete History")
                setMessage("Are you sure you want to delete this item?")
                setPositiveButton("Yes") { _, _ ->
                    onDeleteClick(result) // Call the delete callback
                }
                setNegativeButton("No", null)
                create().show()
            }
        }
    }

    class MyViewHolder(private val binding: HistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultEntity) {
            val imageUri = Uri.parse(result.imageUri)
            binding.imgItemImage.setImageURI(imageUri)
            binding.resultText.text = result.resultName
            binding.percentageText.text = result.resultScore
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultEntity>() {
            override fun areItemsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ResultEntity, newItem: ResultEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}