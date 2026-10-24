package com.cip.conwaylife3d

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun ConwayApp() {
    var largeWorld by remember { mutableStateOf(false) }
    Column {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { largeWorld = false }) { Text("48×48 3D") }
            TextButton(onClick = { largeWorld = true }) { Text("Large world") }
        }
        Box(Modifier.weight(1f)) {
            if (largeWorld) LargeLifeScreen() else LifeScreen()
        }
    }
}

@Composable
private fun LargeLifeScreen() {
    val context = LocalContext.current
    val engine = remember { SparseLife() }
    var cells by remember { mutableStateOf(setOf(Cell(1,0), Cell(2,1), Cell(0,2), Cell(1,2), Cell(2,2))) }
    var generation by remember { mutableIntStateOf(0) }
    var playing by remember { mutableStateOf(false) }
    var pxPerCell by remember { mutableFloatStateOf(12f) }
    var centerX by remember { mutableFloatStateOf(1.5f) }
    var centerY by remember { mutableFloatStateOf(1.5f) }
    var error by remember { mutableStateOf<String?>(null) }
    var importInProgress by remember { mutableStateOf(false) }
    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            playing = false
            importInProgress = true
            try {
                val data = context.contentResolver.openInputStream(uri)?.use { input ->
                    // Avoid unbounded document loads on a handheld device.
                    val bytes = input.readBytes()
                    require(bytes.size <= 2_000_000) { "RLE file exceeds 2 MB" }
                    bytes.toString(Charsets.UTF_8)
                } ?: throw IllegalStateException("Could not read file")
                val pattern = LifeRle.decode(data)
                require(pattern.cells.size <= 100_000) { "Pattern has too many live cells for the basic engine" }
                cells = pattern.cells
                generation = 0
                centerX = pattern.width / 2f
                centerY = pattern.height / 2f
                pxPerCell = (300f / maxOf(pattern.width, pattern.height)).coerceIn(.25f, 20f)
                error = null
            } catch (e: Exception) {
                error = e.message ?: "RLE import failed"
            } finally {
                importInProgress = false
            }
        }
    }
    LaunchedEffect(playing) {
        while (playing) {
            delay(150L)
            cells = withContext(Dispatchers.Default) { engine.next(cells) }
            generation++
        }
    }
    Column(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Large Conway world", style = MaterialTheme.typography.titleLarge)
        Text("Generation $generation · ${cells.size} live cells · B3/S23, no wrapping", style = MaterialTheme.typography.bodySmall)
        Canvas(
            Modifier.fillMaxWidth().weight(1f)
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, _ ->
                        val oldScale = pxPerCell
                        val nextScale = (oldScale * zoom).coerceIn(.25f, 60f)
                        // Translation retains the cell under the gesture centroid.
                        val dx = (centroid.x - size.width / 2f)
                        val dy = (centroid.y - size.height / 2f)
                        centerX -= pan.x / nextScale + dx * (1f / nextScale - 1f / oldScale)
                        centerY -= pan.y / nextScale + dy * (1f / nextScale - 1f / oldScale)
                        pxPerCell = nextScale
                    }
                }
        ) {
            drawRect(Color(0xFF102334))
            val left = centerX - size.width / (2f * pxPerCell)
            val top = centerY - size.height / (2f * pxPerCell)
            val right = centerX + size.width / (2f * pxPerCell)
            val bottom = centerY + size.height / (2f * pxPerCell)
            for (cell in cells) {
                if (cell.x < floor(left).toInt() || cell.x > ceil(right).toInt() ||
                    cell.y < floor(top).toInt() || cell.y > ceil(bottom).toInt()) continue
                drawRect(
                    Color(0xFF74F9EE),
                    topLeft = Offset((cell.x - left) * pxPerCell, (cell.y - top) * pxPerCell),
                    size = androidx.compose.ui.geometry.Size(pxPerCell.coerceAtLeast(1f), pxPerCell.coerceAtLeast(1f))
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Button(onClick = { playing = !playing }) { Text(if (playing) "Pause" else "Start") }
            OutlinedButton(onClick = { playing = false; cells = engine.next(cells); generation++ }) { Text("Step") }
            OutlinedButton(onClick = { playing = false; cells = emptySet(); generation = 0 }) { Text("Clear") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            OutlinedButton(onClick = { playing = false; filePicker.launch("*/*") }, enabled = !importInProgress) { Text("Import RLE") }
            OutlinedButton(onClick = {
                playing = false
                cells = setOf(Cell(1,0), Cell(2,1), Cell(0,2), Cell(1,2), Cell(2,2))
                centerX = 1.5f; centerY = 1.5f; pxPerCell = 12f; generation = 0
            }) { Text("Glider") }
        }
        Text("Pinch to zoom · drag to pan · RLE files run under standard Life rules", style = MaterialTheme.typography.bodySmall)
        if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
    }
}
