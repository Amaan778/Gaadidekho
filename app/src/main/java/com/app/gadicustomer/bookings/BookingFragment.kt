package com.app.gadicustomer.bookings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gadicustomer.R
import com.app.gadicustomer.favorite.FavoriteRecycler
import com.app.gadicustomer.products.Favoritemodelclas
import com.app.gadicustomer.products.ManageserviceDataClass
import com.app.gadicustomer.products.RecyclerAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class BookingFragment : Fragment() {
    private lateinit var recycler: RecyclerView
    private var mList = ArrayList<Bookmodelclass>()
    private lateinit var adapter: BookingRecycler
    var db= Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var text: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler=view.findViewById(R.id.recyler)
        auth = Firebase.auth

        recycler.layoutManager= LinearLayoutManager(requireContext())
        adapter=BookingRecycler(requireContext(),mList)
        recycler.adapter=adapter

        mList.clear()

        val uid = auth.currentUser?.uid
        db = FirebaseFirestore.getInstance()
        db.collection(uid.toString()).addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("firestore", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        mList.add(dc.document.toObject(Bookmodelclass::class.java))
                    }
                }

                adapter?.notifyDataSetChanged()
            }
        })

    }

}