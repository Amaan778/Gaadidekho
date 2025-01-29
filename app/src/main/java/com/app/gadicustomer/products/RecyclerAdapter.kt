package com.app.gadicustomer.products

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.gadicustomer.R
import com.bumptech.glide.Glide

class RecyclerAdapter(private val context: Context, private val list:List<ManageserviceDataClass>):RecyclerView.Adapter<RecyclerAdapter.Viewholder>() {
    class Viewholder(itemview:View) : RecyclerView.ViewHolder(itemview) {
        val coverimgs=itemview.findViewById<ImageView>(R.id.coverimg)
        val titles=itemview.findViewById<TextView>(R.id.title)
        val prices=itemview.findViewById<TextView>(R.id.price)
        val offers=itemview.findViewById<TextView>(R.id.offer)
        val btn=itemview.findViewById<Button>(R.id.bookbtn)
        val id=itemview.findViewById<TextView>(R.id.ids)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view=LayoutInflater.from(context).inflate(R.layout.service,parent,false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.titles.text=list[position].title
        holder.prices.text=list[position].price
        holder.offers.text=list[position].offer
        Glide.with(context).load(list[position].coverImage).into(holder.coverimgs)
        holder.id.text=list[position].id

        holder.btn.setOnClickListener {
            val intent=Intent(context,ProductDetails::class.java)
            intent.putExtra("id",list[position].id)
            intent.putExtra("cover",list[position].coverImage)
            context.startActivity(intent)
        }

//        holder.itemView.setOnClickListener {
//            val intent = Intent(context,DetailService::class.java)
//            intent.putExtra("id",list[position].id)
//            Log.d("idd", "onBindViewHolder: "+list[position].id)
//            context.startActivity(intent)
//        }
    }
}