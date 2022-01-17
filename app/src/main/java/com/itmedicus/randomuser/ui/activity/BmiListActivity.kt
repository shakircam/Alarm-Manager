package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmedicus.randomuser.data.adapter.BmiAdapter
import com.itmedicus.randomuser.databinding.ActivityBmiBinding
import com.itmedicus.randomuser.databinding.ActivityBmiListBinding
import com.itmedicus.randomuser.model.BmiRecord
import com.itmedicus.randomuser.ui.viewmodel.AlarmViewModel

class BmiListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBmiListBinding
    private lateinit var myViewModel: AlarmViewModel
    private val adapter by lazy { BmiAdapter() }
    private val list = mutableListOf<BmiRecord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        initRecyclerView()
        myViewModel.getAllBmiResult.observe(this,{
            list.addAll(it)
            adapter.setData(list)
        })

    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }
}