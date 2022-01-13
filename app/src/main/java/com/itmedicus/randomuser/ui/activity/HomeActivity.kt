package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import com.denzcoskun.imageslider.models.SlideModel
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.ActivityDetailsBinding
import com.itmedicus.randomuser.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController : NavController
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        invalidateOptionsMenu()

        val drawerLayout : DrawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open,
            R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1609775436218-78e51e5e61dd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8YWxhcm0lMjBjbG9ja3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=400&q=60"))
        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1628692945421-21162c93a8a6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjB8fGFsYXJtJTIwY2xvY2t8ZW58MHx8MHx8&auto=format&fit=crop&w=400&q=60"))
        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1595080472879-bdd162e05586?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTZ8fGFsYXJtJTIwY2xvY2t8ZW58MHx8MHx8&auto=format&fit=crop&w=400&q=60"))
        binding.slideImage.setImageList(imageList)


        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.explore -> Toast.makeText(applicationContext,"this is Explore",Toast.LENGTH_SHORT).show()
            }
            when(it.itemId){
                R.id.logout -> { logout()
                    Toast.makeText(applicationContext,"Logout",Toast.LENGTH_SHORT).show()
                }
            }
            when(it.itemId){
                R.id.profile ->
                    Toast.makeText(applicationContext,"Profile",Toast.LENGTH_SHORT).show()
            }

            when(it.itemId){
                R.id.login -> Toast.makeText(applicationContext,"Login",Toast.LENGTH_SHORT).show()
            }

            when(it.itemId){
                R.id.theme ->
                    openThemeDialog()
            }

            true
        }


        binding.card2.setOnClickListener{
            val intent = Intent(this,ListActivity::class.java)
            startActivity(intent)
        }

        binding.card3.setOnClickListener {
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.card4.setOnClickListener {
            //
        }

        binding.card5.setOnClickListener {
            val intent = Intent(this,ShowAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
     logout()
    }


    private fun openThemeDialog(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Apply Theme?")
        builder.setMessage("Do you want to apply dark mode?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }
        builder.setNegativeButton("No") { _, _ ->
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()

    }

    private fun logout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Close?")
        builder.setMessage("Do you want to close the app?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
            Toast.makeText(this,"successfully close the app",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            Toast.makeText(this,"Not Closed the app",Toast.LENGTH_SHORT).show()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }

}