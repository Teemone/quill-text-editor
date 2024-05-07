package com.yourandroidguy.quil.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.yourandroidguy.quil.ui.theme.QuilTheme

@Composable
fun TextFormattingRow(
    toggleBoldText: () -> Unit,
    toggleItalic: () -> Unit,
    toggleUnderline: () -> Unit,
    toggleOrderedList: () -> Unit,
    modifier: Modifier=Modifier,
) {

    LazyRow(
        modifier = modifier
            .shadow(0.5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically

    ) {

        item{
            BoldRowItem(toggleBoldText = toggleBoldText)
        }

        item{
            ItalicRowItem(toggleItalic = toggleItalic)
        }

        item{
            UnderlineRowItem(toggleUnderline = toggleUnderline)
        }

        item{
            OrderedListRowItem(toggleOrderedList = toggleOrderedList)
        }

    }
}

@Composable
fun BoldRowItem(
    toggleBoldText: () -> Unit,
    modifier: Modifier=Modifier
) {
    var checked by rememberSaveable {
        mutableStateOf(false)
    }

    TextFormattingRowItem(
        modifier = modifier
            .drawIconGradient(checked),
        checked = checked,
        icon = {
            Icon(imageVector = Icons.Default.FormatBold
                , contentDescription = null)

        },
        onItemSelected = {
            toggleBoldText()
            checked = it
        })
}

@Composable
fun ItalicRowItem(
    toggleItalic: () -> Unit,
    modifier: Modifier=Modifier
) {
    var checked by rememberSaveable {
        mutableStateOf(false)
    }

    TextFormattingRowItem(
        modifier = modifier
            .drawIconGradient(checked),
        checked = checked,
        icon = {
            Icon(imageVector = Icons.Default.FormatItalic
                , contentDescription = null)

        },
        onItemSelected = {
            toggleItalic()
            checked = it
        })
}

@Composable
fun UnderlineRowItem(
    toggleUnderline: () -> Unit,
    modifier: Modifier=Modifier
) {
    var checked by rememberSaveable {
        mutableStateOf(false)
    }

    TextFormattingRowItem(
        modifier = modifier
            .drawIconGradient(checked),
        checked = checked,
        icon = {
            Icon(imageVector = Icons.Default.FormatUnderlined
                , contentDescription = null)

        },
        onItemSelected = {
            toggleUnderline()
            checked = it
        })
}

@Composable
fun OrderedListRowItem(
    toggleOrderedList: () -> Unit,
    modifier: Modifier=Modifier
) {
    var checked by rememberSaveable {
        mutableStateOf(false)
    }

    TextFormattingRowItem(
        modifier = modifier
            .drawIconGradient(checked),
        checked = checked,
        icon = {
            Icon(imageVector = Icons.Default.FormatListNumbered
                , contentDescription = null)

        },
        onItemSelected = {
            toggleOrderedList()
            checked = it
        })
}

@Composable
private fun TextFormattingRowItem(
    checked: Boolean,
    icon: @Composable () -> Unit,
    onItemSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {onItemSelected(it)}) {
        icon()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteEntryBody(
    modifier: Modifier=Modifier,
    titleState: TextFieldState = rememberTextFieldState(),
    content: @Composable () -> Unit

) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        val interactionSource = remember {
            MutableInteractionSource()
        }

        BasicTextField2(
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.displaySmall,
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            state = titleState,
            decorator = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = titleState.text.toString(),
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = false,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    placeholder = { Text(text = "Title", style = MaterialTheme.typography.displaySmall)}
                )
            }
        )
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEntryAppBar(
    showControls: Boolean,
    modifier: Modifier=Modifier,
    onUpButtonClicked: () -> Unit = {},
    onRedoClicked: () -> Unit = {},
    onUndoClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {},
    undoEnabled: Boolean = false,
    redoEnabled: Boolean = false,
    shareEnabled: Boolean = false
) {
    TopAppBar(
        modifier = modifier,
        title = { },
        actions = {
            AnimatedVisibility(
                visible = showControls,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 150,
                        easing = LinearEasing
                    )
                ),
                exit = fadeOut()
            ) {
                Row {
                    IconButton(onClick = onUndoClicked, enabled = undoEnabled) {
                        Icon(imageVector = Icons.AutoMirrored.Default.Undo, contentDescription = null)
                    }

                    IconButton(onClick = onRedoClicked, enabled = redoEnabled) {
                        Icon(imageVector = Icons.AutoMirrored.Default.Redo, contentDescription = null)
                    }
                }
            }

            AnimatedVisibility(
                visible = shareEnabled,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 150,
                        easing = LinearEasing
                    )
                ),
                exit = fadeOut()
                ) {
                IconButton(onClick = onShareClicked) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null)
                }
            }

            IconButton(onClick = onSaveClicked) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
            }
            Color.Magenta
        },
        navigationIcon = {
            IconButton(onClick = onUpButtonClicked){
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEntryTextBody(
    modifier: Modifier=Modifier,
    onTextBodyFocusChanged: (FocusState) -> Unit = {},
    textStyle: TextStyle = TextStyle(),
    state: RichTextState = rememberRichTextState()

) {
    Column {
        OutlinedRichTextEditor(
            modifier = modifier
                .onFocusChanged {
                    onTextBodyFocusChanged(it)
                }
                .fillMaxWidth()
                .weight(1f),
            placeholder = {Text("Start here..", style = textStyle)},
            state = state,
            shape = RichTextEditorDefaults.outlinedShape,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            colors = RichTextEditorDefaults.outlinedRichTextEditorColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                containerColor = Color.Transparent
            ),
            textStyle = textStyle,
            contentPadding = PaddingValues(vertical = 8.dp)
        )
    }

}

@Composable
fun Modifier.drawIconGradient(checked: Boolean): Modifier {
    return graphicsLayer { alpha = 0.99f }
        .drawWithCache {
            onDrawWithContent {
                if (checked) {
                    drawContent()
                    drawRect(
                        Brush.linearGradient(
                            listOf(Color.Cyan, Color.Magenta, Color.Yellow),
                        ),
                        blendMode = BlendMode.SrcAtop
                    )
                } else drawContent()

            }
        }
}


@Preview
@Composable
private fun NoteEntryCompPrev() {
    QuilTheme {
    }
}