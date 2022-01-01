package com.itmedicus.randomuser.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.itmedicus.randomuser.R
import com.itmedicus.randomuser.databinding.FragmentWeekDialogBinding
import com.itmedicus.randomuser.ui.activity.AlarmCreateActivity
import java.util.*


class WeekDialogFragment : DialogFragment() {
    private var _binding : FragmentWeekDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWeekDialogBinding.inflate(inflater, container, false)


        binding.saveBn.setOnClickListener {

            val intent= Intent(activity,AlarmCreateActivity::class.java)
            //val intent= Intent(activity, WeekendAlarmActivity::class.java)
            if (binding.saturday.isChecked){
                val saturday = "1"

                val calendar : Long = Calendar.getInstance().timeInMillis
                Log.d("req", calendar.toString())
                intent.putExtra("saturday",saturday)
            }
            if (binding.sunday.isChecked){
               val sunday = "2"
                intent.putExtra("sunday",sunday)
            }
            if (binding.monday.isChecked){
                val monday = "3"
                intent.putExtra("monday",monday)
            }
            if (binding.tuesday.isChecked){
                val tuesday = "4"
                intent.putExtra("tuesday",tuesday)
            }
            if (binding.wednesday.isChecked){
               val  wednesday = "5"
                intent.putExtra("wednesday",wednesday)
            }

            if (binding.thursday.isChecked){
                val thursday = "6"

                intent.putExtra("thursday",thursday)
            }

            if (binding.friday.isChecked){
                val friday = "7"
                intent.putExtra("friday",friday)
            }
            startActivity(intent)
            dismiss()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle(getString(R.string.week_alarm))

    }
    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog?.window?.setLayout(width, height)
    }
}