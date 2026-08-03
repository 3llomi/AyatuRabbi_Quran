package com.devlomi.shared.ui.download

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.app_icon
import com.devlomi.shared.data.network.DownloadingResource
import com.devlomi.shared.ui.suras.DialogActions
import org.jetbrains.compose.resources.painterResource

@Composable
fun DownloadScreen(
    state: DownloadScreenState,
    onEvent: (DownloadEvents) -> Unit,
) {


    if (state.showConfirmDownloadDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Download required files") },
            text = { Text("Required Quran files must be downloaded before continuing.") },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.StartDownloadAction(DialogActions.OnConfirm))
                }) { Text("Download") }
            },
            dismissButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.StartDownloadAction(DialogActions.OnDismiss))
                }) { Text("Cancel") }
            }
        )
    }

    if (state.showConfirmCancelDownloadDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Cancel") },
            text = { Text("Are you sure you want to cancel") },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.CancelDownloadAction(DialogActions.OnConfirm))
                }) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.CancelDownloadAction(DialogActions.OnDismiss))
                }) { Text("Cancel") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(Res.drawable.app_icon),
            modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally),
            contentDescription = null
        )


        when (state.downlaodState) {
            is DownloadingResource.Loading -> {
                Text(
                    text = "downloadingText",
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LinearProgressIndicator(
                    progress = { state.downlaodState.progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                Button(
                    onClick = {
                        onEvent(DownloadEvents.OnCancel)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }

            }

            is DownloadingResource.Error -> {
                Text("Download Error")
                //retry button
                Button(
                    onClick = {
                        onEvent(DownloadEvents.OnStartDownload)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Retry")
                }
            }

            else -> {
                //No op
            }
        }
    }
}

@Composable
@Preview
fun DownloadScreenPreview(
) {

    DownloadScreen(
        state = DownloadScreenState(
            downlaodState = DownloadingResource.Loading(50),
            showConfirmDownloadDialog = true
        ),
        onEvent = {}
    )
}