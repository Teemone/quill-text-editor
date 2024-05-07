package com.yourandroidguy.quil.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yourandroidguy.quil.components.AnimatedGradientText
import com.yourandroidguy.quil.components.NoteItemList
import com.yourandroidguy.quil.data.Note
import com.yourandroidguy.quil.ui.theme.QuilTheme
import com.yourandroidguy.quil.viewmodel.QuilViewModel

@Composable
fun HomeScreen(
    viewModel: QuilViewModel,
    noteList: List<Note>,
    modifier: Modifier = Modifier,
    onNoteItemClicked: (Note) -> Unit = {},

) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = rememberLazyListState()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedGradientText(text = "Quil")
            Spacer(modifier = Modifier.height(15.dp))
        }

        NoteItemList(
            state = state,
            itemList = noteList,
            onNoteItemClicked = {onNoteItemClicked(it)},
            onDeleteNoteItem = {viewModel.deleteNote(it)}
        )

    }
}


@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPrev() {
    QuilTheme {
//        HomeScreen()
    }
}