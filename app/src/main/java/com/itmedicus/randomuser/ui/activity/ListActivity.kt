package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

class ListActivity : AppCompatActivity(),ItemClickListener {

    private lateinit var binding: ActivityListBinding
    private val adapter by lazy { UserAdapter(this) }
    var list = mutableListOf<Dami.Results>()
    var allUser = mutableListOf<Result>()
    var flag = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        initRecyclerView()


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
                    adapter.setData(list)


                }
            }
            override fun onFailure(call: Call<Dami>, t: Throwable) {
                Log.d("dd",t.localizedMessage)
            }
        })
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
            allUser[position].id


            val listSize = db.getAllUser().size
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
            }

        }
        startActivity(intent)
    }
}