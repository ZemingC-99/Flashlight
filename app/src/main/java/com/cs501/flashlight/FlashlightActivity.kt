/**
 * References:
 * 1.https://abhiandroid.com/ui/switch
 * 2.https://stackoverflow.com/questions/73463685/gesturedetector-ongesturelistener-overridden-methods-are-not-working-in-android
 * 3.https://stackoverflow.com/questions/48552821/what-is-the-use-of-android-permission-flashlight
 * 4.https://developer.android.com/reference/androidx/core/view/GestureDetectorCompat
 * 5.https://developer.android.com/reference/android/view/GestureDetector.OnGestureListener
 * 6.https://source.android.com/docs/core/camera/system-cameras
 */
package com.cs501.flashlight

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class FlashlightActivity : AppCompatActivity() {

    private lateinit var flashlightSwitch: Switch
    private lateinit var userInputEditText: EditText
    private lateinit var submitButton: Button
    private var isFlashlightOn: Boolean = false
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashlightSwitch = findViewById(R.id.flashlightSwitch)
        userInputEditText = findViewById(R.id.userInputEditText)
        submitButton = findViewById(R.id.submitButton)

        flashlightSwitch.setOnCheckedChangeListener { _, isChecked ->
            toggleFlashlight(isChecked)
        }

        submitButton.setOnClickListener {
            val userInput = userInputEditText.text.toString().toUpperCase()
            when (userInput) {
                "ON" -> {
                    flashlightSwitch.isChecked = true
                }
                "OFF" -> {
                    flashlightSwitch.isChecked = false
                }
                else -> {
                    Toast.makeText(this, "Please enter 'ON' or 'OFF'", Toast.LENGTH_SHORT).show()
                }
            }
        }

        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 != null && e2 != null) {
                    if (e2.y - e1.y > 50) {
                        flashlightSwitch.isChecked = false
                        return true
                    } else if (e1.y - e2.y > 50) {
                        flashlightSwitch.isChecked = true
                        return true
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { gestureDetector.onTouchEvent(it) }
        return super.onTouchEvent(event)
    }

    private fun toggleFlashlight(on: Boolean) {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        try {
            cameraManager.setTorchMode(cameraId, on)
            isFlashlightOn = on
        } catch (e: CameraAccessException) {
            Toast.makeText(this, "Flashlight not available", Toast.LENGTH_SHORT).show()
        }
    }
}