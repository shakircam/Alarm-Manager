package com.itmedicus.randomuser.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.viewpager2.widget.ViewPager2
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.FragmentSecondScreenBinding


class SecondScreen : Fragment() {

    private var _binding : FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)
        val topAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_animation)

        binding.imageView2.animation = topAnim
        binding.title2.animation = bottomAnim
        binding.description2.animation = bottomAnim

        binding.next2.setOnClickListener {
            viewPager?.currentItem = 2
        }

        return binding.root
    }
}