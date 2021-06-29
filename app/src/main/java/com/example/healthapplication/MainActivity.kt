package com.example.healthapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity() : AppCompatActivity(),SensorEventListener, Parcelable {
    var running=false
    var sensorManager:SensorManager?=null

    constructor(parcel: Parcel) : this() {
        running = parcel.readByte() != 0.toByte()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onResume() {
        super.onResume()
        running=true
        val stepSensor=sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if(stepSensor==null)
        {
            Toast.makeText(this,"NO STEP COUNTER SENSOR!",Toast.LENGTH_SHORT).show()
        }
        else
        {
            sensorManager?.registerListener(this, stepSensor ,SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        running=false
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?)
    {
        if(running)
        {
            val steps:TextView=findViewById(R.id.StepsValue)
            if (p0 != null)
            {
                steps.text=p0.values[0].toString()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (running) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }
}