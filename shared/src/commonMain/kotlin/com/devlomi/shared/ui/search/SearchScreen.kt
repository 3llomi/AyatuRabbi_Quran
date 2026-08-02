package com.devlomi.shared.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.ic_article
import ayaturabbi.shared.generated.resources.ic_quran_logo
import ayaturabbi.shared.generated.resources.ic_reading_quran
import ayaturabbi.shared.generated.resources.ic_search
import ayaturabbi.shared.generated.resources.ic_star_ayah
import ayaturabbi.shared.generated.resources.search_for_ayah
import com.devlomi.shared.domain.model.SearchResult
import com.devlomi.shared.ui.components.SearchCard
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvents) -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // layout_search: margins 32/24/32
            SearchCard(
                value = state.query,
                onValueChange = { onEvent(SearchEvents.OnSearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, top = 24.dp, end = 32.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                items(
                    items = state.searchResults,
                    key = { item -> "${item.pageNumber}_${item.ayahNumber}_${item.surahName}" }
                ) { result ->
                    SearchResultItem(
                        item = result,
                        onClick = {
                            onEvent(SearchEvents.OnSearchResultClicked(result))
                        }
                    )
                }
            }
        }

        // img_quran centered, visible when empty
        if (state.searchResults.isEmpty()) {
            Image(
                painter = painterResource(Res.drawable.ic_quran_logo),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}



private fun highlightMatches(
    fullText: String,
    query: String,
    normalStyle: SpanStyle,
    highlightStyle: SpanStyle
): AnnotatedString {
    if (query.isBlank() || fullText.isBlank()) {
        return buildAnnotatedString { append(fullText) }
    }

    val source = fullText.lowercase()
    val needle = query.trim().lowercase()
    if (needle.isEmpty()) return buildAnnotatedString { append(fullText) }

    val ranges = mutableListOf<IntRange>()
    var startIndex = 0
    while (startIndex < source.length) {
        val found = source.indexOf(needle, startIndex = startIndex)
        if (found < 0) break
        ranges += found until (found + needle.length)
        startIndex = found + needle.length
    }

    if (ranges.isEmpty()) return buildAnnotatedString { append(fullText) }

    return buildAnnotatedString {
        append(fullText)

        // base style for all text
        addStyle(normalStyle, 0, fullText.length)

        // highlight style for each match
        ranges.forEach { r ->
            addStyle(highlightStyle, r.first, r.last + 1)
        }
    }
}
@Composable
private fun SearchResultItem(
    item: SearchResult,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null, // replace with ripple if you want selectableItemBackground behavior
                onClick = onClick
            )
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
    ) {
        val normal = SpanStyle(
            color = Color.White, // or your default text color
            fontSize = 16.sp,
            fontFamily = FontFamily.Default
        )
        val highlight = SpanStyle(
            color = Color(0xFFFFD54F), // tune to old highlight color
            background = Color(0x66FFD54F),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = highlightMatches(
                fullText = item.foundText,
                query = item.highlightedText, // pass query down to item
                normalStyle = normal,
                highlightStyle = highlight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MetaWithIcon(
                text = item.pageNumber.toString(),
                icon = Res.drawable.ic_article
            )
            MetaWithIcon(
                text = item.ayahNumber.toString(),
                icon = Res.drawable.ic_star_ayah
            )
            MetaWithIcon(
                text = item.surahName,
                icon = Res.drawable.ic_reading_quran
            )
        }
    }
}

@Composable
private fun MetaWithIcon(
    text: String,
    icon: DrawableResource
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFFB0BEC5), // map to colorOnSecondary
            fontFamily = FontFamily.Default // Cairo regular
        )
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}