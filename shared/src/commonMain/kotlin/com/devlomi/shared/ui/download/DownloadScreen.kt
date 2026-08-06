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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.app_icon
import ayaturabbi.shared.generated.resources.cancel
import ayaturabbi.shared.generated.resources.download
import ayaturabbi.shared.generated.resources.download_failed
import ayaturabbi.shared.generated.resources.download_required_files_title
import ayaturabbi.shared.generated.resources.downloading_files
import ayaturabbi.shared.generated.resources.yes
import com.devlomi.shared.data.network.DownloadingResource
import com.devlomi.shared.ui.suras.DialogActions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DownloadScreen(
    state: DownloadScreenState,
    onEvent: (DownloadEvents) -> Unit,
) {


    if (state.showConfirmDownloadDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(Res.string.download_required_files_title)) },
            text = { Text("Required Quran files must be downloaded before continuing.") },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.StartDownloadAction(DialogActions.OnConfirm<Nothing>()))
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)) { Text(stringResource(Res.string.download)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.StartDownloadAction(DialogActions.OnDismiss))
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)) { Text(stringResource(Res.string.cancel)) }
            }
        )
    }

    if (state.showConfirmCancelDownloadDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(Res.string.cancel)) },
            text = { Text("Are you sure you want to cancel") },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.CancelDownloadAction(DialogActions.OnConfirm(null)))
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)) { Text(stringResource(Res.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    onEvent(DownloadEvents.CancelDownloadAction(DialogActions.OnDismiss))
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)) { Text(stringResource(Res.string.cancel)) }
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
                    text = stringResource(Res.string.downloading_files),
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
                    Text(stringResource(Res.string.cancel))
                }

            }

            is DownloadingResource.Error -> {
                Text(stringResource(Res.string.download_failed))
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