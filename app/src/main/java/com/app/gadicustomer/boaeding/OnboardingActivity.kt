package com.app.gadicustomer.boaeding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.app.gadicustomer.auth.LoginActivity
import com.app.gadicustomer.R

class OnboardingActivity : AppCompatActivity() {
    private lateinit var btn:Button
    var sharedPreferences: SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        btn=findViewById(R.id.btn)

        if (restore()){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            saved()
        }

    }
    private fun saved(){
        sharedPreferences=applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor=sharedPreferences!!.edit()
        editor.putBoolean("first",true)
        editor.apply()
    }

    private fun restore():Boolean{
        sharedPreferences=applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("first",false)
    }
}