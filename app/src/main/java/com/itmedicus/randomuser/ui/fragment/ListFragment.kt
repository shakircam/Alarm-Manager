package com.itmedicus.randomuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.data.adapter.UserAdapter
import com.itmedicus.randomuser.data.repository.UserRepository
import com.itmedicus.randomuser.databinding.FragmentHomeBinding
import com.itmedicus.randomuser.databinding.FragmentListBinding
import com.itmedicus.randomuser.ui.viewmodel.UserViewModel
import com.itmedicus.randomuser.ui.viewmodel.UserViewModelFactory


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

        viewModel.userListLiveData.observe(viewLifecycleOwner,{
            //adapter.setData(it)
        })
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
    }

}