package com.sonbn.remi.mylib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sonbn.remi.mylib.ads.AdBanner
import com.sonbn.remi.mylib.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}