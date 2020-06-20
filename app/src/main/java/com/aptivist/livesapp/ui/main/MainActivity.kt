package com.aptivist.livesapp.ui.main

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aptivist.livesapp.R
import com.aptivist.livesapp.di.implementation.SharePreferencesImpl
import com.aptivist.livesapp.di.interfaces.IFirebaseInstance
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.helpers.Constants
import com.aptivist.livesapp.helpers.Constants.Companion.PHOTO_USER_DEFAULT
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_EMAILUSER_FIREBASE
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_PASSUSER_FIREBASE
import com.aptivist.livesapp.helpers.Constants.Companion.SHAREPREF_TOKEN_FACEBOOK
import com.aptivist.livesapp.model.UserData
import com.aptivist.livesapp.ui.home.HomeFragment
import com.aptivist.livesapp.ui.indicator.IndicatorFragment
import com.aptivist.livesapp.ui.profile.ProfileFragment
import com.aptivist.livesapp.ui.signin.SigninViewModel
import com.aptivist.livesapp.ui.splash.SplashActivity
import com.aptivist.livesapp.ui.start.StartActivity
import com.facebook.login.LoginManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView:NavigationView
    private lateinit var view:View
    lateinit var mainViewModel: MainViewModel
    private lateinit var pHelper: SharePreferencesImpl
    private val messagesUser: IMessagesDialogs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pHelper = SharePreferencesImpl.newInstance(applicationContext)!!

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
            R.id.nav_home,
            R.id.nav_profile,
            R.id.nav_indicators,
            R.id.nav_statistics,
            R.id.nav_settings
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            logout()
        }

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
        NameProfile.text = user.userUser?:resources.getString(R.string.livesapp_user)
        var EmailProfile:TextView = view.findViewById(R.id.txtEmailProfileUser)
        EmailProfile.text = user.emailUser?:resources.getString(R.string.email_livesapp)
        var PhotoProfile:ImageView = view.findViewById(R.id.imgProfileUser)
        Picasso.get().load( (user.photoUser)?:PHOTO_USER_DEFAULT).resize(250,250).centerCrop().into(PhotoProfile)
        messagesUser.showToast(this,resources.getString(R.string.login_successful))
    }

    private fun logout() : Boolean
    {
        /*LoginManager.getInstance().logOut()*/
        mainViewModel.logout()
        pHelper.clearDataFirebase(SHAREPREF_TOKEN_FACEBOOK)
        pHelper.clearDataFirebase(SHAREPREF_EMAILUSER_FIREBASE)
        pHelper.clearDataFirebase(SHAREPREF_PASSUSER_FIREBASE)

        var intent = Intent(this,StartActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        messagesUser.showToast(this,"Logout success")
        //Toast.makeText(this, "Logout success", Toast.LENGTH_SHORT).show()
        //finish()
        messagesUser.showToast(this,resources.getString(R.string.logout_successful))
        return true
    }

}
