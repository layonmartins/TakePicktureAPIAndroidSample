package com.example.takepicturesample

data class Exif(
    var IS_ROTATED: String? = "No need to rotate",
    var TAG_DATETIME: String? = null,
    var TAG_DATETIME_ORIGINAL: String? = null,
    var TAG_IMAGE_DESCRIPTION: String? = null,
    var TAG_IMAGE_LENGTH: String? = null,
    var TAG_IMAGE_WIDTH: String? = null,
    var TAG_JPEG_INTERCHANGE_FORMAT: String? = null,
    var TAG_ORIENTATION: String? = null,
    var TAG_REFERENCE_BLACK_WHITE: String? = null,
    var TAG_RESOLUTION_UNIT: String? = null,
    var TAG_SOFTWARE: String? = null,
    var TAG_BRIGHTNESS_VALUE: String? = null,
    var TAG_CONTRAST: String? = null,
    var TAG_DEVICE_SETTING_DESCRIPTION: String? = null,
    var TAG_DIGITAL_ZOOM_RATIO: String? = null,
    var TAG_EXIF_VERSION: String? = null,
    var TAG_EXPOSURE_PROGRAM: String? = null,
    var TAG_FLASH: String? = null,
    var TAG_FLASH_ENERGY: String? = null,
    var TAG_FOCAL_LENGTH: String? = null,
    var TAG_SATURATION: String? = null,
    var TAG_SCENE_CAPTURE_TYPE: String? = null,
    var TAG_WHITE_BALANCE: String? = null,
    var TAG_GPS_ALTITUDE: String? = null,
    var TAG_GPS_AREA_INFORMATION: String? = null,
    var TAG_GPS_IMG_DIRECTION: String? = null,
    var TAG_GPS_LATITUDE: String? = null,
    var TAG_GPS_LONGITUDE: String? = null,
    var TAG_GPS_SPEED: String? = null,
    var TAG_THUMBNAIL_IMAGE_LENGTH: String? = null,
    var TAG_THUMBNAIL_IMAGE_WIDTH: String? = null,
    var TAG_THUMBNAIL_ORIENTATION: String? = null,
)