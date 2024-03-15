package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data_class.CurrentDataClass

class CurrentAdapterClass(
    private val dataList: ArrayList<CurrentDataClass>,
) : RecyclerView.Adapter<CurrentAdapterClass.ViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.frame_view, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvIcon.setImageResource(currentItem.dataIcon)
        holder.rvNumber.text = currentItem.dataNumber
        holder.rvSituation.text = currentItem.dataSituation
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvIcon: ImageView = itemView.findViewById(R.id.icon)
        val rvNumber: TextView = itemView.findViewById(R.id.number)
        val rvSituation: TextView = itemView.findViewById(R.id.situation)
    }
}






