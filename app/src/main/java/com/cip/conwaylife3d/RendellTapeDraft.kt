package com.cip.conwaylife3d

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RendellTapeDraft(value: String, onValueChange: (String) -> Unit) {
    val validation = RendellTapeInput.validate(value)
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Tape input · draft only", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text("Initial symbols") },
                supportingText = { Text("Three-symbol alphabet: 0, 1, 2") },
                isError = validation != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (validation != null) {
                Text(validation, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            } else {
                Text("Draft accepted: ${RendellTapeInput.normalize(value)}",
                    style = MaterialTheme.typography.bodySmall)
            }
            Text("Not applied to Life cells. The loaded Rendell machine still starts with its original two 1s. Encoding a new tape requires verified coordinates and glider phases for both stacks; pressing Jump does not use this draft.",
                style = MaterialTheme.typography.bodySmall)
        }
    }
}
