package com.itmedicus.randomuser.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.data.adapter.UserAdapter
import com.itmedicus.randomuser.data.local.UserDatabase
import com.itmedicus.randomuser.data.network.ApiInterface
import com.itmedicus.randomuser.data.network.RetrofitClient
import com.itmedicus.randomuser.databinding.ActivityListBinding
import com.itmedicus.randomuser.model.Dami
import com.itmedicus.randomuser.model.Name
import com.itmedicus.randomuser.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.net.Network

import androidx.annotation.NonNull

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.lang.Exception
import com.itmedicus.randomuser.MainActivity

import android.widget.Toast
import com.itmedicus.randomuser.R


class ListActivity : AppCompatActivity(),ItemClickListener {

    private lateinit var binding: ActivityListBinding
    private val adapter by lazy { UserAdapter(this) }
    var list = mutableListOf<Dami.Results>()
    var allUser = mutableListOf<Result>()
    var flag = 1
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    var isConnected = false
    var connectivityManager: ConnectivityManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        initRecyclerView()
        shimmerFrameLayout = binding.shimmerLayout

    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()

    }

    override fun onPause() {
        shimmerFrameLayout.stopShimmer()
        super.onPause()
    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    private fun getData(){
        val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getRandomUserr()

        call.enqueue(object : Callback<Dami> {

            override fun onResponse(
                call: Call<Dami>,
                response: Response<Dami>
            ) {
                response.body()?.let {
                    list.addAll(it.results)

                    shimmerFrameLayout.isVisible = false
                    adapter.setData(list)
                    binding.recyclerview.isVisible = true

                }
            }
            override fun onFailure(call: Call<Dami>, t: Throwable) {
                Log.d("dd",t.localizedMessage)
            }
        })
    }


    private fun registerNetworkCallback() {
        try {
            connectivityManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager!!.registerDefaultNetworkCallback(object : NetworkCallback() {

                override fun onAvailable(network: Network) {
                    isConnected = true
                }

                override fun onLost(network: Network) {
                    isConnected = false
                    networkDialog()
                }
            })
        } catch (e: Exception) {
            isConnected = false
        }
    }

    private fun unregisterNetworkCallback() {
        connectivityManager!!.unregisterNetworkCallback(NetworkCallback())
    }


    private fun networkDialog(){
        val dialog = Dialog(this)
        dialog.setTitle("Select Day")
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.network_dialog)
        dialog.context.setTheme(R.style.CustomDialog)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)

        val tryBt = dialog.findViewById<Button>(R.id.tryBt)
        tryBt.setOnClickListener {
            if (isConnected){
                dialog.dismiss()
            }else{
                Toast.makeText(this,"Not Connected",Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }


    override fun onItemSend(position: Int) {
        val intent = Intent(this,DetailsActivity::class.java)
        val item =  list[position]

        intent.putExtra("gender",item.gender)
        intent.putExtra("email",item.email)
        intent.putExtra("nat",item.nat)
        intent.putExtra("phone",item.phone)
        intent.putExtra("pic",item.picture.large)
        intent.putExtra("location",item.location.city)
        intent.putExtra("state",item.location.state)
        intent.putExtra("name",item.name.first+item.name.last)
        val name = item.name.first+item.name.last

        lifecycleScope.launch(Dispatchers.IO) {
            val db = UserDatabase.getDatabase(this@ListActivity).userDao()
            val user = Result(item.name.first+item.name.last,item.phone,item.gender,item.email,item.nat,item.picture.medium,item.location.city)

             val allRandomUser = db.getAllRandomUser()
             allUser.addAll(allRandomUser)

             val id = allUser.filter { it.name == name }.map { it.id }
             val remove = allUser.removeIf { it.name == name }
             Log.d("filter", " $id ::: $remove")
             Log.d("filter",allUser.size.toString())
             allUser.add(user)

            for (i in allUser){
                if (name == i.name){
                    flag = 2
                    break
                }
            }

            if (flag == 2){

                 allUser.removeIf { it.name == name }
                 allUser.add(user)
                 db.insertToLocal(allUser)
                 flag = 1

            }else{
                if (allUser.size == 10){
                    allUser.removeFirst()
                    allUser.add(user)
                    db.insertToLocal(allUser)

                }else{
                    if(allUser.size<10)
                        allUser.add(user)
                     db.insertToLocal(allUser)
                }
            }




          /*  val listSize = db.getAllUser().size
            val userName = db.allUserNameList()
            val userId = db.getUserId(name)
            val nameList = mutableListOf<Name>()
            nameList.addAll(userName)

            for (i in nameList){
                if (name == i.name){
                    flag = 2
                    break
                }
            }

            if (flag == 2){

                db.deleteItem(userId)
               // Log.d("tag","delete item when match:: $name,list size-> $listSize")
                db.insertData(user)
                Log.d("tag","Item match go to up-> $name ::: list size-> $listSize")
                flag = 1
            }else{
                if (listSize == 10){
                    val userList = db.getAllUser()
                    val id = userList.first().id!!

                    db.deleteItem(id)
                   // Log.d("tag","delete-> $name ::: list size-> $listSize")
                    db.insertData(user)
                    Log.d("tag","list size-> $listSize already, insert -> $name ::: move to up ")

                }else{
                    if(listSize<10)
                    db.insertData(user)
                    Log.d("tag","insert-> $name ::: list size->  $listSize")
                }
            }*/

        }
        startActivity(intent)
    }
}


