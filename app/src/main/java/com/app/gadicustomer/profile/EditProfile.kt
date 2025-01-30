package com.app.gadicustomer.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.gadicustomer.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfile : AppCompatActivity() {
    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var number: EditText
    private lateinit var email: EditText
    private lateinit var btn: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var users: ProfileData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firstname = findViewById(R.id.firstname)
        lastname = findViewById(R.id.lastname)
        number = findViewById(R.id.phonenumber)
        email = findViewById(R.id.email)
        btn = findViewById(R.id.update)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        database = FirebaseDatabase.getInstance().getReference("users")
        if (uid.isNotEmpty()) {

            database.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    users = snapshot.getValue(ProfileData::class.java)!!
                    firstname.setText(users.firstname)
                    lastname.setText(users.lastname)
                    email.setText(users.email)
                    number.setText(users.number)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EditProfile, "Error while loading personal data", Toast.LENGTH_LONG).show()
                }
            })

            btn.setOnClickListener {
                val first=firstname.text.toString().trim()
                val last=lastname.text.toString().trim()
                val num=number.text.toString().trim()
                val id=uid.toString()

                database = FirebaseDatabase.getInstance().getReference("users")

                if (uid.isNotEmpty()){

                    val user= mapOf<String,String>(
                        "firstname" to first,
                        "lastname" to last,
                        "number" to num
                    )

                    database.child(id).updateChildren(user)
                        .addOnSuccessListener {
                            Toast.makeText(this,"Data updated",Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"Data not updated",Toast.LENGTH_LONG).show()
                        }

                }
                else{
                    Toast.makeText(this,"Error in uid",Toast.LENGTH_LONG).show()
                }


            }

        }
    }
}