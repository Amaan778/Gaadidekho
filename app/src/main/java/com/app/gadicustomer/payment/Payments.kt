package com.app.gadicustomer.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.gadicustomer.Dashboard
import com.app.gadicustomer.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class Payments : AppCompatActivity(), PaymentResultListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var uid:String
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        val price=intent.getStringExtra("price")
        firestore=FirebaseFirestore.getInstance()

//        Log.d("values", "onCreate: ${name + email + number + price + date + time + service} ")

        val checkout= Checkout()
        checkout.setKeyID("rzp_test_nk40BKiH4sgKUf")

        try {
            val options = JSONObject()
            options.put("name","Gadi Customer app")
            options.put("description","Car App")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#f75d12");
            options.put("currency","INR");
            options.put("amount",(price!!.toInt())*100)//pass amount in currency subunits


            val prefill = JSONObject()
            prefill.put("email","amaanansari6341@gmail.com")
            prefill.put("contact","9324503691")
            options.put("prefill",prefill)
            checkout.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    override fun onPaymentSuccess(p0: String?) {
        val name=intent.getStringExtra("name")
        val email=intent.getStringExtra("email")
        val number=intent.getStringExtra("number")
        val price=intent.getStringExtra("price")
        val date=intent.getStringExtra("date")
        val time=intent.getStringExtra("time")
        val service=intent.getStringExtra("service")
        val id=intent.getStringExtra("id")

        Toast.makeText(this,"Payment Sucess",Toast.LENGTH_LONG).show()
        fetchdata(id.toString(),name.toString(),email.toString(),number.toString(),date.toString(),time.toString(),service.toString(),id.toString())
        startActivity(Intent(this,OrdersPlaced::class.java))
        finish()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment not completed",Toast.LENGTH_LONG).show()
    }

    private fun fetchdata(productId:String, name: String, email: String, number: String, date: String, time: String, service: String,id: String){
        Firebase.firestore.collection("services").document(productId).get()
            .addOnSuccessListener {
                savedData(it.getString("title"),it.getString("offer"), it.getString("price"),it.getString("rating"),it.getString("coverImage")
                , name,email,number, date, time, service,id)

            }
            .addOnFailureListener {

            }
    }

    private fun savedData(title:String?, offer:String?, price:String?, rating:String?, coverImage: String?, name:String?,
                          email: String?, number: String?, date: String?, time: String?, service: String?,id:String?){
        val data= hashMapOf<String,Any>()
        data["id"]=id!!
        data["title"]=title!!
        data["offer"]=offer!!
        data["price"]=price!!
        data["rating"]=rating!!
        data["coverImage"]=coverImage!!
        data["name"]=name!!
        data["email"]=email!!
        data["number"]=number!!
        data["date"]=date!!
        data["time"]=time!!
        data["service"]=service!!
        data["status"]="Completed"

        firestore.collection("Orders").document(id).set(data)
            .addOnSuccessListener {

                auth= FirebaseAuth.getInstance()
                uid=auth.currentUser?.uid.toString()
                firestore.collection(uid).document().set(data)
                    .addOnSuccessListener {
                        Toast.makeText(this,"Orderd placed sucessfully",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"Orderd not placed sucessfully",Toast.LENGTH_LONG).show()
                    }

            }
            .addOnFailureListener {
                Toast.makeText(this,"Failure", Toast.LENGTH_LONG).show()
            }

    }
}