package com.cip.conwaylife3d

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = darkColorScheme()) { ConwayApp() } }
    }
}

@Composable
fun LifeScreen() {
    val life = remember { Life() }
    var cells by remember { mutableStateOf(life.glider()) }
    var running by remember { mutableStateOf(false) }
    var generation by remember { mutableIntStateOf(0) }
    var speed by remember { mutableFloatStateOf(8f) }
    var angle by remember { mutableFloatStateOf(.65f) }
    var tilt by remember { mutableFloatStateOf(.85f) }
    LaunchedEffect(running, speed) {
        while (running) {
            delay((1000f / speed).toLong())
            cells = life.next(cells); generation++
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Conway Life 3D", style = MaterialTheme.typography.headlineMedium)
        Text("48 × 48  ·  Generation $generation  ·  ${cells.count { it }} alive")
        Canvas(
            Modifier.fillMaxWidth().weight(1f).pointerInput(Unit) {
                detectDragGestures { change, drag ->
                    change.consume()
                    angle += drag.x * .012f
                    tilt = (tilt + drag.y * .006f).coerceIn(.25f, 1.35f)
                }
            }
        ) { renderLife(cells, angle, tilt) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { running = !running }) { Text(if (running) "Pause" else "Start") }
            OutlinedButton(onClick = { running = false; cells = life.next(cells); generation++ }) { Text("Step") }
            OutlinedButton(onClick = { running = false; cells = life.empty(); generation = 0 }) { Text("Clear") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { running = false; cells = life.glider(); generation = 0 }) { Text("Glider") }
            OutlinedButton(onClick = { running = false; cells = life.random(System.nanoTime().toInt()); generation = 0 }) { Text("Random") }
        }
        Text("Speed: ${speed.toInt()} generations/s")
        Slider(value = speed, onValueChange = { speed = it }, valueRange = 1f..30f)
        Text("Drag the board to rotate and tilt the camera", style = MaterialTheme.typography.bodySmall)
    }
}

private fun DrawScope.renderLife(cells: BooleanArray, angle: Float, tilt: Float) {
    val scale = min(size.width / 75f, size.height / 75f)
    val ca = cos(angle); val sa = sin(angle)
    val st = sin(tilt); val ct = cos(tilt)
    fun point(x: Float, y: Float, z: Float): Offset {
        val rx = x * ca - y * sa
        val ry = x * sa + y * ca
        return Offset(size.width / 2 + rx * scale, size.height / 2 + (ry * st - z * ct) * scale)
    }
    fun face(color: Color, vararg pts: Triple<Float, Float, Float>) {
        val path = Path()
        pts.forEachIndexed { index, p ->
            val v = point(p.first, p.second, p.third)
            if (index == 0) path.moveTo(v.x, v.y) else path.lineTo(v.x, v.y)
        }
        path.close(); drawPath(path, color)
    }
    face(Color(0xFF173044), Triple(-24f,-24f,0f), Triple(24f,-24f,0f), Triple(24f,24f,0f), Triple(-24f,24f,0f))
    val sorted = cells.indices.filter { cells[it] }.sortedBy { i ->
        val x = i % 48 - 23.5f; val y = i / 48 - 23.5f
        x * sa + y * ca
    }
    for (i in sorted) {
        val x = i % 48 - 23.5f; val y = i / 48 - 23.5f
        val a = x - .43f; val b = x + .43f; val c = y - .43f; val d = y + .43f
        val h = 1.5f
        face(Color(0xFF117B96), Triple(a,c,0f), Triple(b,c,0f), Triple(b,c,h), Triple(a,c,h))
        face(Color(0xFF149FB0), Triple(b,c,0f), Triple(b,d,0f), Triple(b,d,h), Triple(b,c,h))
        face(Color(0xFF0E647B), Triple(b,d,0f), Triple(a,d,0f), Triple(a,d,h), Triple(b,d,h))
        face(Color(0xFF0C536A), Triple(a,d,0f), Triple(a,c,0f), Triple(a,c,h), Triple(a,d,h))
        face(Color(0xFF74F9EE), Triple(a,c,h), Triple(b,c,h), Triple(b,d,h), Triple(a,d,h))
    }
}
