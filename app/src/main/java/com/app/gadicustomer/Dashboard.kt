package com.app.gadicustomer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.app.gadicustomer.about.AboutFragment
import com.app.gadicustomer.bookings.BookingFragment
import com.app.gadicustomer.favorite.FavoriteFragment
import com.app.gadicustomer.home.HomeFragment
import com.app.gadicustomer.profile.ProfileFragment
import com.app.gadicustomer.shareswithsomefrag.ContactFragment
import com.app.gadicustomer.shareswithsomefrag.PrivacyFragment
import com.app.gadicustomer.shareswithsomefrag.RateFragment
import com.app.gadicustomer.shareswithsomefrag.ShareFragment
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerlayout: DrawerLayout
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerlayout = findViewById(R.id.main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.open, R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment(),"Home") // Set HomeFragment as default
            navigationView.setCheckedItem(R.id.nav_home)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home->replaceFragment(HomeFragment(),"Home")
            R.id.nav_favorite -> replaceFragment(FavoriteFragment(),"Favorite")
            R.id.nav_booking -> replaceFragment(BookingFragment(), "Booking")
            R.id.nav_profile->replaceFragment(ProfileFragment(),"Profile")
            R.id.nav_share->shareApp()
            R.id.nav_rate->showRateUsDialog()
            R.id.nav_privacy->replaceFragment(PrivacyFragment(),"Privacy Policy")
            R.id.nav_contact->replaceFragment(ContactFragment(),"Contact us")
            R.id.nav_about->showAboutDialog()
        }
        drawerlayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment,tag:String) {
        // Replace the fragment and update the currentFragmentTag
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentcontainer, fragment,tag)
        transaction.commit()
        currentFragmentTag = tag
    }

//    about us
    private fun showAboutDialog() {

        // Inflate the custom layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_about, null)

        // Create AlertDialog and set the custom view
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Prevent dismissal on outside touch
            .create()

//        / Remove the default dialog background
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Access views in the custom layout
        val btn = dialogView.findViewById<TextView>(R.id.close)

        btn.setOnClickListener {
            alertDialog.dismiss() // Close dialog
        }

        // Show the dialog
        alertDialog.show()
    }

    private fun shareApp() {
        val appPackageName = packageName // Get your app's package name
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this awesome app!")
            putExtra(Intent.EXTRA_TEXT, "Hey! Download this amazing app:\nhttps://play.google.com/store/apps/details?id=$appPackageName")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

//    rate us

    private fun showRateUsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.fragment_rate, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val btnRateNow = dialogView.findViewById<Button>(R.id.btnRateNow)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        //        / Remove the default dialog background
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnRateNow.setOnClickListener {
            val rating = ratingBar.rating
            if (rating >= 4) {
                // Redirect to Play Store for positive rating
                Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
            } else {
                // Show feedback option for low rating
                Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
            }
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


    override fun onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            // Close the drawer if it's open
            drawerlayout.closeDrawer(GravityCompat.START)
        } else if (currentFragmentTag != "Home") {
            // If not on HomeFragment, navigate to HomeFragment
            replaceFragment(HomeFragment(), "Home")
        } else {
            // If already on HomeFragment, use the default back-press behavior
            super.onBackPressed()
        }
    }

}