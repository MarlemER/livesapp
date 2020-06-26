package com.aptivist.livesapp.ui.newIncidence

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController

import com.aptivist.livesapp.R
import com.aptivist.livesapp.databinding.NewIncidenceFragmentBinding
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.helpers.Constants.Companion.PICTURE_NAME
import com.aptivist.livesapp.helpers.Constants.Companion.REQUEST_CODE_CAMERA
import com.aptivist.livesapp.helpers.Utilities
import com.google.protobuf.compiler.PluginProtos
import kotlinx.android.synthetic.main.new_incidence_fragment.*
import kotlinx.android.synthetic.main.new_incidence_fragment.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.ext.getScopeName
import org.koin.ext.scope
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewIncidenceFragment : Fragment() {

    companion object {
        fun newInstance() = NewIncidenceFragment()
    }

    val viewModelFragment by viewModel<NewIncidenceViewModel>()
    var utilities:Utilities? = null
    private val messageUser: IMessagesDialogs by inject()
    lateinit var photoFile: File
    var currentItemSelect:CardView? = null
    var arrayCardView:ArrayList<CardView>? = null

    lateinit var binding: NewIncidenceFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.new_incidence_fragment, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.new_incidence_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.newIncidenceFragment = this
        binding.newIncidenceViewModel = viewModelFragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(NewIncidenceViewModel::class.java)
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
            //txtDateTime.text = utilities?.dateTimeFormat(utilities?.calendarDatePicker(it.context)).toString()
        }

        btnOpenCamera.setOnClickListener {
            openCamera(it)
        }

        txtPicturePreview.setOnClickListener {
            openPicturePreview(it)
        }
    }

    private fun openCamera(it:View){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       // photoFile = getPhotoPreview(PICTURE_NAME)
        if(takePictureIntent.resolveActivity(activity!!.packageManager)!=null){
            startActivityForResult(takePictureIntent,REQUEST_CODE_CAMERA)
        }else{
            messageUser.showToast(it.context,resources.getString(R.string.user_register_successful))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode== Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            //imgPreview.setImageBitmap(takenImage)
            txtPicturePreview.text = "Preview"
        }else{
            super.onActivityResult(requestCode, resultCode, data)
            txtPicturePreview.text = "No image"
        }
    }

    fun openPicturePreview(it:View){
        messageUser.showMessagePreview("Preview","Show image",it.context)
    }

    /*fun getPhotoPreview(fileName:String,it:View):File{
        val storageDirectory = getExternalFilesDirs(it.context,Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName,".jpg", storageDirector)
    }*/


    fun selectColor(view: View){
        for(item in glNewIncidence.children)
        {
            if((item as CardView).id==view.id){
                item.setCardBackgroundColor(Color.parseColor("#44BCF4"))
                messageUser.showToast(view.context,"Clic on${item.id}")
            }else{
                item.setCardBackgroundColor(Color.parseColor("#ACDBF1"))
            }
        }
    }

}
