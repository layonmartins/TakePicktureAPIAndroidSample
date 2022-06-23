package net.weg.wemob.commons.services.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.takepicturesample.Exif
import com.example.takepicturesample.R
import com.example.takepicturesample.databinding.FragmentFullscreenBinding


class FullscreenFragmnet(val exif: Exif) : DialogFragment() {

    private var _binding: FragmentFullscreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding(inflater, container)
    }

    fun setBinding(binding: FragmentFullscreenBinding?) {
        _binding = binding
    }

    fun binding(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.apply {
            textView.text =
                "ORIENTATION INFOS: \n" +
                        "\n" + (exif.IS_ROTATED ?: "") +
                        "\nTAG_ORIENTATION: " + (exif.TAG_ORIENTATION ?: "null") +
                        "\n// Constants used for the Orientation Exif tag." +
                        "\npublic static final int ORIENTATION_UNDEFINED = 0;" +
                        "\npublic static final int ORIENTATION_NORMAL = 1;" +
                        "\npublic static final int ORIENTATION_FLIP_HORIZONTAL = 2;  // left right reversed mirror" +
                        "\npublic static final int ORIENTATION_ROTATE_180 = 3;" +
                        "\npublic static final int ORIENTATION_FLIP_VERTICAL = 4;  // upside down mirror" +
                        "\n// flipped about top-left <--> bottom-right axis" +
                        "\npublic static final int ORIENTATION_TRANSPOSE = 5;" +
                        "\npublic static final int ORIENTATION_ROTATE_90 = 6;  // rotate 90 cw to right it" +
                        "\n// flipped about top-right <--> bottom-left axis" +
                        "\npublic static final int ORIENTATION_TRANSVERSE = 7;" +
                        "\npublic static final int ORIENTATION_ROTATE_270 = 8;  // rotate 270 to right it" +
                        "\n// Constants used for white balance" +
                        "\npublic static final int WHITEBALANCE_AUTO = 0;" +
                        "\npublic static final int WHITEBALANCE_MANUAL = 1;" +

                        "\n\nEXIF TAGS INFOS: \n" +
                        "TAG_DATETIME: " + (exif.TAG_DATETIME ?: "null") +
                        "\nTAG_IMAGE_DESCRIPTION: " + (exif.TAG_IMAGE_DESCRIPTION ?: "null") +
                        "\nTAG_IMAGE_LENGTH: " + (exif.TAG_IMAGE_LENGTH ?: "null") +
                        "\nTAG_IMAGE_WIDTH: " + (exif.TAG_IMAGE_WIDTH ?: "null") +
                        "\nTAG_JPEG_INTERCHANGE_FORMAT: " + (exif.TAG_JPEG_INTERCHANGE_FORMAT
                    ?: "null") +
                        "\nTAG_REFERENCE_BLACK_WHITE: " + (exif.TAG_REFERENCE_BLACK_WHITE
                    ?: "null") +
                        "\nTAG_RESOLUTION_UNIT: " + (exif.TAG_RESOLUTION_UNIT ?: "null") +
                        "\nTAG_SOFTWARE: " + (exif.TAG_SOFTWARE ?: "null") +
                        "\nTAG_BRIGHTNESS_VALUE: " + (exif.TAG_BRIGHTNESS_VALUE ?: "null") +
                        "\nTAG_CONTRAST: " + (exif.TAG_CONTRAST ?: "null") +
                        "\nTAG_DEVICE_SETTING_DESCRIPTION: " + (exif.TAG_DEVICE_SETTING_DESCRIPTION
                    ?: "null") +
                        "\nTAG_DIGITAL_ZOOM_RATIO: " + (exif.TAG_DIGITAL_ZOOM_RATIO ?: "null") +
                        "\nTAG_EXIF_VERSION: " + (exif.TAG_EXIF_VERSION ?: "null") +
                        "\nTAG_EXPOSURE_PROGRAM: " + (exif.TAG_EXPOSURE_PROGRAM ?: "null") +
                        "\nTAG_FLASH: " + (exif.TAG_FLASH ?: "null") +
                        "\nTAG_FLASH_ENERGY: " + (exif.TAG_FLASH_ENERGY ?: "null") +
                        "\nTAG_FOCAL_LENGTH: " + (exif.TAG_FOCAL_LENGTH ?: "null") +
                        "\nTAG_SATURATION: " + (exif.TAG_SATURATION ?: "null") +
                        "\nTAG_SCENE_CAPTURE_TYPE: " + (exif.TAG_SCENE_CAPTURE_TYPE ?: "null") +
                        "\nTAG_WHITE_BALANCE: " + (exif.TAG_WHITE_BALANCE ?: "null") +
                        "\nTAG_GPS_ALTITUDE: " + (exif.TAG_GPS_ALTITUDE ?: "null") +
                        "\nTAG_GPS_AREA_INFORMATION: " + (exif.TAG_GPS_AREA_INFORMATION ?: "null") +
                        "\nTAG_GPS_IMG_DIRECTION: " + (exif.TAG_GPS_IMG_DIRECTION ?: "null") +
                        "\nTAG_GPS_LATITUDE: " + (exif.TAG_GPS_LATITUDE ?: "null") +
                        "\nTAG_GPS_LONGITUDE: " + (exif.TAG_GPS_LONGITUDE ?: "null") +
                        "\nTAG_GPS_SPEED: " + (exif.TAG_GPS_SPEED ?: "null") +
                        "\nTAG_THUMBNAIL_IMAGE_LENGTH: " + (exif.TAG_THUMBNAIL_IMAGE_LENGTH
                    ?: "null") +
                        "\nTAG_THUMBNAIL_IMAGE_WIDTH: " + (exif.TAG_THUMBNAIL_IMAGE_WIDTH
                    ?: "null") +
                        "\nTAG_THUMBNAIL_ORIENTATION: " + (exif.TAG_THUMBNAIL_ORIENTATION ?: "null")
        }
    }
}