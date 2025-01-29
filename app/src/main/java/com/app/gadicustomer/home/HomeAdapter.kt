package com.app.gadicustomer.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.gadicustomer.R
import com.app.gadicustomer.products.Products
import com.bumptech.glide.Glide

class HomeAdapter (private val context: Context, private var list: ArrayList<HomeModelclass>) :RecyclerView.Adapter<HomeAdapter.viewHolder>() {
    class viewHolder(itemview:View) :RecyclerView.ViewHolder(itemview) {

        val img:ImageView=itemview.findViewById(R.id.image)
        val name:TextView=itemview.findViewById(R.id.catname)
        val id:TextView=itemview.findViewById(R.id.idss)

    }

    fun setfilteredlist(list: List<HomeModelclass>){
        this.list = list as ArrayList<HomeModelclass>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.services,parent,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.name.text=list[position].name
        Glide.with(context).load(list[position].imageUrl).circleCrop().into(holder.img)

        holder.itemView.setOnClickListener {
            val intent=Intent(context,Products::class.java)
            intent.putExtra("id",list[position].name)
            context.startActivity(intent)
        }

    }
}