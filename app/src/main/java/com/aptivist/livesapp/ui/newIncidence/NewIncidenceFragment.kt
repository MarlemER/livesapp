package com.aptivist.livesapp.ui.newIncidence

import android.app.*
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.core.content.FileProvider
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
import com.aptivist.livesapp.helpers.Constants.Companion.REQUEST_PERMISSION_CAMERA
import com.aptivist.livesapp.helpers.Utilities
import com.google.protobuf.compiler.PluginProtos
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.new_incidence_fragment.*
import kotlinx.android.synthetic.main.new_incidence_fragment.view.*
import kotlinx.android.synthetic.main.picture_preview.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.ext.getScopeName
import org.koin.ext.scope
import java.io.File
import java.io.IOException
import java.net.URL
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
    lateinit var currentPhotoPath: String
    private lateinit var uri: Uri
    val permissionCamera = android.Manifest.permission.CAMERA
    val writeStorage = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val readStorage = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private lateinit var url: String

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
         // uri = dispatchTakePictureIntent()
            //requestPermissions()

        }

        txtPicturePreview.setOnClickListener {
            openPicturePreview(it,uri)
        }
    }

    private fun openCamera(it:View){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            var archivoFtot: File? = null
            archivoFtot = crearArchivoImagen()
            uri = FileProvider.getUriForFile(activity!!, "com.aptivist.livesapp.fileprovider", archivoFtot)

            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
            }
        }
    }


    fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(activity!!.packageManager)!= null)
        {
            var archivoFtot: File? = null
            archivoFtot = crearArchivoImagen()
            if(archivoFtot != null){
                val urlFoto = FileProvider.getUriForFile(activity!!, "com.aptivist.livesapp.fileprovider", archivoFtot)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, urlFoto)
                startActivityForResult(intent, REQUEST_CODE_CAMERA)
            }
        }
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
                    //dispatchTakePictureIntent()
                    takePicture()
                }else
                {
                    context?.applicationContext?.let { messageUser.showToast(it,"Failed") }
                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =  context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //val storageDir = Environment.getExternalStorageDirectory()
        //val storageDir = File(Environment.getExternalStorageDirectory()+"/yourlocation")
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    private fun dispatchTakePictureIntent(): Uri {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                     uri = FileProvider.getUriForFile(
                        activity!!,
                        "com.aptivist.livesapp.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
                }
            }
        }
        return uri
    }






    fun crearArchivoImagen(): File {
        val timeStamp =  SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val nombre ="JPEG_" + timeStamp + "_"
        val directorio = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imagen  = File.createTempFile(nombre,".jpg", directorio)
        url = "file://" + imagen.absolutePath
        return imagen
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(imageBitmap)
            txtPicturePreview.text = "Preview"
            var dt = (data)
            if(dt!=null)
            {
                uri = data?.data!!

                AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
                    .setTitle("Preview")
                    .setView(R.layout.picture_preview)



                    .create().show()
                Picasso.get().load(data.data).into(imgNewIncidencePreview)

            }
        }else{
            txtPicturePreview.text = "No image"
        }
    }
*/
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            context?.sendBroadcast(mediaScanIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data!=null)
        {
            when(requestCode){
                REQUEST_CODE_CAMERA -> {
                    if(resultCode == RESULT_OK){
                        //Obtener imagen
                         val extras = data?.extras
                         val imageBitmap = extras!!.get("data") as Bitmap
                        //val url = Uri.parse(url)
                       // val stream = activity!!.contentResolver.openInputStream(Uri.parse(url))
                       // val imageBitMap = BitmapFactory.decodeStream(stream)


                        var f = File(url)
                        var dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
                        var inflater = this.getLayoutInflater()
                        var dialogView = inflater.inflate(R.layout.picture_preview, null);
                        var imgVw = dialogView.findViewById<ImageView>(R.id.imgPreview)
                        dialogBuilder.setView(dialogView)

                        //imgVw?.setImageBitmap(imageBitMap)
                       // var  imageContent:Uri? = data.data


                        if(f.exists()){
                            Picasso.get().load(f).into(imgVw)
                        }

                        dialogBuilder.create()
                        dialogBuilder.show()



                        /*

                       var alert = AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
                            .setTitle("Preview")
                            .setView(View.inflate(context,R.layout.picture_preview))
                            .create()
                        var imgVw = alert.findViewById<ImageView>(R.id.imgPreview)
                        Picasso.get().load(uri).into(imgVw)
                            alert.show()*/


                       /* val dialog = context?.let { Dialog(it) }
                         //dialog?.setOnDismissListener(onDismissListener)
                        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog?.setCancelable(true)
                        dialog?.setContentView(R.layout.picture_preview)

                        val imVw = dialog?.findViewById<ImageView>(R.id.imgPreview)
                        Picasso.get().load(uri).into(imVw)

                        dialog?.create()
                        dialog?.show()*/






                        //imgPreview?.setImageBitmap(imageBitMap)
                        txtPicturePreview.text = "Preview"
                    }
                    else{
                        //
                        // Se cancelo la captura
                        txtPicturePreview.text = "No img"
                    }
                }

        }

        }
    }


    fun openPicturePreview(it:View, uri: Uri?){
        //messageUser.showMessagePreview("Preview","Show image",it.context)
       /* val dialog = context?.let { Dialog(it) }
       // dialog?.setOnDismissListener(onDismissListener)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView()*/


       /* AlertDialog.Builder(ContextThemeWrapper(context,R.style.AlertDialogCustom))
            .setTitle("Preview")
            .setView(R.layout.picture_preview)
            .create().show()
        Picasso.get().load(uri).into(imgPreview)*/


        val dialog = context?.let { Dialog(it) }
        //dialog?.setOnDismissListener(onDismissListener)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.picture_preview)

        val imVw = dialog?.findViewById<ImageView>(R.id.imgPreview)
        Picasso.get().load(uri).into(imVw)

        dialog?.show()


        //messageUser.showMessagePreview("Preview","Show image",it.context)
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
