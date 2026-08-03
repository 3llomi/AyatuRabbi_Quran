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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
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
            .background(SurasUiTokens.colorPrimaryVariant)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // layout_search margins 32/24/32
            SearchCard(
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
                        containerColor = SurasUiTokens.colorSecondary,
                        contentColor = SurasUiTokens.colorOnSecondary
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_article),
                        contentDescription = null,
                        tint = SurasUiTokens.colorOnSecondary
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
                        containerColor = SurasUiTokens.colorSecondary,
                        contentColor = SurasUiTokens.colorOnSecondary
                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_star_ayah),
                        contentDescription = null,
                        tint = SurasUiTokens.colorOnSecondary
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
                        surah = surah,
                        onClick = {
//                            onOpenSurah(surah.surahNumber)//TODO
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
                    SurasEvents.PageNumberDialogEvents(DialogActions.OnDismiss)
                )
            },
            onChange = {
                onEvent(SurasEvents.PageNumberDialogEvents(DialogActions.OnQueryChange(it)))
            },
            onConfirm = {
                onEvent(SurasEvents.PageNumberDialogEvents(DialogActions.OnConfirm))
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
                    SurasEvents.JuzoaNumberDialogEvents(DialogActions.OnDismiss)
                )
            },
            onChange = {
                onEvent(SurasEvents.JuzoaNumberDialogEvents(DialogActions.OnQueryChange(it)))
            },
            onConfirm = {
                onEvent(SurasEvents.JuzoaNumberDialogEvents(DialogActions.OnConfirm))
            }
        )
    }
}

@Composable
private fun SurahItem(
    surah: Surah,
    onClick: () -> Unit
) {
    // item_surah.xml clone
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // selectable bg equivalent via ripple from theme
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = surah.surahName,
            fontSize = 18.sp,
            fontFamily = FontFamily.Default, // swap Cairo regular
            color = SurasUiTokens.primaryText,
            modifier = Modifier.padding(end = 16.dp)
        )

        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(SurasUiTokens.colorSecondaryVariant)
            )
            Text(
                text = surah.surahNumber.toString(),//TODO surahNumberArabic
                color = SurasUiTokens.primaryText
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
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) { Text("Go") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

object SurasUiTokens {
    // swap with exact app colors/fonts for 1:1
    val colorPrimaryVariant = Color(0xFF1B1B1B)
    val colorSecondary = Color(0xFF2E7D32)
    val colorOnSecondary = Color.White
    val colorSecondaryVariant = Color(0xFF4CAF50)
    val primaryText = Color.White
}