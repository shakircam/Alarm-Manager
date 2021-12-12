package com.itmedicus.randomuser.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.data.adapter.UserAdapter
import com.itmedicus.randomuser.data.network.ApiInterface
import com.itmedicus.randomuser.data.network.RetrofitClient
import com.itmedicus.randomuser.databinding.FragmentListBinding
import com.itmedicus.randomuser.model.Dami
import com.itmedicus.randomuser.model.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListFragment : Fragment(),ItemClickListener {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { UserAdapter(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        getData()
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
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
                    val list = mutableListOf<Dami.Results>()
                    list.addAll(it.results)
                    adapter.setData(list)
                    val code = response.code()
                    Log.d("tag","$code")
                    Log.d("tag",it.toString())
                    Log.d("tag",list.toString())

                }
            }
            override fun onFailure(call: Call<Dami>, t: Throwable) {
               Log.d("tag",t.localizedMessage)
            }
        })
    }

    override fun onItemSend(position: Int) {
        //TODO("Not yet implemented")
    }


}