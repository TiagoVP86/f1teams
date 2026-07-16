package com.example.formula1.ui.teams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.formula1.R
import com.example.formula1.databinding.ItemTeamBinding
import com.example.formula1.domain.model.Team

class TeamsAdapter(
    private val onClick: (Team) -> Unit,
    private val onFavorite: (Team) -> Unit
) : ListAdapter<Team, TeamsAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTeamBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val binding: ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(team: Team) {
            binding.teamName.text = team.teamName
            binding.teamNationality.text = team.teamNationality
            binding.favorite.setImageResource(
                if (team.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            binding.root.setOnClickListener { onClick(team) }
            binding.favorite.setOnClickListener { onFavorite(team) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Team>() {
            override fun areItemsTheSame(a: Team, b: Team) = a.teamId == b.teamId
            override fun areContentsTheSame(a: Team, b: Team) = a == b
        }
    }
}
