package com.cip.conwaylife3d

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RendellTapeDraft(
    left: String,
    right: String,
    onLeftChange: (String) -> Unit,
    onRightChange: (String) -> Unit
) {
    val draft = RendellStackDraft(left, right)
    val validation = RendellTapeInput.validate(draft)
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Two-stack input · draft only", style = MaterialTheme.typography.titleMedium)
            Text("Enter each stack starting with the symbol nearest the read/write head.",
                style = MaterialTheme.typography.bodySmall)
            OutlinedTextField(value = left, onValueChange = onLeftChange,
                label = { Text("Left of head") }, singleLine = true,
                modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = right, onValueChange = onRightChange,
                label = { Text("Right of head") }, singleLine = true,
                modifier = Modifier.fillMaxWidth())
            if (validation != null) Text(validation, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall)
            else Text("Draft: L=[${RendellTapeInput.normalize(left)}] · R=[${RendellTapeInput.normalize(right)}]",
                style = MaterialTheme.typography.bodySmall)
            Text("NOT APPLIED. Original Life preset still has two 1s to the right of the head. The tape is physically represented by two glider stacks. No cell-level encoder or decoder is verified yet.",
                style = MaterialTheme.typography.bodySmall)
        }
    }
}
