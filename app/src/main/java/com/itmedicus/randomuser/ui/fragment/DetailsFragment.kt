package com.itmedicus.randomuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.FragmentDetailsBinding
import com.itmedicus.randomuser.databinding.FragmentListBinding


class DetailsFragment : Fragment() {

  //  private val args by navArgs<DetailsFragmentArgs>()

    private var _binding : FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
       /* val data = args
        val nat = data.result.nat
        val gender =  data.result.gender
        val email = data.result.email

        binding.nationality.text= nat
        binding.email.text = email
        binding.gender.text=gender*/

        return binding.root
    }


}