package com.ainotebook.ui.screens.note

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ainotebook.AINotebookApplication
import com.ainotebook.data.model.Note
import com.ainotebook.data.model.NoteType
import com.ainotebook.ui.viewmodel.NoteViewModel
import com.ainotebook.ui.viewmodel.NoteViewModelFactory
import com.ainotebook.util.AttachmentHandler
import com.ainotebook.util.PermissionUtil
import com.ainotebook.util.rememberAttachmentHandler
import com.ainotebook.util.rememberAttachmentLaunchers
import com.ainotebook.ui.components.TagChip
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteScreen(
    noteId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val noteViewModel: NoteViewModel = viewModel(
        factory = NoteViewModelFactory(
            (context.applicationContext as AINotebookApplication).repository
        )
    )
    
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var attachments by remember { mutableStateOf(listOf<String>()) }
    var isRecording by remember { mutableStateOf(false) }
    var tags by remember { mutableStateOf(listOf<String>()) }
    var newTag by remember { mutableStateOf("") }
    var showAddTag by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    val scope = rememberCoroutineScope()
    val attachmentHandler = rememberAttachmentHandler(context)
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            // Handle permission granted
        }
    }
    
    val (imageLauncher, fileLauncher) = rememberAttachmentLaunchers(
        onImageCaptured = { path ->
            path?.let { attachments = attachments + it }
        },
        onFileSelected = { path ->
            path?.let { attachments = attachments + it }
        }
    )
    
    // Load existing note if editing
    LaunchedEffect(noteId) {
        if (noteId != "new") {
            noteViewModel.getNoteById(noteId)?.let { note ->
                title = note.title
                content = note.content
                attachments = note.attachments
                tags = note.tags
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (noteId == "new") "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (noteId == "new") {
                                    noteViewModel.createNote(
                                        title = title,
                                        content = content,
                                        type = NoteType.TEXT,
                                        tags = tags,
                                        attachments = attachments
                                    )
                                } else {
                                    noteViewModel.getNoteById(noteId)?.let { existingNote ->
                                        noteViewModel.updateNote(
                                            existingNote.copy(
                                                title = title,
                                                content = content,
                                                tags = tags,
                                                attachments = attachments
                                            )
                                        )
                                    }
                                }
                                navController.navigateUp()
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = "Save")
                    }
                    if (noteId != "new") {
                        IconButton(
                            onClick = {
                                noteViewModel.deleteNote(noteViewModel.getNoteById(noteId)!!)
                                navController.navigateUp()
                            }
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Column {
                SmallFloatingActionButton(
                    onClick = {
                        if (!PermissionUtil.hasPermissions(context, PermissionUtil.AUDIO_PERMISSIONS)) {
                            permissionLauncher.launch(PermissionUtil.AUDIO_PERMISSIONS)
                        } else if (!isRecording) {
                            attachmentHandler.startAudioRecording()
                            isRecording = true
                        } else {
                            attachmentHandler.stopAudioRecording()?.let {
                                attachments = attachments + it
                            }
                            isRecording = false
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        if (isRecording) Icons.Filled.Stop else Icons.Filled.Mic,
                        contentDescription = if (isRecording) "Stop Recording" else "Record Audio"
                    )
                }
                SmallFloatingActionButton(
                    onClick = {
                        if (!PermissionUtil.hasPermissions(context, PermissionUtil.CAMERA_PERMISSIONS)) {
                            permissionLauncher.launch(PermissionUtil.CAMERA_PERMISSIONS)
                        } else {
                            imageLauncher.launch(null)
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.PhotoCamera, contentDescription = "Take Photo")
                }
                SmallFloatingActionButton(
                    onClick = {
                        if (!PermissionUtil.hasPermissions(context, PermissionUtil.STORAGE_PERMISSIONS)) {
                            permissionLauncher.launch(PermissionUtil.STORAGE_PERMISSIONS)
                        } else {
                            fileLauncher.launch("*/*")
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.AttachFile, contentDescription = "Attach File")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Title") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tags Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.subtitle1
                )
                IconButton(onClick = { showAddTag = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add tag")
                }
            }
            
            if (showAddTag) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = newTag,
                        onValueChange = { newTag = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Enter tag") },
                        singleLine = true,
                        onValueChange = { 
                            if (!it.contains(" ")) {
                                newTag = it
                            }
                        }
                    )
                    IconButton(
                        onClick = {
                            if (newTag.isNotEmpty() && !tags.contains(newTag)) {
                                tags = tags + newTag
                            }
                            newTag = ""
                            showAddTag = false
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Add")
                    }
                    IconButton(
                        onClick = {
                            newTag = ""
                            showAddTag = false
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Cancel")
                    }
                }
            }
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tags) { tag ->
                    TagChip(
                        tag = tag,
                        onRemove = { tags = tags - tag }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text("Start typing...") }
            )
            
            if (attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Attachments (${attachments.size})",
                    style = MaterialTheme.typography.subtitle1
                )
                AttachmentPreview(
                    attachments = attachments,
                    onRemoveAttachment = { path ->
                        attachments = attachments - path
                    }
                )
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            attachmentHandler.cleanup()
        }
    }
} 