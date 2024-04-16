package com.example.fcppushnotificationshttpv1.photo_compression.presentation.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.example.fcppushnotificationshttpv1.photo_compression.data.PhotoCompressionWorker
import com.example.fcppushnotificationshttpv1.photo_compression.presentation.PhotoCompressionViewModel

@Composable
fun PhotoCompressionScreen(
    uri: String,
    workManager: WorkManager,
    modifier: Modifier = Modifier,
    photoCompressionViewModel: PhotoCompressionViewModel = viewModel()
) {
    val workerResult = photoCompressionViewModel.workId?.let { id ->
        workManager.getWorkInfoByIdLiveData(id).observeAsState().value
    }

    LaunchedEffect(key1 = true) {
        photoCompressionViewModel.updateUncompressUri(uri)

        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>()
            .setInputData(
                workDataOf(
                    PhotoCompressionWorker.KEY_CONTENT_URI to uri,
                    PhotoCompressionWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 20L
                )
            )
            .setConstraints(
                Constraints(
                    requiresStorageNotLow = true
                )
            )
            .build()

        photoCompressionViewModel.updateWorkId(request.id)
        workManager.enqueue(request)
    }

    LaunchedEffect(key1 = workerResult?.outputData) {
        if (workerResult?.outputData != null) {
            val filePath = workerResult.outputData.getString(PhotoCompressionWorker.KEY_RESULT_PATH)
            filePath?.let {
                val bitmap = BitmapFactory.decodeFile(it)
                photoCompressionViewModel.updateCompressedBitmap(bitmap)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        photoCompressionViewModel.uncompressedUri?.let {
            val uncompressedUri = Uri.parse(it)
            Text(text = "Uncompressed Photo:")
            AsyncImage(model = uncompressedUri, contentDescription = null)
        }
        Spacer(modifier = Modifier.height(16.dp))
        photoCompressionViewModel.compressedBitmap?.let {
            Text(text = "Compressed Photo:")
            Image(bitmap = it.asImageBitmap(), contentDescription = null)
        }
    }
}