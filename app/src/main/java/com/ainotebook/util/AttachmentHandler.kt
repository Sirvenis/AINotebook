package com.ainotebook.util

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import java.io.File

class AttachmentHandler(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    
    fun startAudioRecording() {
        audioFile = FileUtil.createAudioFile(context)
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile?.absolutePath)
            prepare()
            start()
        }
    }
    
    fun stopAudioRecording(): String? {
        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            audioFile?.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun handleImageCapture(uri: Uri?): String? {
        return uri?.let { FileUtil.getFileFromUri(context, it)?.absolutePath }
    }
    
    fun handleFileSelection(uri: Uri?): String? {
        return uri?.let { FileUtil.getFileFromUri(context, it)?.absolutePath }
    }
    
    fun cleanup() {
        mediaRecorder?.release()
        mediaRecorder = null
    }
}

@Composable
fun rememberAttachmentHandler(context: Context): AttachmentHandler {
    return remember { AttachmentHandler(context) }
}

@Composable
fun rememberAttachmentLaunchers(
    onImageCaptured: (String?) -> Unit,
    onFileSelected: (String?) -> Unit
): Pair<ManagedActivityResultLauncher<Uri, Boolean>, ManagedActivityResultLauncher<String, Uri?>> {
    val context = rememberCoroutineScope()
    val handler = rememberAttachmentHandler(context as Context)
    
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Handle the captured image
            onImageCaptured(handler.handleImageCapture(/* uri from camera */))
        }
    }
    
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // Handle the selected file
        onFileSelected(handler.handleFileSelection(uri))
    }
    
    return Pair(imageLauncher, fileLauncher)
} 