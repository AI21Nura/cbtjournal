package com.ainsln.feature.notes.components.circle

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

fun DrawScope.drawEmotionText(
    center: Offset,
    innerRadius: Float,
    outerRadius: Float,
    label: String,
    isReflected: Boolean,
    textPaint: NativePaint,
) {
    val correctInnerRadius = if (innerRadius == 0f) outerRadius * 0.1f else innerRadius
    val textRadius = (correctInnerRadius + outerRadius) / 2

    val fontMetrics = textPaint.fontMetrics
    val textHeight = (fontMetrics.descent - fontMetrics.ascent) / 3

    val textX = center.x + textRadius // * kotlin.math.cos(Math.toRadians(0.0)).toFloat()
    val textY = center.y // + textRadius * kotlin.math.sin(Math.toRadians(0.0)).toFloat()

    drawContext.canvas.nativeCanvas.apply {
        save()

        if (isReflected) {
            translate(textX, textY - textHeight)
            scale(-1f, -1f)
        } else {
            translate(textX, textY + textHeight)
        }

        drawText(label, 0f, 0f, textPaint)
        restore()
    }
}

fun DrawScope.drawEmotionSector(
    center: Offset,
    level: CircleLevel,
    isSelected: Boolean,
    sector: Sector,
) {
    val glowColor = Color(0xFF0000d1).copy(alpha = 0.7f)

    if (isSelected) {
        drawPath(
            path = level.getPath(center),
            color = glowColor,
            style = Stroke(width = 10f),
        )
    } else {
        drawPath(level.getPath(center), color = sector.color, style = Fill)
        drawPath(level.getPath(center), color = Color.Black, style = Stroke(width = 1.dp.toPx()))
    }
}

fun DrawScope.drawSections(
    level: CircleLevel,
    sectorIndex: Int,
    center: Offset,
    paint: NativePaint,
) {
    val numSectors = level.sectorsNumber
    val isReflected =
        when (numSectors % 4) {
            0, 1 -> (sectorIndex in (numSectors / 4)..<(numSectors / 4 * 3))
            else -> (sectorIndex in (numSectors / 4)..<(numSectors / 4 * 3 + numSectors % 4 - 1))
        }

    rotate(degrees = sectorIndex * level.sectorAngle + (level.sectorAngle / 2), pivot = center) {
        drawEmotionSector(
            center,
            level,
            false,
            level.sectors[sectorIndex],
        )

        drawEmotionText(
            center,
            level.innerRadius,
            level.outerRadius,
            level.sectors[sectorIndex].label,
            isReflected,
            paint.apply { adjustTextSize(level.sectors[sectorIndex].label, level.maxTextWidth, level.averageTextLength) },
        )
    }
}

fun DrawScope.drawSelections(
    levels: List<CircleLevel>,
    center: Offset,
) {
    levels.forEach { level ->
        level.selectedSectorsIndexes.forEach { sectorIndex ->
            rotate(
                degrees = sectorIndex * level.sectorAngle + (level.sectorAngle / 2),
                pivot = center,
            ) {
                drawEmotionSector(
                    center,
                    level,
                    true,
                    level.sectors[sectorIndex],
                )
            }
        }
    }
}

fun NativePaint.adjustTextSize(
    text: String,
    maxWidth: Float,
    averageTextLength: Int,
) {
    val correctText =
        if (text.length < averageTextLength) List(averageTextLength) { "a" }.joinToString("") else text
    textSize = 70f
    var textWidth = measureText(correctText)
    while (textWidth > maxWidth) {
        textSize -= 5f
        textWidth = measureText(correctText)
    }
}
