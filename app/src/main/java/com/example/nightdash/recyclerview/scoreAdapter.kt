package com.example.nightdash.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nightdash.R
import com.example.nightdash.databinding.ItemScoreBinding
import com.example.nightdash.retrofitcoil.scoreDataClass

class scoreAdapter(val scores: List<scoreDataClass>): RecyclerView.Adapter<scoreAdapter.scoreViewHolder>() {

    inner class scoreViewHolder(val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): scoreViewHolder {
        return scoreViewHolder(ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
       return scores.size
    }

    override fun onBindViewHolder(holder: scoreViewHolder, position: Int) {
        holder.binding.apply{
            scoreName.text = scores.get(position).name
            scoreScore.text = scores.get(position).score.toString()
            rank.text = (position+1).toString()
        }
    }

}

