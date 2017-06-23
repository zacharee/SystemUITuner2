package com.zacharee1.systemuituner.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zacharee1.systemuituner.R
import com.zacharee1.systemuituner.SetThings

class TaskerInstructionsActivity : AppCompatActivity() {
    private var mSetThings: SetThings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSetThings = SetThings(this)

        setContentView(R.layout.activity_tasker_instructions)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
