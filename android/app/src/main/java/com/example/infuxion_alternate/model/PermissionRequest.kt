package com.example.infuxion_alternate.model

data class PermissionRequest(
    val tempId: String?,
    val imei: String?,
    val isLocationGranted: Boolean?,
    val isContactsGranted: Boolean?,
    val isReadSmsGranted: Boolean?,
    val isAudioGranted: Boolean?,
    val isCameraGranted: Boolean?,
    val PP: Boolean?,
    val TNC: Boolean?,
    val contacts :  List<ContactModel>?,
    val financialSMS: List<SmsModel>?,
    val financialAppsInstalled: List<AppModel>?,
)
