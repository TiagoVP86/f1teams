package com.example.formula1.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.formula1.R
import com.example.formula1.databinding.ItemDriverBinding
import com.example.formula1.domain.model.Driver

class DriversAdapter : ListAdapter<Driver, DriversAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemDriverBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(private val binding: ItemDriverBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(d: Driver) {
            val context = binding.root.context
            binding.number.text = d.number?.toString() ?: "—"
            binding.driverName.text = d.fullName
            binding.driverMeta.text = d.age?.let { age ->
                context.getString(R.string.driver_meta, d.nationality, age, d.position)
            } ?: "${d.nationality} • — • P${d.position}"
            binding.points.text = context.getString(R.string.driver_points, d.points)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Driver>() {
            override fun areItemsTheSame(a: Driver, b: Driver) = a.driverId == b.driverId
            override fun areContentsTheSame(a: Driver, b: Driver) = a == b
        }
    }
}
