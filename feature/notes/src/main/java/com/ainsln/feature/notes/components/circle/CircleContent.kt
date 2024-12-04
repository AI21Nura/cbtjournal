package com.ainsln.feature.notes.components.circle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt


@Composable
fun ClickableCircle(
    circle: Circle,
    addEmotion: (Long) -> Unit,
    removeEmotion: (Long) -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState =
        rememberTransformableState { zoomChange, panChange, _ ->
            scale *= zoomChange
            offset = if (scale == 1f) Offset.Zero
            else
                Offset(
                    x = offset.x + panChange.x,
                    y = offset.y + panChange.y,
                )
        }

    val levels = remember { mutableListOf(circle.levels) }

    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()

    var bitmap: ImageBitmap? by remember {
        mutableStateOf(null)
    }

    if (bitmap != null) {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .focusable(false)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y,
                )
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        when (val clickResult = findSectorIndex(circle, offset)) {
                            is CircleClickResult.OutsideCircleClick -> return@detectTapGestures
                            is CircleClickResult.SectorClicked -> {
                                if (circle.levels[clickResult.level].selectedSectorsIndexes.contains(clickResult.sector)) {
                                    circle.levels[clickResult.level].selectedSectorsIndexes.remove(clickResult.sector)
                                    removeEmotion(circle.levels[clickResult.level].sectors[clickResult.sector].id)
                                } else {
                                    circle.levels[clickResult.level].selectedSectorsIndexes.add(clickResult.sector)
                                    addEmotion(circle.levels[clickResult.level].sectors[clickResult.sector].id)
                                }
                            }
                        }
                    }
                }
                .drawWithContent {
                    drawContent()
                    drawSelections(levels.first(), circle.center)
                }
                .transformable(state = transformableState),
        ) {
            Image(bitmap = bitmap!!, contentDescription = "")
        }
    } else {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .focusable(false)
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                    coroutineScope.launch {
                        bitmap = graphicsLayer.toImageBitmap()
                    }
                },
        ) {
            ScreenContentToCapture(circle)
        }
    }
}

@Composable
fun ScreenContentToCapture(circle: Circle) {
    val paint =
        remember {
            Paint().asFrameworkPaint().apply {
                color = android.graphics.Color.BLACK
                textAlign = android.graphics.Paint.Align.CENTER
            }
        }

    Canvas(modifier = Modifier.fillMaxSize().focusable(false)) {
        circle.levels.forEach { level ->
            repeat(level.sectorsNumber) { sectorIndex ->
                drawSections(level, sectorIndex, circle.center, paint)
            }
        }
    }
}

fun findSectorIndex(
    circle: Circle,
    point: Offset,
): CircleClickResult {

    val angle =
        atan2((point.y - circle.center.y).toDouble(), (point.x - circle.center.x).toDouble())
            .toFloat()

    val normalizedAngle = (angle + 2 * Math.PI).toFloat() % (2 * Math.PI).toFloat()

    circle.levels.forEachIndexed { index, level ->
        val distanceFromCenter =
            sqrt(
                    (
                            (point.x - circle.center.x)
                                .toDouble()
                                .pow(2) + (point.y - circle.center.y).toDouble().pow(2)
                            ),
                ).toFloat()

        val belongsToLevel =
            distanceFromCenter > level.innerRadius && distanceFromCenter <= level.outerRadius

        if (!belongsToLevel) return@forEachIndexed

        val numSectors = level.sectorsNumber
        val sectorSize = (2 * Math.PI / numSectors).toFloat()

        val sectorIndex = (normalizedAngle / sectorSize).toInt()

        val correctedIndex = sectorIndex.coerceIn(0, numSectors - 1)

        return CircleClickResult.SectorClicked(index, correctedIndex)
    }
    return CircleClickResult.OutsideCircleClick
}
