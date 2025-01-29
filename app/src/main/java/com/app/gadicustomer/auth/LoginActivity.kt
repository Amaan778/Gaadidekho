package com.app.gadicustomer.auth

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.gadicustomer.Dashboard
import com.app.gadicustomer.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btn: Button
    private lateinit var reg:TextView
    private lateinit var auth: FirebaseAuth
    private var diaog: Dialog?=null
    var sharedPreferences: SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email=findViewById(R.id.email)
        password=findViewById(R.id.passwordEditText)
        btn=findViewById(R.id.loginbtn)
        reg=findViewById(R.id.register)

        auth = Firebase.auth

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
                passwordInputLayout.endIconDrawable = ContextCompat.getDrawable(this,
                    R.drawable.eyeopen
                )
            } else {
                // Hide password
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordInputLayout.endIconDrawable = ContextCompat.getDrawable(this,
                    R.drawable.eyeclose
                )
            }

            // Move the cursor to the end after toggling
            password.setSelection(password.text.length)
        }

//        going to register screen
        reg.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        if (restore()){
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        btn.setOnClickListener {

            val mail=email.text.toString().trim()
            val pass=password.text.toString().trim()

            if (mail.isEmpty()){
                email.error="Required"
            }
            else if (pass.isEmpty()){
                password.error="Required"
            }
            else{

                showdialog()
                auth.signInWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener(this){task->
                        if (task.isSuccessful){

                            Toast.makeText(baseContext, "Login sucessful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,Dashboard::class.java))
                            finish()
                            saved()
                            diaog?.dismiss()

                        }
                        else{
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            diaog?.dismiss()
                        }

                    }

            }

        }

    }

    private fun saved(){
        sharedPreferences=applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor=sharedPreferences!!.edit()
        editor.putBoolean("clicked",true)
        editor.apply()
    }

    private fun restore():Boolean{
        sharedPreferences=applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("clicked",false)
    }

    private fun showdialog(){
        diaog= Dialog(this)
        diaog?.setContentView(R.layout.progress_dialog)
        diaog?.setCancelable(false)
        diaog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        diaog?.show()
    }

}