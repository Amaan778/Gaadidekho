package com.app.gadicustomer.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.gadicustomer.R
import com.app.gadicustomer.payment.Payments
import java.util.Calendar

class BookingActvity : AppCompatActivity() {
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var number: EditText
    private lateinit var bookingservice:EditText
    private lateinit var etDate:EditText
    private lateinit var etTime:EditText
    private lateinit var pricing:TextView
    private lateinit var btn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_actvity)

        name=findViewById(R.id.name)
        email=findViewById(R.id.email)
        number=findViewById(R.id.number)
        pricing=findViewById(R.id.pricing)
        etDate = findViewById(R.id.et_date)
        etTime = findViewById(R.id.et_time)
        bookingservice=findViewById(R.id.booking)
        btn=findViewById(R.id.booknow)

        val service=intent.getStringExtra("servicename")
        bookingservice.setText(service.toString())

        val offerprice=intent.getStringExtra("offer")
        pricing.setText(offerprice)

        val id=intent.getStringExtra("id")
        val calendar = Calendar.getInstance()

        // Date Picker
        etDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    etDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time Picker
        etTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    etTime.setText(selectedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true // 24-hour format
            ).show()
        }

        btn.setOnClickListener {
            val names=name.text.toString().trim()
            val emails=email.text.toString().trim()
            val num=number.text.toString().trim()
            val price=pricing.text.toString().trim()
            val date=etDate.text.toString().trim()
            val time=etTime.text.toString().trim()
            val bkservice=bookingservice.text.toString().trim()
            val idd=id.toString()

            val intent=Intent(this,Payments::class.java)
            intent.putExtra("name",names)
            intent.putExtra("email",emails)
            intent.putExtra("number",num)
            intent.putExtra("price",price)
            intent.putExtra("date",date)
            intent.putExtra("time",time)
            intent.putExtra("service",bkservice)
            intent.putExtra("id",idd)
            startActivity(intent)

        }

    }
}