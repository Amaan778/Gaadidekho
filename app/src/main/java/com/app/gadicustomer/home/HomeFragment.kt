package com.app.gadicustomer.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.app.gadicustomer.R
import com.app.gadicustomer.notify.Notification
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<HomeModelclass>()
    private lateinit var readpter:HomeAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var searchss: SearchView
    private lateinit var notify:ImageView

    val db= Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView=view.findViewById(R.id.recyler)
        searchss = view.findViewById(R.id.amaan)
        notify=view.findViewById(R.id.notify)

        //        imageslider
        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)

        db.collection("imageslider").document("slider").get()
            .addOnSuccessListener {
                val cover=it.data?.get("coverImage").toString()
                val img1=it.data?.get("image1").toString()
                val img2=it.data?.get("image2").toString()
                val img3=it.data?.get("image3").toString()

                val imageList = ArrayList<SlideModel>() // Create image list

                imageList.add(SlideModel(img1, ScaleTypes.FIT))
                imageList.add(SlideModel(img2, ScaleTypes.FIT))
                imageList.add(SlideModel(img3, ScaleTypes.FIT))
                imageSlider.setImageList(imageList)

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Error", Toast.LENGTH_LONG).show()
            }

//        recyclerview
        recyclerView.layoutManager=GridLayoutManager(requireContext(),3)
        mList=ArrayList()

        readpter= HomeAdapter(requireContext(),mList)
        recyclerView.adapter=readpter

        databaseReference=FirebaseDatabase.getInstance().getReference("categories")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()

                for (usersnapshot in snapshot.children){
                    val user= usersnapshot.getValue(HomeModelclass::class.java)
                    if (user!=null){
                        mList.add(user)
                    }
                }

                readpter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
            }

        })

        // SearchView setup
        searchss.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterlist(newText)
                return true
            }
        })

//        notification
        notify.setOnClickListener {
            startActivity(Intent(requireContext(),Notification::class.java))
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })

    }

    private fun filterlist(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<HomeModelclass>()
            for (item in mList) {
                if (item.name?.toLowerCase(Locale.ROOT)!!.contains(query)) {
                    filteredList.add(item)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(requireActivity(), "No Data Found", Toast.LENGTH_LONG).show()
            } else {
                readpter.setfilteredlist(filteredList)
            }
        }
    }

    private fun showExitConfirmationDialog() {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialogbox, null)

        // Create AlertDialog and set the custom view
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false) // Prevent dismissal on outside touch
            .create()

//        / Remove the default dialog background
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Access views in the custom layout
        val btnNo = dialogView.findViewById<TextView>(R.id.btnCancel)
        val btnYes = dialogView.findViewById<Button>(R.id.btnDelete)


        // Handle button clicks
        btnYes.setOnClickListener {
            requireActivity().finish() // Exit the app
            alertDialog.dismiss()
        }
        btnNo.setOnClickListener {
            alertDialog.dismiss() // Close dialog
        }

        // Show the dialog
        alertDialog.show()
    }

}