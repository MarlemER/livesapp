package com.aptivist.livesapp.helpers

class Constants {
    companion object{
        var TAG = "FirebaseAuthAppTag"
        var RC_SIGN_IN = 123
        var USER = "user"
        var USERS = "users"
        var PHOTO_USER_DEFAULT = "https://img.pngio.com/user-account-management-logo-user-icon-png-600x600-png-user-logo-png-820_641.png"
        val SHARE_PREFERENCES_MAIN:String="mySharedPreferences"
        val SHAREPREF_TOKEN_FACEBOOK:String="keyFacebook"
        val SHAREPREF_EMAILUSER_FIREBASE:String="emailFirebase"
        val SHAREPREF_PASSUSER_FIREBASE:String="passFirebase"
        val REQUEST_CODE_CAMERA = 10
        val REQUEST_PERMISSION_CAMERA = 11
        val PICTURE_NAME = "LIVESAPP_PICTURE"
        val LOCATION_PERMISSION_REQUEST_CODE = 1
        val AUTOCOMPLETE_REQUEST_CODE = 1
        val UPLOAD_IMAGE_INCIDENCE = 1
        val INCIDENCE_ENTITY_DB = "Incidence"
        val WIDTH_PICTURE_NEW_INCIDENCE = 800
        val HEIGHT_PICTURE_NEW_INCIDENCE = 1500
    }
}