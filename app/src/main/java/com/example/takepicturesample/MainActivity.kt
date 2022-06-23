package com.example.takepicturesample

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import net.weg.wemob.commons.services.dialog.FullscreenFragmnet
import java.io.File
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var takePictureViewModel: TakePictureViewModel
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var buttonShowTags: ImageView
    private var exifData: Exif = Exif()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRequestPermissionLauncherResult()

        takePictureViewModel = ViewModelProvider(this).get(TakePictureViewModel::class.java)

        findViewById<Button>(R.id.button).setOnClickListener {
            requestPermissionToCapturePhotoIfNeeded()
            Log.d("layon.f", "onClick")
        }

        buttonShowTags = findViewById<ImageView>(R.id.imageViewExif)
        buttonShowTags.setOnClickListener {
            Log.d("layon.f", "ShowTags")
            showFullDialogInfos(
                context = this,
                title = "Image Exif",
                fragmentTransaction = supportFragmentManager.beginTransaction(),
                exifData = exifData
            )
        }

    }

    /**  This method will ask for permission if photo capture is required. */
    fun requestPermissionToCapturePhotoIfNeeded() {
        Log.d("layon.f", "requestPermissionToCapturePhotoIfNeeded()")
        if (!hasPermission(
                this,
                android.Manifest.permission.CAMERA
            )
        ) {
            requestPermissionIfNeeded(
                this,
                android.Manifest.permission.CAMERA,
                requestPermissionLauncher
            )
        } else {
            capturePhoto()
        }
    }

    /**  Open the camera to capture photo. */

    private fun capturePhoto() {
        Log.d("layon.f", "capturePhoto()")
        lifecycleScope.launchWhenStarted {
            getNewTempFileUri().let { uri ->
                Log.d("layon.f", "capturePhoto() tempUri = $uri")
                takePictureViewModel.tempUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    /**  Creates a file temporarily to save the photo in full size. */
    private fun getNewTempFileUri(): Uri {
        Log.d("layon.f", "getNewTempFileUri()")

        val path = File(this.externalCacheDir, "take_picture_files/")
        path.mkdir()

        val tmpFile =
            File.createTempFile("img", ".png", path)
                .apply {
                    createNewFile()
                    deleteOnExit()
                }

        return FileProvider.getUriForFile(
            this,
            this.applicationContext.packageName + ".provider",
            tmpFile
        )
    }

    /**  Note the change result of camera. */
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->

            if (isSuccess) {
                Log.d("layon.f", "takeImageResult isSuccess")
                Log.d("layon.f", "takeImageResult tempUri: ${takePictureViewModel.tempUri}")
                takePictureViewModel.tempUri?.let { uri ->
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        uri
                    )
                    getExifInterfaceRotation(uri)?.let { rotate ->
                        Log.d("layon.f", "need rotate to $rotate degrees")
                        var textRotate = "Need to rotate $rotate degrees"
                        exifData.IS_ROTATED = textRotate
                        val needRotate = findViewById<CheckBox>(R.id.checkBox).isChecked
                        if (needRotate) {
                            if (rotate > 0) {
                                textRotate = "It was necessary to rotate $rotate degrees"
                                exifData.IS_ROTATED = textRotate
                                Toast.makeText(
                                    applicationContext,
                                    textRotate,
                                    Toast.LENGTH_LONG
                                ).show()
                                bitmap = turnBitmap(bitmap, rotate.toFloat())
                            } else {
                                textRotate = "No need to rotate"
                                exifData.IS_ROTATED = textRotate
                                Toast.makeText(
                                    applicationContext,
                                    textRotate,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    Log.d("layon.f", "imageView() .setImageBitmap()")
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(
                        bitmap
                    )
                    buttonShowTags.visibility = View.VISIBLE
                }
            }
        }

    fun getExifInterfaceRotation(uri: Uri): Int? {
        Log.d("layon.f", "getExifInterfaceRotation(uri: $uri)")
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        try {
            val exif = inputStream?.let { ExifInterface(it) }
            Log.d("layon.f", "exif: ${exif}")
            getSomeExifTags(exif)
            val TAG_ORIENTATION = exif?.getAttribute(ExifInterface.TAG_ORIENTATION)
            Log.d("layon.f", "ExifInterface.TAG_ORIENTATION: $TAG_ORIENTATION")
            // Constants used for the Orientation Exif tag in ExifInterface.java line 503.
            var orientation: Int? = when (TAG_ORIENTATION?.toInt()) {
                ExifInterface.ORIENTATION_UNDEFINED -> null
                ExifInterface.ORIENTATION_NORMAL -> null
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> null // left right reversed mirror
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> null
                ExifInterface.ORIENTATION_TRANSPOSE -> null
                ExifInterface.ORIENTATION_ROTATE_90 -> 90 // rotate 90 cw to right it
                ExifInterface.ORIENTATION_TRANSVERSE -> null
                ExifInterface.ORIENTATION_ROTATE_270 -> 270 // rotate 270 to right it
                else -> null
            }
            Log.d("layon.f", "getExifInterfaceRotation() return $orientation")
            return orientation
        } catch (e: IOException) {
            // handler
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {

                }
            }
        }
        return null
    }

    fun getSomeExifTags(exif: ExifInterface?) {
        exif?.let {
            exifData.apply {
                TAG_DATETIME = it.getAttribute(ExifInterface.TAG_DATETIME)
                TAG_DATETIME_ORIGINAL = it.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                TAG_IMAGE_DESCRIPTION = it.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION)
                TAG_IMAGE_LENGTH = it.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)
                TAG_IMAGE_WIDTH = it.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)
                TAG_JPEG_INTERCHANGE_FORMAT =
                    it.getAttribute(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)
                TAG_ORIENTATION = it.getAttribute(ExifInterface.TAG_ORIENTATION)
                TAG_REFERENCE_BLACK_WHITE = it.getAttribute(ExifInterface.TAG_REFERENCE_BLACK_WHITE)
                TAG_RESOLUTION_UNIT = it.getAttribute(ExifInterface.TAG_RESOLUTION_UNIT)
                TAG_SOFTWARE = it.getAttribute(ExifInterface.TAG_SOFTWARE)
                TAG_BRIGHTNESS_VALUE = it.getAttribute(ExifInterface.TAG_BRIGHTNESS_VALUE)
                TAG_CONTRAST = it.getAttribute(ExifInterface.TAG_CONTRAST)
                TAG_DEVICE_SETTING_DESCRIPTION =
                    it.getAttribute(ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION)
                TAG_DIGITAL_ZOOM_RATIO = it.getAttribute(ExifInterface.TAG_DIGITAL_ZOOM_RATIO)
                TAG_EXIF_VERSION = it.getAttribute(ExifInterface.TAG_EXIF_VERSION)
                TAG_EXPOSURE_PROGRAM = it.getAttribute(ExifInterface.TAG_EXPOSURE_PROGRAM)
                TAG_FLASH = it.getAttribute(ExifInterface.TAG_FLASH)
                TAG_FLASH_ENERGY = it.getAttribute(ExifInterface.TAG_FLASH_ENERGY)
                TAG_FOCAL_LENGTH = it.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
                TAG_SATURATION = it.getAttribute(ExifInterface.TAG_SATURATION)
                TAG_SCENE_CAPTURE_TYPE = it.getAttribute(ExifInterface.TAG_SCENE_CAPTURE_TYPE)
                TAG_WHITE_BALANCE = it.getAttribute(ExifInterface.TAG_WHITE_BALANCE)
                TAG_GPS_ALTITUDE = it.getAttribute(ExifInterface.TAG_GPS_ALTITUDE)
                TAG_GPS_AREA_INFORMATION = it.getAttribute(ExifInterface.TAG_GPS_AREA_INFORMATION)
                TAG_GPS_IMG_DIRECTION = it.getAttribute(ExifInterface.TAG_GPS_IMG_DIRECTION)
                TAG_GPS_LATITUDE = it.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                TAG_GPS_LONGITUDE = it.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                TAG_GPS_SPEED = it.getAttribute(ExifInterface.TAG_GPS_SPEED)
                TAG_THUMBNAIL_IMAGE_LENGTH =
                    it.getAttribute(ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH)
                TAG_THUMBNAIL_IMAGE_WIDTH = it.getAttribute(ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH)
                //TAG_THUMBNAIL_ORIENTATION = it.getAttribute(ExifInterface.TAG_THUMBNAIL_ORIENTATION)
            }
        }
    }

    fun turnBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        Log.d("layon.f", "turnBitmap() degrees: $degrees")
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    /** This method will create an object that will receive the user request permission result. */
    fun setupRequestPermissionLauncherResult() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    capturePhoto()
                }
            }
    }

    companion object {
        /**
         * Function to check if given permission is granted or not
         */
        fun hasPermission(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * Function to request the permission if needed
         */
        fun requestPermissionIfNeeded(
            context: Context,
            permission: String,
            requestPermissionLauncher: ActivityResultLauncher<String>
        ) {
            if (!hasPermission(context, permission)) {
                // The permission callback will be called inside requestPermissionLauncher object:
                requestPermissionLauncher.launch(permission)
            }
        }

        /**
         * This method will setup and show a dialog full screen for error handling.
         */
        fun showFullDialogInfos(
            context: Context,
            title: String,
            fragmentTransaction: FragmentTransaction,
            exifData: Exif
        ) {
            val dialog = FullscreenFragmnet(exifData)
            dialog.show(fragmentTransaction, null)
        }
    }
}