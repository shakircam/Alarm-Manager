package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmedicus.randomuser.data.adapter.HistoryAdapter
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.data.adapter.UserAdapter
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.databinding.ActivityDetailsBinding
import com.itmedicus.randomuser.databinding.ActivityHistoryBinding
import com.itmedicus.randomuser.model.Dami
import com.itmedicus.randomuser.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity(), ItemClickListener, SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityHistoryBinding
    private val adapter by lazy { HistoryAdapter(this) }
    var list = mutableListOf<Result>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        initRecyclerView()
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun getData(){
        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(this@HistoryActivity).userDao()
            val dbList= db.getAllUserData()
            list.addAll(dbList)
            adapter.setData(list)
        }
    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    override fun onItemSend(position: Int) {
        val intent = Intent(this,DetailsActivity::class.java)
        val item =  list[position]

        intent.putExtra("gender",item.gender)
        intent.putExtra("email",item.email)
        intent.putExtra("nat",item.nat)
        intent.putExtra("phone",item.phone)
        intent.putExtra("pic",item.picture)
        intent.putExtra("location",item.location)
        intent.putExtra("state",item.location)
        intent.putExtra("name",item.name)

        startActivity(intent)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        lifecycleScope.launch {
            val db = UserDatabase.getDatabase(this@HistoryActivity).userDao()
            val it = db.searchDatabase(searchQuery)
            adapter.setData(it)
        }
    }
}