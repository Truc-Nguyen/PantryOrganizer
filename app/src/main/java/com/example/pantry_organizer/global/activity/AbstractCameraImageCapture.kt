package com.example.pantry_organizer.global.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.util.*

abstract class AbstractCameraImageCapture: AbstractPantryAppActivity() {
    // Activity request codes.
    val REQUEST_PERMISSIONS = 1
    val REQUEST_CAMERA_IMAGE_CAPTURE = 2

    // Stores the path to the local file during temporary storage.
    var tempLocalAbsolutePath: String? = null

    // Stores the firebase storage filename after the image has been pushed to the cloud.
    var fbsFilename: String? = null

    // Request use of camera to take a picture.
    fun requestImageCapture() {
        // Request permissions.
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS)
        } else {
            // Create an intent to take a picture.
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent.
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go.
                    val photoFile: File? = try {
                        getTempImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    // Continue only if the File was successfully created.
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.pantry_organizer.fileprovider",
                            it
                        )
                        // Start the activity with this intent and store the photo to the temporary
                        // file through the URI.
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    // Creates a temporary image file placeholder.
    private fun getTempImageFile(): File {
        // Get the temporary local storage path.
        val filename = UUID.randomUUID().toString()
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Return the created file and store an instance of its absolute path to photoImagePath.
        return File.createTempFile(filename, ".jpg", storageDir)
            .apply {
                tempLocalAbsolutePath = absolutePath
            }
    }

    // Push the image to the cloud as well as an optional image view.
    fun pushImage(view: ImageView?) {
        // Load the view if it exists with the file image.
        view?.setImageBitmap(BitmapFactory.decodeFile(tempLocalAbsolutePath))

        // Upload the image to firebase and get the cloud filename.
        fbsFilename = viewModel.uploadFileToStorage(tempLocalAbsolutePath!!)
    }
}