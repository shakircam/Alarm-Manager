package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.data.adapter.NewsAdapter
import com.itmedicus.randomuser.data.adapter.UserAdapter
import com.itmedicus.randomuser.data.network.ApiInterface
import com.itmedicus.randomuser.data.network.NewsInterface
import com.itmedicus.randomuser.data.network.RetrofitClient
import com.itmedicus.randomuser.data.network.RetrofitNewsClient
import com.itmedicus.randomuser.databinding.ActivityHomeBinding
import com.itmedicus.randomuser.databinding.ActivityNewsBinding
import com.itmedicus.randomuser.model.Dami
import com.itmedicus.randomuser.model.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity(), ItemClickListener {

  companion object{
      const val TAG = "NewsActivity"
  }
    private lateinit var binding: ActivityNewsBinding
    private val adapter by lazy { NewsAdapter(this) }
    var newsList = mutableListOf<News.Article>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getNews()
        initRecyclerView()

    }


    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    private fun getNews(){
        val apiInterface = RetrofitNewsClient.getNewsClient().create(NewsInterface::class.java)
        val call = apiInterface.getNews()

        call.enqueue(object : Callback<News> {

            override fun onResponse(
                call: Call<News>,
                response: Response<News>
            ) {
                response.body()?.let {
                    newsList.addAll(it.articles)

                    adapter.setData(newsList)

                }
            }
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("dd",t.localizedMessage)
            }
        })
    }

    override fun onItemSend(position: Int) {
        val intent = Intent(this,NewsDetailsActivity::class.java)
        val item =  newsList[position]

        intent.putExtra("title",item.title)
        intent.putExtra("excerpt",item.excerpt)
        intent.putExtra("image",item.imageUrl)
        startActivity(intent)
    }


}