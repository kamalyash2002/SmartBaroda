package com.uphar.smartbaroda

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun shownotification(message: String)
{
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val context = LocalContext.current
    val waterNotificationService = remember { NotificationService(context) }

    LaunchedEffect(key1 = true) {
        if (postNotificationPermission.status.isGranted) {
            waterNotificationService.showBasicNotification()
        } else {
            postNotificationPermission.launchPermissionRequest()
        }
    }

    // Show the notification as soon as permission is granted
//    LaunchedEffect(postNotificationPermission.status.isGranted) {
//        if (postNotificationPermission.status.isGranted) {
//            waterNotificationService.showExpandableNotification()
//        }
//    }
}
