package com.app.gadicustomer.notify

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.gadicustomer.R
import com.app.gadicustomer.products.ManageserviceDataClass
import com.app.gadicustomer.products.ProductDetails
import com.app.gadicustomer.products.RecyclerAdapter
import com.bumptech.glide.Glide

class NotificationRecyler(private val context: Context, private val list:List<NotificationDataClass>):
    RecyclerView.Adapter<NotificationRecyler.Viewholder>() {
    class Viewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val id=itemview.findViewById<TextView>(R.id.ids)
        val title=itemview.findViewById<TextView>(R.id.title)
        val descrip=itemview.findViewById<TextView>(R.id.description)
        val img=itemview.findViewById<ImageView>(R.id.coverimg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view= LayoutInflater.from(context).inflate(R.layout.notification,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.id.text=list[position].id
        holder.title.text=list[position].title
        holder.descrip.text=list[position].description
        Glide.with(context).load(list[position].imageUrl).into(holder.img)
    }
}