package com.hy.group3_project.ViewActivities.Account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hy.group3_project.R
import com.hy.group3_project.ViewActivities.BaseActivity
import com.hy.group3_project.databinding.ActivityPropertyDetailBinding

class PropertyDetailActivity : BaseActivity() {
    lateinit var binding:ActivityPropertyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set option menu
        setSupportActionBar(this.binding.tbOptionMenu)

    }
}