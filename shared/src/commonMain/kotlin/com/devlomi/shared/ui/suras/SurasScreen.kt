package com.devlomi.shared.ui.suras

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.go_to_juzoa
import ayaturabbi.shared.generated.resources.go_to_page
import ayaturabbi.shared.generated.resources.ic_article
import ayaturabbi.shared.generated.resources.ic_star_ayah
import ayaturabbi.shared.generated.resources.invalid_juzoa
import ayaturabbi.shared.generated.resources.invalid_page
import ayaturabbi.shared.generated.resources.juzoa_number
import ayaturabbi.shared.generated.resources.page_number
import ayaturabbi.shared.generated.resources.search_for_ayah
import ayaturabbi.shared.generated.resources.go
import ayaturabbi.shared.generated.resources.cancel
import com.devlomi.shared.domain.model.Surah
import com.devlomi.shared.ui.components.SearchCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SurasScreen(
    state: SurasState,
    onEvent: (SurasEvents) -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // layout_search margins 32/24/32
            SearchCard(
                placeholder = stringResource(Res.string.search_for_ayah),
                value = state.query,
                onValueChange = { onEvent(SurasEvents.OnQueryChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 24.dp, end = 32.dp)
            )

            // btn_go_to_page + btn_go_to_juzoa, top 16
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onEvent(SurasEvents.OnGoToPageClick)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_article),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(stringResource(Res.string.go_to_page))
                }

                Spacer(Modifier.size(12.dp))

                Button(
                    onClick = {
                        onEvent(SurasEvents.OnGoToJuzoaClick)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_star_ayah),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(stringResource(Res.string.go_to_juzoa))
                }
            }

            // rv_suras marginTop 28
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 28.dp)
            ) {
                items(state.suras, key = { it.surahNumber }) { surah ->
                    SurahItem(
                        modifier = Modifier.animateItem(),
                        surah = surah,
                        onClick = {
                            onEvent(SurasEvents.OnSurahClick(surah))
                        }
                    )
                }
            }
        }
    }

    if (state.pageNumberDialogState.isVisible) {
        NumberInputDialog(
            value = state.pageNumberDialogState.text,
            showError = state.pageNumberDialogState.showError,
            title = stringResource(Res.string.go_to_page),
            hint = stringResource(Res.string.page_number),
            errorText = stringResource(Res.string.invalid_page),
            onDismiss = {
                onEvent(
                    SurasEvents.PageNumberDialogEvents(DialogActionsWithQuery.OnDismiss)
                )
            },
            onChange = {
                onEvent(SurasEvents.PageNumberDialogEvents(DialogActionsWithQuery.OnQueryChange(it)))
            },
            onConfirm = {
                onEvent(SurasEvents.PageNumberDialogEvents(DialogActionsWithQuery.OnConfirm(null)))
            }
        )
    }

    if (state.juzoaNumberDialogState.isVisible) {
        NumberInputDialog(
            value = state.pageNumberDialogState.text,
            showError = state.pageNumberDialogState.showError,
            title = stringResource(Res.string.go_to_juzoa),
            hint = stringResource(Res.string.juzoa_number),
            errorText = stringResource(Res.string.invalid_juzoa),
            onDismiss = {
                onEvent(
                    SurasEvents.JuzoaNumberDialogEvents(DialogActionsWithQuery.OnDismiss)
                )
            },
            onChange = {
                onEvent(SurasEvents.JuzoaNumberDialogEvents(DialogActionsWithQuery.OnQueryChange(it)))
            },
            onConfirm = {
                onEvent(SurasEvents.JuzoaNumberDialogEvents(DialogActionsWithQuery.OnConfirm(null)))
            }
        )
    }
}

@Composable
private fun SurahItem(
    modifier: Modifier = Modifier,
    surah: Surah,
    onClick: () -> Unit
) {
    // item_surah.xml clone
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // selectable bg equivalent via ripple from theme
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = surah.surahName,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(end = 16.dp)
        )

        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Text(
                text = surah.surahNumber.toString(),//TODO surahNumberArabic
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
private fun NumberInputDialog(
    title: String,
    hint: String,
    value: String,
    errorText: String,
    showError: Boolean,
    onDismiss: () -> Unit,
    onChange: (query: String) -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        onChange(it)
                    },
                    singleLine = true,
                    placeholder = { Text(hint) }
                )
                if (showError) {
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) { Text(stringResource(Res.string.go)) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) { Text(stringResource(Res.string.cancel)) }
        }
    )
}
