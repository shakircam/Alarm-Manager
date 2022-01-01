package com.itmedicus.randomuser.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.FragmentAlarmDialogBinding
import com.itmedicus.randomuser.ui.activity.AlarmCreateActivity
import com.itmedicus.randomuser.ui.activity.TwoTimesAlarmActivity


class AlarmDialogFragment : DialogFragment() {
    private var _binding : FragmentAlarmDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAlarmDialogBinding.inflate(inflater, container, false)

        binding.oneTv.setOnClickListener {
            dismiss()
        }
        binding.twoTv.setOnClickListener {
            val intent = Intent(activity, TwoTimesAlarmActivity::class.java)
            startActivity(intent)
            // finish activity & dialog
            requireActivity().finish()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle(getString(R.string.title_alarm))
    }
    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog?.window?.setLayout(width, height)
    }
}