package com.kproject.toplightning.presentation.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kproject.toplightning.R
import com.kproject.toplightning.presentation.screens.home.SortListBy
import com.kproject.toplightning.presentation.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSorterBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    selectedSortOption: SortListBy,
    onSort: (SortListBy) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showBottomSheet) {
        var currentSelectedSortOption by remember { mutableStateOf(selectedSortOption) }
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = modifier
        ) {
            Content(
                title = stringResource(id = R.string.sort_list_by),
                options = SortListBy.entries,
                selectedSortOption = currentSelectedSortOption,
                onSelectedSortOptionChange = { currentSelectedSortOption = it },
                onSort = onSort
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Content(
    title: String,
    options: List<SortListBy>,
    selectedSortOption: SortListBy,
    onSelectedSortOptionChange: (SortListBy) -> Unit,
    onSort: (SortListBy) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(14.dp))

        FlowRow {
            options.forEachIndexed { _, option ->
                FilterChip(
                    onClick = { onSelectedSortOptionChange.invoke(option) },
                    label = {
                        Text(text = stringResource(id = option.titleResId))
                    },
                    selected = option == selectedSortOption,
                    modifier = modifier
                )
                Spacer(Modifier.width(6.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onSort.invoke(selectedSortOption) },
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(id = R.string.sort),
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
private fun ListSorterBottomSheetPreview() {
    PreviewTheme {
        ListSorterBottomSheet(
            showBottomSheet = true,
            onDismiss = {},
            onSort = {},
            selectedSortOption = SortListBy.Channels
        )
    }
}