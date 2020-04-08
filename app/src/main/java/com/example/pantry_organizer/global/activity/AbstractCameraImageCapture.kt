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
import java.io.FileInputStream
import java.io.IOException
import java.util.*

abstract class AbstractCameraImageCapture: AbstractPantryAppActivity() {
    // Activity request codes.
    val REQUEST_PERMISSIONS = 1
    val REQUEST_CAMERA_IMAGE_CAPTURE = 2

    // Stores the path to the local file during temporary storage.
    // Stores the firebase storage filename after the image has been pushed to the cloud.
    var photoImagePath: String? = null

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
                photoImagePath = absolutePath
            }
    }

    // Push the image to the cloud as well as an optional image view.
    fun pushImage(view: ImageView?) {
        // Load the view with the file image.
        view?.setImageBitmap(BitmapFactory.decodeFile(photoImagePath))

        // Define the file stream from the locally stored image file.
        val stream = FileInputStream(File(photoImagePath!!))

        // Update photoImagePath to a random UUID to store in the cloud.
        photoImagePath = "${UUID.randomUUID()}.jpg"

        // Push the image to firebase storage.
        fbs.child(photoImagePath!!).putStream(stream)
    }

//    // todo this is reference code for pulling a picture into firebase storage
//    val imageRef = fbs.child("e9733fe0-9817-4906-90f4-d3d6e9f6162a.jpg")
//
//    imageRef.downloadUrl.addOnSuccessListener {
//        Picasso.get()
//            .load(it)
//            .transform(CropSquareTransformation())
//            .transform(RoundedCornersTransformation(10, 0))
//            .placeholder(R.drawable.loading_icon).into(customFood_imageView)
//    }.addOnFailureListener {
//        customFood_imageView.setImageResource(R.drawable.no_image_icon)
//    }
}