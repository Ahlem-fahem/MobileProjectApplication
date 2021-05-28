package com.example.mobileprojectapplication

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mobileprojectapplication.data.localdb.RoomLocalDb
import com.example.mobileprojectapplication.models.City
import com.example.mobileprojectapplication.ui.home.adapter.CityListAdapter
import com.example.mobileprojectapplication.ui.splashscreen.SplashScreenActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_add_custom_view.view.*
import kotlinx.android.synthetic.main.nav_drawer_list_item.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(),LocationListener {
    private lateinit var roomDataBaseInstance: RoomLocalDb
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val locationPermissionCode = 2
    private lateinit var locationManager: LocationManager
    private val citiesRvAdapter : CityListAdapter by lazy {
        CityListAdapter(listOf())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roomDataBaseInstance = RoomLocalDb.getInstance(this)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //init drawer view (for example user mail adress text) and logout
        initDrawerHeader()
        //list of cities
        recyclerViewSetup()
        //get current user location to display default weather city
        getLocation()

    }
    private fun initDrawerHeader(){
        firebaseAuth.currentUser?.let { user ->
            useEmailTv.text = user.email
            logoutButton.setOnClickListener {
                firebaseAuth.signOut()
                roomDataBaseInstance.citiesDao().deleteAll()
                val intent = Intent(this,SplashScreenActivity::class.java)
                startActivity(intent)
            }
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    @ExperimentalCoroutinesApi
    fun recyclerViewSetup(){
        addNewCityButton.setOnClickListener {
            addNewCityAlert()
        }
        citiesList.adapter = citiesRvAdapter
        citiesRvAdapter.onItemCityDeleteClick = { city ->
            deleteCityAlert(city)
        }
        citiesRvAdapter.onItemClick = { city ->
            navigateToCityWeather(city)
        }
    }
    //user to delete city
    private fun deleteCityAlert(city: City) {
        val layoutInflater: LayoutInflater = LayoutInflater.from(this)
        val view: View = layoutInflater.inflate(R.layout.alert_delete_custom_view, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                roomDataBaseInstance.citiesDao().delete(city)
                updateCitiesList()
            }
            .setNegativeButton(getString(R.string.non), null)
            .show()
        val buttonPositive: Button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        buttonPositive.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
    }

    //used to add city
    private fun addNewCityAlert() {
        val layoutInflater: LayoutInflater = LayoutInflater.from(this)
        val view: View = layoutInflater.inflate(R.layout.alert_add_custom_view, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(view)
            .show()
       view.apply {
           cancelButton.setOnClickListener {
               alertDialog.dismiss()
           }
           addButton.setOnClickListener {
               if (cityNameEditText.text.toString().isNotEmpty()){
                   val city = City(cityNameEditText.text.toString(),true)
                   roomDataBaseInstance.citiesDao().insert(city)
                   updateCitiesList()
                   alertDialog.dismiss()
               } else {
                   cityNameErrorText.visibility = View.VISIBLE
               }

           }
           cityNameEditText.doOnTextChanged { _, _, _, _ ->
               if (cityNameEditText.text.toString().isNotEmpty()){
                   cityNameErrorText.visibility = View.GONE
               } else {
                   cityNameErrorText.visibility = View.VISIBLE
               }
           }

       }
    }

    private fun getLocation() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    permission.ACCESS_COARSE_LOCATION,
                    permission.ACCESS_FINE_LOCATION
                ),
                locationPermissionCode
            )
        } else {
            val bestLocation: Location? = getUserLocation()
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geoCoder.getFromLocation(bestLocation?.latitude!!, bestLocation?.longitude, 1)
            val cityName: String = addresses[0].locality.toString()

            val city = City(cityName,true)
            initCurrentCityItem(city)
            updateCitiesList()

            navigateToCityWeather(city)
        }

    private fun navigateToCityWeather(city: City) {
        val bundle = bundleOf("city" to city)
        findNavController(R.id.nav_host_fragment).navigate(R.id.nav_home, bundle)
        drawer_layout.closeDrawers()
    }

    private fun initCurrentCityItem(city: City){
        actualCityItem.deleteButton.visibility = View.GONE
        actualCityItem.title.text = city.name
        actualCityItem.setOnClickListener {
            navigateToCityWeather(city)
        }
    }
    private fun updateCitiesList() {
        val cities = roomDataBaseInstance.citiesDao().getAllCities()
        (citiesList.adapter as CityListAdapter).setList(cities)
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation(): Location? {
        locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location = locationManager.getLastKnownLocation(provider)
                ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    override fun onLocationChanged(location: Location) {
        Toast.makeText(
            this,
            "Latitude: " + location.latitude + " , Longitude: " + location.longitude,
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getLocation()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}