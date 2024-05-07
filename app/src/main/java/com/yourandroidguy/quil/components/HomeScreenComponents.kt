package com.yourandroidguy.quil.components

import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.yourandroidguy.quil.R
import com.yourandroidguy.quil.data.Note
import com.yourandroidguy.quil.ui.theme.QuilTheme

val defaultColors = listOf(
        Color.Cyan, Color.Magenta, Color.Yellow
    )

private val dancingFontFamily =
    FontFamily(
        Font(resId = R.font.ds_reg, FontWeight.Normal),
        Font(resId = R.font.ds_bold, FontWeight.Bold),
        Font(resId = R.font.ds_med, FontWeight.Medium),
        Font(resId = R.font.ds_semi_bold, FontWeight.SemiBold),
    )

@Composable
fun AnimatedGradientText(
    text: String,
    modifier: Modifier=Modifier,
    colors: List<Color> = defaultColors
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val offset by infiniteTransition.animateFloat(
        label = "animate float",
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = remember(offset) {
        GradientShaderBrush(colors, offset)
    }

    Text(
        modifier = modifier,
        style = TextStyle(
            brush = brush,
            fontFamily = dancingFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(56f, TextUnitType.Sp)
        ),
        text = text)
}

@Composable
fun LargeHomeFab(
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LargeFloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(painter = icon, contentDescription = null, modifier = Modifier.drawIconGradient(true))
    }
}



@Composable
fun NoteItem(
    title: String,
    body: String,
    date: String,
    onClick: () -> Unit,
    modifier: Modifier=Modifier
) {
    Card(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() },
    ) {
        Column(
            Modifier
                .padding(20.dp)
        ) {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = body,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = TextUnit(20f, TextUnitType.Sp)
            )

            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 4.dp)
            )

        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItemList(
    modifier: Modifier=Modifier,
    onNoteItemClicked: (Note) -> Unit = {},
    onDeleteNoteItem: (Note) -> Unit = {},
    itemList: List<Note>,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = itemList,
            key = {it.id}
        ){
            SwipeBox(
                modifier = Modifier
                    .animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = EaseOutQuint
                        )
                    )
                    .padding(horizontal = 16.dp),
                onDelete = {onDeleteNoteItem(it)}) {

                NoteItem(
                    modifier = Modifier
                        .heightIn(min = 80.dp)
                        .fillMaxWidth(),
                    title = it.title,
                    body = it.body,
                    date= it.date,
                    onClick = {onNoteItemClicked(it)}
                )
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBox(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState()
    val alpha = (swipeState.progress * 2).coerceIn(0f, 1f)
    val isEndToStart = swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart

    SwipeToDismissBox(
        modifier = modifier.fillMaxSize(),
        state = swipeState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(alpha)
                    .background(
                        color =
                            if(isEndToStart) MaterialTheme.colorScheme.errorContainer else Color.Transparent,
                        shape =
                            MaterialTheme.shapes.medium
                    )
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    imageVector = Icons.Outlined.Delete, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        content()
    }

    LaunchedEffect(key1 = swipeState.progress) {
        if (swipeState.progress < .5f)
            swipeState.reset()
        else
            swipeState.dismiss(swipeState.dismissDirection)
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            onDelete()
        }
        else -> {}
    }
}

@Preview
@Composable
private fun NoteItemsPrev() {
    QuilTheme {
    }
}

@Preview
@Composable
private fun ComponentsPrev() {
    QuilTheme {
        AnimatedGradientText(text = "Quil", modifier = Modifier.fillMaxWidth())
    }
}