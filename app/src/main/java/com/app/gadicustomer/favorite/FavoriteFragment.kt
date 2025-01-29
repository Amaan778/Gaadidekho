package com.app.gadicustomer.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.gadicustomer.R
import com.app.gadicustomer.home.HomeAdapter
import com.app.gadicustomer.home.HomeModelclass
import com.app.gadicustomer.products.Favoritemodelclas
import com.app.gadicustomer.products.ManageserviceDataClass
import com.app.gadicustomer.products.RecyclerAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore

class FavoriteFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<Favoritemodelclas>()
    private lateinit var readpter: FavoriteRecycler
    private lateinit var databaseReference: DatabaseReference
    val db= Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var text:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text=view.findViewById(R.id.texting)
        recyclerView=view.findViewById(R.id.recyler)
        auth = Firebase.auth
        //        recyclerview
        recyclerView.layoutManager= LinearLayoutManager(requireContext())
        mList=ArrayList()

        readpter= FavoriteRecycler(requireContext(),mList)
        recyclerView.adapter=readpter

        databaseReference= FirebaseDatabase.getInstance().getReference("Favorites")

        val uid = auth.currentUser?.uid
        databaseReference.child(uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()

                if (mList.isEmpty()){
                    text.visibility=View.VISIBLE
                }

                for (usersnapshot in snapshot.children){
                    val user= usersnapshot.getValue(Favoritemodelclas::class.java)
                    if (user!=null){
                        mList.add(user)
                    }
                }

                readpter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Error", Toast.LENGTH_LONG).show()
            }

        })

    }


}