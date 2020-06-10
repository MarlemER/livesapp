package com.aptivist.livesapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aptivist.livesapp.R
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.helpers.Constants
import com.aptivist.livesapp.helpers.Constants.Companion.PHOTO_USER_DEFAULT
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.ui.signin.SigninViewModel
import com.aptivist.livesapp.ui.splash.SplashActivity
import com.facebook.login.LoginManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView:NavigationView
    private lateinit var view:View
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_profile,
            R.id.nav_indicators,
            R.id.nav_statistics
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(this)

        val user : UserData = intent.extras?.get(Constants.USER) as UserData
        getDataProfile(user)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getDataProfile(user : UserData){
        view = navView.getHeaderView(0)
        var NameProfile:TextView = view.findViewById(R.id.txtNameProfileUser)
        NameProfile.text = user.userUser?:"livesapp"
        var EmailProfile:TextView = view.findViewById(R.id.txtEmailProfileUser)
        EmailProfile.text = user.emailUser?:"livesapp@support.com"
        var PhotoProfile:ImageView = view.findViewById(R.id.imgProfileUser)
        Picasso.get().load( (user.photoUser)?:PHOTO_USER_DEFAULT).resize(250,250).centerCrop().into(PhotoProfile)
        Toast.makeText(this,"Login successful",Toast.LENGTH_LONG).show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.nav_logout -> item.setOnMenuItemClickListener {
                logout()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout()
    {
        /*firebaseCredential.getFirebaseAuth().signOut()
        LoginManager.getInstance().logOut()*/
        mainViewModel.logout()

        startActivity(Intent(this,SplashActivity::class.java))
        Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()
        finish()
    }

}
