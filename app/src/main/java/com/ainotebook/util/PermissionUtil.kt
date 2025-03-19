package com.ainotebook.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtil {
    val CAMERA_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )
    
    val AUDIO_PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    
    val STORAGE_PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
} 