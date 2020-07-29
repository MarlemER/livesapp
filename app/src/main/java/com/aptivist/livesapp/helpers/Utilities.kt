package com.aptivist.livesapp.helpers

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*


open class Utilities {

    fun calendarDatePicker(context: Context):Calendar{
        var c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
        var hour = c.get(Calendar.HOUR_OF_DAY)
        var minute = c.get(Calendar.MINUTE)
        var datePicker = DatePickerDialog(context,DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDayOfMonth ->
            c.set(Calendar.YEAR,mYear)
            c.set(Calendar.MONTH,mMonth)
            c.set(Calendar.DAY_OF_MONTH,mDayOfMonth)
            // Log.d("****",dateEnd)
        },year, month,dayOfMonth )
        var timerPicker = TimePickerDialog(context,TimePickerDialog.OnTimeSetListener { view, mHourOfDay, mMinute ->
            // txtDateTime.text = SimpleDateFormat("MMM dd, YYYY HH:mm a", Locale.US).format(c.time)
            c.set(Calendar.HOUR_OF_DAY,mHourOfDay)
            c.set(Calendar.MINUTE,mMinute)
            dateTimeFormat(c)
        },hour,minute,false)
        timerPicker.show()
        datePicker.show()
        datePicker.setOnCancelListener {
            timerPicker.dismiss()

        }
        timerPicker.setOnCancelListener {
            c.set(Calendar.HOUR_OF_DAY,hour)
            c.set(Calendar.MINUTE,minute)
            dateTimeFormat(c)
        }
        return c
    }

    fun calendarDate():Calendar{
        return Calendar.getInstance()
    }

    private fun dateTimeFormat(calendar:Calendar?):String{
       return SimpleDateFormat("MMM dd, YYYY HH:mm a", Locale.US).format(calendar?.time).toString()
    }

    fun validationFieldsNewIncidence(optionAddNewIncidence:Int?, latitude:Double?, longitude:Double?, dateTime:String?):Boolean{
        return optionAddNewIncidence!=null && longitude!=null && latitude!=null && dateTime!= null
    }

    fun convertIncidenceName(itemSelected:Int):String{
        return when(itemSelected<=10) {
                itemSelected == (1) -> "Stolen"
                itemSelected == (2)-> "Homicid"
                itemSelected == (3)-> "IlegalSales"
                itemSelected == (4)-> "Misplacement"
                itemSelected == (5)-> "Atack"
                itemSelected == (6)-> "Accident"
                itemSelected == (7)-> "Shoplifting"
                itemSelected == (8)-> "Vandalism"
                itemSelected == (9)-> "Other"
            else -> "No found"
        }
    }

    fun convertEnumIncidenceName(itemSelected:Int):String{
        return when(EnumIncidenceType.values().size<=10) {
            itemSelected==1-> EnumIncidenceType.Stole.incidenceName()
            itemSelected==2-> EnumIncidenceType.Homicid.incidenceName()
            itemSelected==3-> EnumIncidenceType.IlegalSales.incidenceName()
            itemSelected==4-> EnumIncidenceType.Misplacement.incidenceName()
            itemSelected==5-> EnumIncidenceType.Atack.incidenceName()
            itemSelected==6-> EnumIncidenceType.Accident.incidenceName()
            itemSelected==7-> EnumIncidenceType.Shoplifting.incidenceName()
            itemSelected==8-> EnumIncidenceType.Vandalism.incidenceName()
            itemSelected==9-> EnumIncidenceType.Other.incidenceName()
            else -> "No found"
        }
    }

}