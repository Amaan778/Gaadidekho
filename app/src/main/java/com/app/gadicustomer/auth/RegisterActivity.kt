package com.app.gadicustomer.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.gadicustomer.Dashboard
import com.app.gadicustomer.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database

class RegisterActivity : AppCompatActivity() {
    private lateinit var firstname:EditText
    private lateinit var lastname:EditText
    private lateinit var emil:EditText
    private lateinit var number:EditText
    private lateinit var password:EditText
    private lateinit var btn:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var diaog: Dialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firstname=findViewById(R.id.firstname)
        lastname=findViewById(R.id.lastname)
        emil=findViewById(R.id.email)
        number=findViewById(R.id.phonenumber)
        password=findViewById(R.id.passwordEditText)
        btn=findViewById(R.id.create)

        auth = Firebase.auth
        database = Firebase.database.reference

        //        For password visibility
        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)

        // Set the initial icon to the "eye-off" drawable (password hidden)
        passwordInputLayout.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.eyeclose)

        // Variable to keep track of password visibility state
        var isPasswordVisible = false

        // Toggle password visibility when the end icon is clicked
        passwordInputLayout.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Show password
                password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordInputLayout.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.eyeopen)
            } else {
                // Hide password
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordInputLayout.endIconDrawable = ContextCompat.getDrawable(this, R.drawable.eyeclose)
            }

            // Move the cursor to the end after toggling
            password.setSelection(password.text.length)
        }

        btn.setOnClickListener {

            val first=firstname.text.toString().trim()
            val last=lastname.text.toString().trim()
            val mail=emil.text.toString().trim()
            val mobile=number.text.toString().trim()
            val pass=password.text.toString().trim()

            if (first.isEmpty()){
                firstname.error="Required"
            }
            else if (last.isEmpty()){
                lastname.error="Required"
            }
            else if (mail.isEmpty()){
                emil.error="Required"
            }
            else if (mobile.isEmpty()){
                number.error="Required"
            }
            else if (pass.isEmpty()){
                password.error="Required"
            }
            else{

                showdialog()
                auth.createUserWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener(this){task->
                        if (task.isSuccessful){

                            val uid=auth.currentUser?.uid
                            val userd=Logindata(first,last,mail,mobile,pass)

                            database.child("users").child(uid!!).setValue(userd)
                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                            Toast.makeText(this,"Account created sucessfully",Toast.LENGTH_LONG).show()
                            diaog?.dismiss()

                        }else{
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                            diaog?.dismiss()
                        }

                    }

            }


        }

    }
    private fun showdialog(){
        diaog= Dialog(this)
        diaog?.setContentView(R.layout.progress_dialog)
        diaog?.setCancelable(false)
        diaog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        diaog?.show()
    }
}