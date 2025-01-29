package com.app.gadicustomer.products

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.gadicustomer.R
import com.app.gadicustomer.booking.BookingActvity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.w3c.dom.Text

class ProductDetails : AppCompatActivity() {
    private lateinit var title:TextView
    private lateinit var ratings:TextView
    private lateinit var save:ImageView
    private lateinit var share:ImageView
    private lateinit var offer:TextView
    private lateinit var pricing:TextView
    private lateinit var description:TextView
    private lateinit var btn:Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        title=findViewById(R.id.title)
        ratings=findViewById(R.id.ratings)
        save=findViewById(R.id.save)
        share=findViewById(R.id.sahre)
        description=findViewById(R.id.description)
        offer=findViewById(R.id.offer)
        pricing=findViewById(R.id.price)
        btn=findViewById(R.id.service)
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)

        auth = Firebase.auth
        database = Firebase.database.reference

        val uid=auth.currentUser?.uid
        val int=intent.getStringExtra("id")
        val strint=int.toString()
        val coverimgg=intent.getStringExtra("cover")
        val cImg=coverimgg.toString()
        Log.d("check", "onCreate: "+int.toString())

        database=FirebaseDatabase.getInstance().getReference("Favorites")
        database.child(uid.toString()).child(strint).get()
            .addOnSuccessListener {
                if (it.exists()){
//                    Toast.makeText(this,"Product is exist",Toast.LENGTH_LONG).show()
                    save.setImageResource(R.drawable.fullheart)
                }
                else{
//                    Toast.makeText(this,"Product is not exist",Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failure",Toast.LENGTH_LONG).show()
            }

        save.setOnClickListener {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                database = FirebaseDatabase.getInstance().getReference("Favorites")
                val productname = title.text.toString()
                val descript = description.text.toString()
                val pricie = offer.text.toString()
                val rting=ratings.text.toString()
                val off=offer.text.toString()
                val img=cImg
                val productId = strint // Assuming `strint` is the product's unique ID

                val productRef = database.child(uid).child(productId)

                // Check if the product already exists
                productRef.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snapshot = task.result
                        if (snapshot.exists()) {
                            // Product already exists, so delete it
                            productRef.removeValue().addOnSuccessListener {
                                Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                                save.setImageResource(R.drawable.heart)
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed to remove", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Product doesn't exist, so add it
                            val data = Favoritemodelclas(productId,productname,descript,rting,pricie,off,cImg)
                            productRef.setValue(data).addOnSuccessListener {
                                Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
                                save.setImageResource(R.drawable.fullheart)
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Failed to check data", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }


//        save.setOnClickListener {
//            val uid=auth.currentUser?.uid
//            database=FirebaseDatabase.getInstance().getReference("Favorites")
//            val productname=title.text.toString()
//            val descript=description.text.toString()
//            val pricie=offer.text.toString()
//
//            val data=Favoritemodelclas(strint, productname,descript,pricie)
//
//            database.child(uid.toString()).child(strint).setValue(data)
//
//        }

        val db=FirebaseFirestore.getInstance()
        db.collection("services").document(strint).get()
            .addOnSuccessListener {
                if (it!=null){
                    val tilt = it.data?.get("title").toString()
                    val desc = it.data?.get("description").toString()
                    val rat = it.data?.get("rating").toString()
                    val pric = it.data?.get("price").toString()
                    val offe = it.data?.get("offer").toString()
                    val img1 = it.data?.get("image1").toString()
                    val img2=it.data?.get("image2").toString()
                    val img3=it.data?.get("image3").toString()

                    val imageList = ArrayList<SlideModel>() // Create image list

                    imageList.add(SlideModel(img1, ScaleTypes.FIT))
                    imageList.add(SlideModel(img2, ScaleTypes.FIT))
                    imageList.add(SlideModel(img3, ScaleTypes.FIT))
                    imageSlider.setImageList(imageList)

                    title.setText(tilt)
                    description.setText(desc)
                    ratings.setText(rat)
                    pricing.setText(pric)
                    offer.setText(offe)

                }

            }
            .addOnFailureListener {
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }

        btn.setOnClickListener {
            val titles=title.text.toString()
            val offers=offer.text.toString()
            val id=strint.toString()

            val intent=Intent(this,BookingActvity::class.java)
            intent.putExtra("servicename",titles)
            intent.putExtra("offer",offers)
            intent.putExtra("id",id)
            startActivity(intent)
        }


    }
}