package com.itmedicus.randomuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.card2.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToListFragment()
            view?.findNavController()?.navigate(action)
        }
        return binding.root
    }

}