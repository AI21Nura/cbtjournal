package com.ainsln.feature.notes.components.circle

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path

data class Circle(
    val center: Offset,
    val radius: Float,
    val levels: List<CircleLevel>,
){
    companion object {
        val DEFAULT = Circle(
            center = Offset(0f,0f),
            radius = 0f,
            levels = emptyList()
        )
    }
}

class CircleLevel(
    val sectorsNumber: Int,
    val outerRadius: Float,
    val innerRadius: Float,
    val sectorAngle: Float,
    val sectors: List<Sector>,
    val averageTextLength: Int,
) {
    val selectedSectorsIndexes: MutableList<Int> =
        mutableStateListOf<Int>().apply {
            sectors.forEachIndexed { index, sector ->
                if (sector.isSelected) add(index)
            }
        }

    val maxTextWidth =
        (if (innerRadius == 0f) (outerRadius * 0.9f) else (outerRadius - innerRadius)) * 0.8f

    private var path: Path? = null

    fun getPath(center: Offset): Path =
        path ?: Path().apply {
            moveTo(
                center.x + innerRadius *
                        kotlin.math
                            .cos(Math.toRadians((-sectorAngle / 2).toDouble()))
                            .toFloat(),
                center.y + innerRadius *
                        kotlin.math
                            .sin(Math.toRadians((-sectorAngle / 2).toDouble()))
                            .toFloat(),
            )

            arcTo(
                rect =
                Rect(
                    Offset(
                        center.x - outerRadius,
                        center.y - outerRadius,
                    ),
                    Size(outerRadius * 2, outerRadius * 2),
                ),
                startAngleDegrees = -sectorAngle / 2,
                sweepAngleDegrees = sectorAngle,
                forceMoveTo = false,
            )

            lineTo(
                center.x + innerRadius *
                        kotlin.math
                            .cos(Math.toRadians((sectorAngle / 2).toDouble()))
                            .toFloat(),
                center.y + innerRadius *
                        kotlin.math
                            .sin(Math.toRadians((sectorAngle / 2).toDouble()))
                            .toFloat(),
            )

            arcTo(
                rect =
                Rect(
                    Offset(
                        center.x - innerRadius,
                        center.y - innerRadius,
                    ),
                    Size(innerRadius * 2, innerRadius * 2),
                ),
                startAngleDegrees = sectorAngle / 2,
                sweepAngleDegrees = -sectorAngle,
                forceMoveTo = false,
            )
            close()
            path = this
        }
}
