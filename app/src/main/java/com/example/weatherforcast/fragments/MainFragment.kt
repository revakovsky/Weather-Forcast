package com.example.weatherforcast.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforcast.R
import com.example.weatherforcast.adapters.MyViewPagerAdapter
import com.example.weatherforcast.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

const val API_KEY = "864b68ca42224665a99104528220807"

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val listOfFragments = listOf(HoursFragment.newInstance(), DaysFragment.newInstance())
    private val listOfTabsNames = listOf("hours", "days")

    private val tabIcons = listOf(
        R.drawable.ic_baseline_access_time,
        R.drawable.ic_baseline_today
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initTabs()
        requestWeatherData("London")
    }

    fun initTabs() = with(binding) {
        val adapterViewPager = MyViewPagerAdapter(activity as FragmentActivity, listOfFragments)
        viewPager.adapter = adapterViewPager
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            //tab.text = listOfTabsNames[position]
            tab.setIcon(tabIcons[position])

        }.attach()
    }

    private fun requestWeatherData(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "5" +
                "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result ->
                Log.d("testLogs", "result: $result")
            },
            {
                error ->
                Log.d("testLogs", "error: $error")
            }
        )
        queue.add(request)
    }

    private fun permissionListener() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}