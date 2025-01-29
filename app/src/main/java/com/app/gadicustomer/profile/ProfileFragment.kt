package com.app.gadicustomer.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.app.gadicustomer.R
import com.app.gadicustomer.about.AboutFragment
import com.app.gadicustomer.bookings.BookingFragment
import com.app.gadicustomer.shareswithsomefrag.ContactFragment
import com.app.gadicustomer.shareswithsomefrag.PrivacyFragment
import com.app.gadicustomer.shareswithsomefrag.ShareFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ProfileFragment : Fragment() {
    private lateinit var name:TextView
    private lateinit var number : TextView
    private lateinit var email:TextView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var uid:String
    private lateinit var users:ProfileData
    private lateinit var editprofile:LinearLayout
    private lateinit var bookinglinear:LinearLayout
    private lateinit var shareapp:LinearLayout
    private lateinit var privacypolicy:LinearLayout
    private lateinit var contactus:LinearLayout
    private lateinit var aboutus:LinearLayout
    private lateinit var fragmentcontainer:LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name=view.findViewById(R.id.name)
        number=view.findViewById(R.id.number)
        email=view.findViewById(R.id.email)
        editprofile=view.findViewById(R.id.editprofile)
        bookinglinear=view.findViewById(R.id.bookinglinear)
        shareapp=view.findViewById(R.id.shareapp)
        privacypolicy=view.findViewById(R.id.privacypolicy)
        contactus=view.findViewById(R.id.contactus)
        aboutus=view.findViewById(R.id.aboutus)
        fragmentcontainer=view.findViewById(R.id.fragmentcontainer)

        auth=FirebaseAuth.getInstance()
        uid=auth.currentUser?.uid.toString()

        database=FirebaseDatabase.getInstance().getReference("users")
        if (uid.isNotEmpty()){

            database.child(uid).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    users=snapshot.getValue(ProfileData::class.java)!!
                    name.setText(users.firstname)
                    email.setText(users.email)
                    number.setText(users.number)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),"Error while loading personal data",Toast.LENGTH_LONG).show()
                }
            })

            editprofile.setOnClickListener{
                startActivity(Intent(requireContext(),EditProfile::class.java))
            }

            bookinglinear.setOnClickListener {
                startActivity(Intent(requireContext(),BookingFragment::class.java))
            }

            shareapp.setOnClickListener {
                startActivity(Intent(requireContext(),ShareFragment::class.java))
            }

            privacypolicy.setOnClickListener {
                startActivity(Intent(requireContext(),PrivacyFragment::class.java))
            }

            contactus.setOnClickListener {
                startActivity(Intent(requireContext(),ContactFragment::class.java))
            }

            aboutus.setOnClickListener {
                val fragmentB = AboutFragment()
                val bundle = Bundle()
                bundle.putString("key", "Hello from Fragment A")
                fragmentB.arguments = bundle

// Navigate to Fragment B (using FragmentTransaction or Navigation)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentcontainer, fragmentB)
                    .addToBackStack(null)
                    .commit()
            }

        }


    }
}