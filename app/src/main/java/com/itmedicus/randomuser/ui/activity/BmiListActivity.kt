package com.itmedicus.randomuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itmedicus.randomuser.data.adapter.BmiAdapter
import com.itmedicus.randomuser.databinding.ActivityBmiBinding
import com.itmedicus.randomuser.databinding.ActivityBmiListBinding
import com.itmedicus.randomuser.model.BmiRecord
import com.itmedicus.randomuser.ui.viewmodel.AlarmViewModel
import com.itmedicus.randomuser.utils.SwipeToDelete

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
        swipeToDelete(mRecyclerView)

    }


    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.list[viewHolder.adapterPosition]
                // Delete Item
                myViewModel.deleteBmiData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}