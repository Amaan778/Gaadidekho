package com.app.gadicustomer.shareswithsomefrag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.app.gadicustomer.R
import com.google.firebase.firestore.FirebaseFirestore

class ContactFragment : Fragment() {
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var msg:EditText
    private lateinit var btn:Button
    private lateinit var db:FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name=view.findViewById(R.id.name)
        email=view.findViewById(R.id.email)
        msg=view.findViewById(R.id.msg)
        btn=view.findViewById(R.id.btn)

        btn.setOnClickListener {

            val nam=name.text.toString()
            val ema=email.text.toString()
            val msgd=msg.text.toString()

            db=FirebaseFirestore.getInstance()

            val data= mapOf<String,String>(
                "name" to nam,
                "email" to ema,
                "message" to msgd
            )

            db.collection("message").document().set(data)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Message sent successfully",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"Message not sended",Toast.LENGTH_LONG).show()
                }

        }


    }

}