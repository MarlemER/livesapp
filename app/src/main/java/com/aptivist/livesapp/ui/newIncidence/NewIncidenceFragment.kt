package com.aptivist.livesapp.ui.newIncidence

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController

import com.aptivist.livesapp.R
import kotlinx.android.synthetic.main.new_incidence_fragment.*
import org.koin.ext.scope
import java.text.SimpleDateFormat
import java.util.*

class NewIncidenceFragment : Fragment() {

    companion object {
        fun newInstance() = NewIncidenceFragment()
    }

    private lateinit var viewModel: NewIncidenceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_incidence_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewIncidenceViewModel::class.java)
        // TODO: Use the ViewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnOpenDate.setOnClickListener {
            var c = Calendar.getInstance()
            var year = c.get(Calendar.YEAR)
            var month = c.get(Calendar.MONTH)
            var dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
            var hour = c.get(Calendar.HOUR_OF_DAY)
            var minute = c.get(Calendar.MINUTE)
            var datePicker = DatePickerDialog(it.context,DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDayOfMonth ->
                c.set(Calendar.YEAR,mYear)
                c.set(Calendar.MONTH,mMonth)
                c.set(Calendar.DAY_OF_MONTH,mDayOfMonth)
               // Log.d("****",dateEnd)
            },year, month,dayOfMonth )
            var timerPicker = TimePickerDialog(it.context,TimePickerDialog.OnTimeSetListener { view, mHourOfDay, mMinute ->
               // txtDateTime.text = SimpleDateFormat("MMM dd, YYYY HH:mm a", Locale.US).format(c.time)
                c.set(Calendar.HOUR_OF_DAY,mHourOfDay)
                c.set(Calendar.MINUTE,mMinute)
                txtDateTime.text = SimpleDateFormat("MMM dd, YYYY HH:mm a", Locale.US).format(c.time)
            },hour,minute,false)
            timerPicker.show()
            datePicker.show()
            datePicker.setOnCancelListener {
                timerPicker.dismiss()
                txtDateTime.text = null
                }
            timerPicker.setOnCancelListener {
                c.set(Calendar.HOUR_OF_DAY,hour)
                c.set(Calendar.MINUTE,minute)
                txtDateTime.text = SimpleDateFormat("MMM dd, YYYY HH:mm a", Locale.US).format(c.time)
            }

        }
    }
}
