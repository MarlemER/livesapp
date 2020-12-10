package com.aptivist.livesapp.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController

import com.aptivist.livesapp.R
import com.aptivist.livesapp.di.interfaces.IMessagesDialogs
import com.aptivist.livesapp.helpers.Constants.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.aptivist.livesapp.helpers.OnBackClickListener
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
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.android.ext.android.inject
import java.util.*

class HomeFragment : Fragment(),OnMapReadyCallback,SearchView.OnQueryTextListener, GoogleMap.OnMapClickListener,
    OnBackClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    lateinit var geocoder:Geocoder
    lateinit var places:PlacesClient
    private val messagesUser: IMessagesDialogs by inject()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var LatLong: LatLng
    private var flagSearchLocation:Boolean = false
    private lateinit var navController: NavController

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
            root.svLocationMap.setOnQueryTextListener(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
            navController.navigate(R.id.goToIncidenceFragment)
        }
        //mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //permissions
        if(ActivityCompat.checkSelfPermission(activity!!,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           return
        }

       /* mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title(LatLong.toString()))
            messagesUser.showMessageLocationGM("Add location","Send the selected location","OK",R.drawable.ic_error,activity!!,view!!,"${it.longitude},${it.latitude}".toString())
            //txtLocationPreview.text = "holaaa"
        }*/
        setUpGoogleMap(googleMap)
        mMap.setOnMapClickListener(this)
    }



    private fun setUpGoogleMap(googleMap: GoogleMap){

        if (ActivityCompat.checkSelfPermission(activity!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

      /*  mMap.let { pMap->
            pMap.setOnPoiClickListener { pointOfInterest ->
                val request = FetchPlaceRequest.newInstance(pointOfInterest.placeId, listOf(Place.Field.ID,Place.Field.NAME,Place.Field.ADDRESS))
                places.fetchPlace(request).addOnSuccessListener { respose->
                    messagesUser.showToast(activity!!,respose.place.address!!)
                }.addOnFailureListener {ex->
                    messagesUser.showToast(activity!!,ex.message!!)
                }
            }
        }*/

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

            /*gMap.setOnMarkerClickListener { mySpot->
                val info = geocoder.getFromLocation(mySpot.position.latitude,mySpot.position.longitude,1)
                Toast.makeText(activity,info[0].countryName,Toast.LENGTH_SHORT).show()
                true
            }*/
        }
    }

    private fun placeMarker(location:LatLng){
        val markerOption = MarkerOptions().position(location)
        LatLong = location
        //val markerTitle = getAddress(location)
        //markerOption.title(markerTitle)
        //mMap.addMarker(markerOption)
        val locationMarker = Geocoder(activity!!).getFromLocation(location.latitude,location.longitude,1)
        var addressCurrently = locationMarker[0].getAddressLine(0)
        mMap.addMarker(MarkerOptions().position(location).title(addressCurrently.toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
            //.snippet("La ubicación puede ser precisa más no exacta."))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        return false
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onQueryTextChange(locationString: String?): Boolean {
        if(!flagSearchLocation && !locationString.isNullOrEmpty())
        {
            flagSearchLocation = true
            var list : List<Place.Field> = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME)
            var intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,list).setInitialQuery(locationString).build(activity!!)
            startActivityForResult(intent,100)
            Log.d("*****","onQueryTextChange")
        }
        return false
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

        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Log.d("*****","resultCode")
            var status: Status? = data?.let { Autocomplete.getStatusFromIntent(it) }
            status?.statusMessage?.let { messagesUser.showToast(activity!!, it) }
        }
        flagSearchLocation = false
    }

    override fun onMapClick(place: LatLng) {
        val locationMarker = Geocoder(activity!!).getFromLocation(place.latitude,place.longitude,1)
        var addressCurrently = locationMarker[0].getAddressLine(0)
        //Log.d("*****",addressCurrently.toString())
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(place).title(addressCurrently.toString()))
        messagesUser.showMessageLocationGM("Are you sure to add the current bookmark?","$addressCurrently","OK",R.drawable.ic_error,activity!!,view!!,"$addressCurrently".toString(),place.longitude,place.latitude)
        }

    override fun onBackClick(): Boolean {
        return !flagSearchLocation
        Log.d("***",flagSearchLocation.toString())
    }

    /* override fun onBackPressed(): Boolean {
         if (searchBar.focusedChild == null) {
             updateFocus(FocusSearchAction.BACK_PRESSED)
             return true
         }
         return false
     }*/

}

