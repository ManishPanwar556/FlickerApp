package com.example.flickerapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.flickerapp.R
import com.example.flickerapp.databinding.ActivityMainBinding
import com.example.flickerapp.viewModel.FlickerViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    val viewModel by lazy {
        ViewModelProvider(this).get(FlickerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.Open, R.string.Close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                   val transaction=supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, HomeFragment())
                    transaction.commit()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }

}