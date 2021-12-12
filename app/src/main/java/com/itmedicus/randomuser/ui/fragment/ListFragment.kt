package com.itmedicus.randomuser.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.adapter.UserAdapter
import com.itmedicus.randomuser.data.network.ApiInterface
import com.itmedicus.randomuser.data.network.RetrofitClient
import com.itmedicus.randomuser.data.repository.UserRepository
import com.itmedicus.randomuser.databinding.FragmentHomeBinding
import com.itmedicus.randomuser.databinding.FragmentListBinding
import com.itmedicus.randomuser.json.Dami
import com.itmedicus.randomuser.model.User
import com.itmedicus.randomuser.ui.viewmodel.UserViewModel
import com.itmedicus.randomuser.ui.viewmodel.UserViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListFragment : Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { UserAdapter() }
    private val repository by lazy { UserRepository() }
    //List

    //ViewModel
    private val viewModel by lazy {
        val factory = UserViewModelFactory(repository)
        ViewModelProvider(this,factory)[UserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

       /* viewModel.userListLiveData.observe(viewLifecycleOwner,{
            adapter.setData(it)
            Log.d("tag",it.toString())
        })*/

        getData()

      //  adapter.setData()
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
    }



   /* private fun getData(){
        val apiInterface = RetrofitClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getRandomUser()

        call.enqueue(object : Callback<MutableList<Dami.Result>> {

            override fun onResponse(
                call: Call<MutableList<Dami.Result>>,
                response: Response<MutableList<Dami.Result>>
            ) {
                response.body()?.let {

                    val code = response.code()
                    Log.d("tag","$code")
                    Log.d("tag",it.toString())
                }
            }
            override fun onFailure(call: Call<MutableList<Dami.Result>>, t: Throwable) {
                Log.d("tag",t.localizedMessage)
            }
        })
    }*/

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


}