package com.aptivist.livesapp.ui.home

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController

import com.aptivist.livesapp.R
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.helpers.Constants.Companion.AUTOCOMPLETE_REQUEST_CODE
import com.aptivist.livesapp.helpers.Constants.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(),OnMapReadyCallback,SearchView.OnQueryTextListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    lateinit var geocoder:Geocoder
    lateinit var places:PlacesClient
    private val messagesUser: IMessagesDialogs by inject()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var LatLong: LatLng
    var execution:Boolean = false

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        geocoder = Geocoder(activity!!, Locale.getDefault())
        Places.initialize(activity!!,getString(R.string.google_api_key))
        places = Places.createClient(activity!!)
        fusedLocationClient = FusedLocationProviderClient(activity!!)
        val mapFragment = childFragmentManager?.findFragmentById(R.id.gmPrincipal) as SupportMapFragment
        if (mapFragment is SupportMapFragment) {
            mapFragment.getMapAsync(this)
        }
        //root.svLocationMap.setOnQueryTextListener(this)

        //geocoder = Geocoder(activity!!, Locale.getDefault())
        /* mapFragment = (childFragmentManager.findFragmentById(R.id.gmPrincipal) as? SupportMapFragment)!!
        if (mapFragment is SupportMapFragment) {
            mapFragment?.getMapAsync(this) }*/
       // val searchView = childFragmentManager?.findFragmentById(R.id.svLocationMap) as SupportMapFragment
         root.svLocationMap.setOnQueryTextListener(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController: NavController = view.findNavController()
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            navController.navigate(R.id.goToIncidenceFragment)
        }


        //mapFragment?.getMapAsync(this)
    }

    /*override fun onMapReady(googleMap: GoogleMap) {

        googleMap.let { pMap->
            pMap.setOnPoiClickListener { pointOfInterest ->
                val request = FetchPlaceRequest.newInstance(pointOfInterest.placeId, listOf(Place.Field.ID,Place.Field.NAME,Place.Field.ADDRESS))
                places.fetchPlace(request).addOnSuccessListener { respose->
                    messagesUser.showToast(activity!!,respose.place.address!!)
                }.addOnFailureListener {ex->
                    messagesUser.showToast(activity!!,ex.message!!)
                }
            }
        }

        googleMap.let { gMap->

            gMap.uiSettings.isZoomControlsEnabled = true
            /val sydney = LatLng(-34.0, 151.0)
            gMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))

            gMap.setOnMarkerClickListener { mySpot->
                val info = geocoder.getFromLocation(mySpot.position.latitude,mySpot.position.longitude,1)
                Toast.makeText(activity,info[0].countryName,Toast.LENGTH_SHORT).show()
                true
            }
            gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
        googleMap.uiSettings.isZoomControlsEnabled = true

    }*/

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //permissions
        if(ActivityCompat.checkSelfPermission(activity!!,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           return
        }
        setUpGoogleMap(googleMap)
    }

    fun setUpGoogleMap(googleMap: GoogleMap){

        if (ActivityCompat.checkSelfPermission(activity!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.let { pMap->
            pMap.setOnPoiClickListener { pointOfInterest ->
                val request = FetchPlaceRequest.newInstance(pointOfInterest.placeId, listOf(Place.Field.ID,Place.Field.NAME,Place.Field.ADDRESS))
                places.fetchPlace(request).addOnSuccessListener { respose->
                    messagesUser.showToast(activity!!,respose.place.address!!)
                }.addOnFailureListener {ex->
                    messagesUser.showToast(activity!!,ex.message!!)
                }
            }
        }

        googleMap.let { gMap->
            mMap.isMyLocationEnabled = true
            //mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            //mMap.getUiSettings().setMyLocationButtonEnabled(true)
            mMap.setPadding(150,500,0,500 )//left, top, right, bottom
            fusedLocationClient.lastLocation.addOnSuccessListener {location->
                if(location!=null){
                    lastLocation = location
                    val currentLatLong = LatLng(location.latitude,location.longitude)
                    placeMarker(currentLatLong)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,15f))
                }
                mMap.uiSettings.isZoomControlsEnabled = true
            }

            gMap.setOnMarkerClickListener { mySpot->
                val info = geocoder.getFromLocation(mySpot.position.latitude,mySpot.position.longitude,1)
                Toast.makeText(activity,info[0].countryName,Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    fun placeMarker(location:LatLng){
        val markerOption = MarkerOptions().position(location)
        LatLong = location
        //val markerTitle = getAddress(location)
        //markerOption.title(markerTitle)
        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mMap.addMarker(markerOption)
            //.snippet("La ubicación puede ser precisa más no exacta."))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        /*var location:String = ""
        var addressList:List<Address>? = null
        if(location!=null || !(location=="")){
            var geocoderQuery:Geocoder = Geocoder(activity!!, Locale.getDefault())
            try{
                addressList = geocoderQuery.getFromLocationName(location,1)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
            if(addressList?.size!=0){
                var address = addressList!!.get(0)
                var latLong = LatLng(address.latitude,address.longitude)
                var city = addressList.get(0).locality
                var state = addressList.get(0).adminArea
                var country = addressList.get(0).countryName
                var postalCode = addressList.get(0).postalCode
                mMap.addMarker(MarkerOptions().position(latLong).title(location))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong,15f))
            }else{
                messagesUser.showToast(activity!!,"Introduce a location more specific")
            }

        }*/
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
       /* execution = autocompleteFragment2()
        if(!execution)
        {
            autocompleteFragment2()
        }*/
    var list : List<Place.Field> = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME)
    var intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,list).build(activity!!)
    startActivityForResult(intent,100)
        Log.d("*****","onQueryTextChange")

        return false
    }

    fun autocompleteSearch(){
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,Arrays.asList(Place.Field.ID,Place.Field.NAME))
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .build(activity!!)
        startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE)


        /*var intent:Intent=Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,Arrays.asList(Place.Field.ID,Place.Field.NAME))
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setLocationBias(RectangularBounds.newInstance(LatLng(-345.7,-3663.6),LatLng(-3467.34,-678.65)))
            //.setCountries()
            .build(activity!!)
        startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE)*/
    }


    fun search(addresses:List<Address>) {

    var address:Address = addresses.get(0);
    var home_long = address.getLongitude();
    var home_lat = address.getLatitude();
    var latLng:LatLng = LatLng(address.getLatitude(), address.getLongitude());

    //var addressText = String.format("%s, %s", address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "", address.getCountryName())

        mMap.addMarker(MarkerOptions().position(latLng))
        mMap.clear()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        /*
        svLocationMap.inputType("Latitude:" + address.getLatitude() + ", Longitude:"
            + address.getLongitude());*/
        messagesUser.showToast(activity!!,"Latitude:" + address.getLatitude() + ", Longitude:"
                + address.getLongitude())


}


   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            val place = Autocomplete.getPlaceFromIntent(data)
                            messagesUser.showToast(activity!!, "Place: ${place.name}, ${place.id}")
                        }!!
                    }
                    AutocompleteActivity.RESULT_ERROR -> {
                        // TODO: Handle the error.
                        data?.let {
                            val status = Autocomplete.getStatusFromIntent(data)
                            messagesUser.showToast(activity!!, status.statusMessage.toString())
                        }!!
                    }
                    Activity.RESULT_CANCELED -> {
                        // The user canceled the operation.
                    }
                }
                return
            }
        }
    }*/

    private fun getAddress(latLong:LatLng): String? {
        val geocoder = Geocoder(activity!!)
        val list = geocoder.getFromLocation(latLong.latitude,latLong.longitude,1)
        return list[0].getAddressLine(0)
    }











fun autocompleteFragment2():Boolean{
        //getContext()?.let { Places.initialize(it,"AIzaSyAvUU2mIl4YNXrhgQAcSe9ouKEENdOxMxg") }
        var list : List<Place.Field> = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME)
        var intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,list).build(activity!!)
        startActivityForResult(intent,100)
    return true
}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("*****","onActivityResult")
        var place:Place? = null
        if(requestCode==100 && resultCode == RESULT_OK){
            place = data?.let { Autocomplete.getPlaceFromIntent(it) }
            mMap.addMarker(place?.latLng?.let { MarkerOptions().position(it).title(place.address) })
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place!!.latLng,15f))

            messagesUser.showToast(activity!!,String.format("Location name ${place?.name}"))

            svLocationMap.setQuery(place.address,false)

            execution = true

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Log.d("*****","resultCode")
            var status: Status? = data?.let { Autocomplete.getStatusFromIntent(it) }
            status?.statusMessage?.let { messagesUser.showToast(activity!!, it) }
        }
    }



}
