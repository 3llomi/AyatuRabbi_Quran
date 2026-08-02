package com.devlomi.shared.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.follow_us
import ayaturabbi.shared.generated.resources.github
import ayaturabbi.shared.generated.resources.ic_github
import ayaturabbi.shared.generated.resources.ic_screen_lock
import ayaturabbi.shared.generated.resources.ic_share
import ayaturabbi.shared.generated.resources.ic_star
import ayaturabbi.shared.generated.resources.ic_twitter
import ayaturabbi.shared.generated.resources.ic_website
import ayaturabbi.shared.generated.resources.rate_app
import ayaturabbi.shared.generated.resources.screen_lock_prevent
import ayaturabbi.shared.generated.resources.share_app
import ayaturabbi.shared.generated.resources.version
import ayaturabbi.shared.generated.resources.website
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvents) -> Unit,
) {

    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()) // ScrollView
            .padding(start = 8.dp, top = 16.dp, end = 8.dp) // LinearLayout margins
    ) {
        Text(
            text = stringResource(Res.string.version),
            style = SettingsTypography.bold
        )

        Text(
            text = state.versionName,
            style = SettingsTypography.regular,
            modifier = Modifier.padding(top = 8.dp)
        )

        DividerLikeXml()

        SwitchRow(
            text = stringResource(Res.string.screen_lock_prevent),
            icon = Res.drawable.ic_screen_lock,
            checked = state.keepScreenOn,
            onCheckedChange = {
                onEvent(SettingsEvents.OnSwitchChange(it))
            }
        )

        DividerLikeXml()

        ActionRow(
            text = stringResource(Res.string.share_app),
            icon = Res.drawable.ic_share,
            tint = Color.White,
            onClick = {
                //TODO share app link
//                onShareApp("Download Ayatu Rabbi App, the easiest app for Reciting Quran \n$appLink")
            }
        )

        ActionRow(
            text = stringResource(Res.string.website),
            icon = Res.drawable.ic_website,
            topMargin = 16.dp,
            onClick = {
                openUrl(urlHandler = uriHandler, link = "http://devlomi.com")
            }
        )

        ActionRow(
            text = stringResource(Res.string.follow_us),
            icon = Res.drawable.ic_twitter,
            topMargin = 16.dp,
            onClick = {
                openUrl(urlHandler = uriHandler, link = "https://twitter.com/3llomi")
            }
        )

        ActionRow(
            text = stringResource(Res.string.github),
            icon = Res.drawable.ic_github,
            topMargin = 16.dp,
            onClick = {
                openUrl(urlHandler = uriHandler, link = "https://github.com/3llomi/AyatuRabbi_Quran")
            }
        )

        ActionRow(
            text = stringResource(Res.string.rate_app),
            icon = Res.drawable.ic_star,
            topMargin = 16.dp,
            onClick = {
                openUrl(urlHandler = uriHandler, link = "")//TODO APPLINK

            }
        )

        Spacer(Modifier.height(16.dp))
    }
}

fun openUrl(urlHandler: UriHandler, link: String) {
    urlHandler.openUri(link)
}

@Composable
private fun DividerLikeXml() {
    HorizontalDivider(
        color = Color(0xFFE1E1E1),
        thickness = 0.3.dp,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    )
}

@Composable
private fun SwitchRow(
    text: String,
    icon: DrawableResource,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(Modifier.height(0.dp).padding(start = 8.dp)) // drawablePadding 8dp feel
        Text(
            text = text,
            style = SettingsTypography.regular,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ActionRow(
    text: String,
    icon: DrawableResource,
    topMargin: androidx.compose.ui.unit.Dp = 0.dp,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topMargin)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint
        )
        Text(
            text = text,
            style = SettingsTypography.regular,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private object SettingsTypography {
    // Replace with Cairo font family mapping when available
    val bold = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp
    )
    val regular = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp
    )
}