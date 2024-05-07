package com.yourandroidguy.quil.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.foundation.text2.input.insert
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.yourandroidguy.quil.components.NoteEntryAppBar
import com.yourandroidguy.quil.components.NoteEntryBody
import com.yourandroidguy.quil.components.NoteEntryTextBody
import com.yourandroidguy.quil.components.TextFormattingRow
import com.yourandroidguy.quil.data.Note
import com.yourandroidguy.quil.ui.theme.QuilTheme
import com.yourandroidguy.quil.viewmodel.QuilViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteEntryScreen(
    navController: NavHostController,
    viewModel: QuilViewModel,
    modifier: Modifier=Modifier,
    noteId: Long = -1,
) {
    //todo: Extract state to StateHolder
    var bodyHasFocus by remember { mutableStateOf(false) }
    val bodyState = rememberRichTextState()
    val titleState = rememberTextFieldState()
    val date = rememberSaveable {
        DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date())
    }
    var note: Note? by remember {
        mutableStateOf(null)
    }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bodyTextStyle = remember {
        TextStyle(
            fontSize = TextUnit(20f, TextUnitType.Sp),
            lineHeight = TextUnit(36f, TextUnitType.Sp)
        )
    }
    var showUndoRedo by remember {
        mutableStateOf(false)
    }
    var currentText by remember { mutableStateOf("") }
    var isChangeTriggeredFromRedo by remember {
        mutableStateOf(false)
    }
    val undoList = remember {
        mutableListOf("")
    }
    val redoList = remember {
        mutableListOf("")
    }
    var enableUndo by remember {
        mutableStateOf(false)
    }
    var enableRedo by remember {
        mutableStateOf(false)
    }
    var enableShare by remember{
        mutableStateOf(false)
    }

    val savedNotes = remember {
        mutableStateListOf<Note>()
    }

    fun undo() {
        undoList.removeLastOrNull()?.let { if (it.isNotBlank()) redoList.add(it) }
        undoList.lastOrNull()?.let { bodyState.setText(it) }
        enableUndo = undoList.isNotEmpty()
        isChangeTriggeredFromRedo = false
        enableRedo = true
    }

    fun redo() {
        redoList.lastOrNull()?.let {
            if (it.isNotBlank()) bodyState.setText(it)
        }
        redoList.removeLastOrNull()?.let { undoList.add(it) }

        enableRedo = redoList.isNotEmpty()
        isChangeTriggeredFromRedo = true
    }

    LaunchedEffect(key1 = Unit) {
        if(noteId.toInt() != -1){
            enableShare = true
            try {
                viewModel.getAllRecords().collect{
                    note = it.first { note -> note.id == noteId }

                    titleState.clearText()
                    titleState.edit { insert(0, note!!.title) }
                    bodyState.setText(note!!.body)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(key1 = bodyState.annotatedString.text) {
        currentText = bodyState.annotatedString.text

        if (!undoList.contains(currentText) && !isChangeTriggeredFromRedo){
            undoList.add(currentText)
        }

        undoList.lastOrNull()?.let {
            if (it.isNotBlank())
                enableUndo = true
        }

    }

    Column(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding()
    ) {
        NoteEntryAppBar(
            showControls = showUndoRedo,
            onUpButtonClicked =
            {
                focusManager.clearFocus()
                navController.navigateUp()

            },
            onUndoClicked = {undo()},
            onRedoClicked = {redo()},
            onShareClicked =
            {
                note?.let {
                    context.startActivity(viewModel.shareNote(it))
                }
                Log.i("NOTE", note.toString())
            },
            onSaveClicked =
            {
                coroutineScope.launch {
                    handleSaveNoteToDb(titleState, bodyState, noteId, date, savedNotes, viewModel)
                    focusManager.clearFocus()
                }
            },
            redoEnabled = enableRedo,
            undoEnabled = enableUndo,
            shareEnabled = enableShare
        )

        NoteEntryBody(
            modifier = Modifier.weight(1f),
            titleState = titleState,
        ){
            NoteEntryTextBody(
                onTextBodyFocusChanged = {
                    bodyHasFocus = it.isFocused
                    showUndoRedo = it.isFocused
                },
                textStyle = bodyTextStyle,
                state = bodyState
            )
        }


        AnimatedVisibility(
            visible = bodyHasFocus,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 150,
                    easing = LinearEasing
                )
            ),
            exit = fadeOut()
        ){
            TextFormattingRow(
                toggleBoldText = { bodyState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) },
                toggleItalic = { bodyState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) },
                toggleUnderline = { bodyState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline) ) },
                toggleOrderedList = { bodyState.toggleOrderedList()},
                )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun handleSaveNoteToDb(
    titleState: TextFieldState,
    bodyState: RichTextState,
    noteId: Long,
    date: String,
    savedNotes: SnapshotStateList<Note>,
    viewModel: QuilViewModel
): Note {
    val title = titleState.text.toString()
    val body = bodyState.annotatedString.text

    val note = when (noteId.toInt()) {
        -1 -> {
            Note(
                title = title,
                body = body,
                date = date
            )
        }

        else -> {
            Note(
                id = noteId,
                title = title,
                body = body,
                date = date
            )
        }
    }
    if (note.title.isNotBlank() || note.body.isNotBlank()) {
        if (note !in savedNotes) {
            viewModel.addOrUpdateNote(note)
            savedNotes.add(note)
        }
    }
    return note
}


@Preview(showBackground = true)
@Composable
private fun NoteEntryScreenPrev() {
    QuilTheme {
//        NoteEntryScreen()
    }
}