package com.example.contacts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.contacts.R
import com.example.contacts.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setUpNavController()
        setUpBottomNavigation()
    }

    private fun setUpNavController() {
        navController = supportFragmentManager.findFragmentById(R.id.navContainer)
            .let { it as NavHostFragment }.navController
        viewBinding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setUpBottomNavigation() {
        viewBinding.bottomNavigationView.selectedItemId = R.id.usersFragment

        viewBinding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            return@setOnItemSelectedListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }
}