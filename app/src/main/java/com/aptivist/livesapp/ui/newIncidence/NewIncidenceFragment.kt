package com.aptivist.livesapp.ui.newIncidence

import android.app.*
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.airbnb.lottie.parser.IntegerParser

import com.aptivist.livesapp.R
import com.aptivist.livesapp.databinding.NewIncidenceFragmentBinding
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.helpers.Constants.Companion.REQUEST_CODE_CAMERA
import com.aptivist.livesapp.helpers.Constants.Companion.REQUEST_PERMISSION_CAMERA
import com.aptivist.livesapp.helpers.EnumIncidenceType
import com.aptivist.livesapp.helpers.OnBackClickListener
import com.aptivist.livesapp.helpers.Utilities
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.new_incidence_fragment.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.ext.getScopeName
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class NewIncidenceFragment : Fragment() {

    companion object {
        fun newInstance() = NewIncidenceFragment()
        fun utilities() = Utilities()
    }

    val viewModelFragment by viewModel<NewIncidenceViewModel>()
    var utilities:Utilities? = null
    private val messageUser: IMessagesDialogs by inject()
    lateinit var photoFile: File
    var currentItemSelect:CardView? = null
    var arrayCardView:ArrayList<CardView>? = null
    lateinit var currentPhotoPath: String
    private lateinit var uri: Uri
    val permissionCamera = android.Manifest.permission.CAMERA
    val writeStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val readStorage = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private lateinit var url: String
    private lateinit var bitMap:Bitmap
    lateinit var navController: NavController
    var itemSelected:Int = 0
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
        navController = view.findNavController()
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
        }
        btnOpenCamera.setOnClickListener {requestPermissions() }
        txtPicturePreview.setOnClickListener {showPicture(uri) }
        btnOpenLocation.setOnClickListener { navController.navigate(R.id.goToHomeMap) }
        txtLocationPreview.text = arguments?.getString("addressLocation")
        btnSaveIncident.setOnClickListener { saveNewIncidence() }
    }


    private fun requestPermissions(){
       var isRequired = ActivityCompat.shouldShowRequestPermissionRationale(activity!!,permissionCamera)
        if(!isRequired){
            requestPermissionCamera()
        }
    }

    private fun requestPermissionCamera(){
        requestPermissions(arrayOf(permissionCamera,writeStorage,readStorage), REQUEST_PERMISSION_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PERMISSION_CAMERA->{
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }else
                {
                    context?.applicationContext?.let { messageUser.showToast(it,"Failed") }
                }
            }
        }
    }

    private fun openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            var filePicture: File? = null
            filePicture = createFileImage()
            uri = FileProvider.getUriForFile(activity!!, "com.aptivist.livesapp.fileprovider", filePicture)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri)
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
            }
        }
    }

    private fun createFileImage(): File {
        val timeStamp =  SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val name ="JPEG_" + timeStamp + "_"
        val directory = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imagen  = File.createTempFile(name,".jpg", directory)
        url = "file://" + imagen.absolutePath
        return imagen
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
           showPicture(uri)
            txtPicturePreview.text = "Preview"
        }else
        {
            txtPicturePreview.text = "No image"
        }
    }


    private fun showPicture(uri:Uri){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.picture_preview, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Preview")
        var img = mDialogView.findViewById<ImageView>(R.id.imgPreview)
        var f = File(uri.toString())
        if(!f.exists()){
            Picasso.get().load(uri).resize(800, 1500).centerCrop().into(img)
        }
        mBuilder.show()
    }

    fun selectColor(view: View){
        for(item in glNewIncidence.children)
        {
            if((item as CardView).id==view.id){
                item.setCardBackgroundColor(Color.parseColor("#44BCF4"))
                itemSelected = (item.tag.toString()).toInt()
                //var enum = EnumIncidenceType(itemSelected)
                /*when(itemSelected){
                    itemSelected-> EnumIncidenceType.Accident
                }*/
                messageUser.showToast(view.context,"Clic on${item.tag}")
            }else{
                item.setCardBackgroundColor(Color.parseColor("#ACDBF1"))
            }
        }
    }

    private fun saveNewIncidence(){
        if(utilities().validationFieldsNewIncidence(itemSelected,arguments?.getDouble("longitudeLocation"),arguments?.getDouble("latitudeLocation"),txtDateTime.text.toString()))
        {
            messageUser.showToast(activity!!,"save the data")
        }else{
            messageUser.showToast(activity!!,"failed please complete the required fields")
        }
    }


}
