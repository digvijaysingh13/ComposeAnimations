package com.digi.ruff.custom

import android.graphics.PointF
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun DropletBottomBar(
    containerColor: Color,
    contentColor: Color,
    content: List<String>,
    onItemClick: (Int) -> Unit
) {
    val list = remember {
        MutableList(content.size) { 0f }
    }
    var offsetX by remember { mutableStateOf(list[0]) }
    val animatedOffset = animateLinear(offset = offsetX)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 2.dp, end = 2.dp, bottom = 2.dp)
            .background(
                color = containerColor, shape = IndentShapeRect(
                    indentShapeData = IndentShapeData(width = 30.dp.toPixel(), height = 15.dp.toPixel()),
                    offset = animatedOffset.value.x,
                    cornerRadius = 6.dp.toPixel()

                )
            )
    ) {

        Box(
            modifier = Modifier
                .offset(floatToDp(value = animatedOffset.value.x) + 10.dp) // x = 6 + (30/2) - (10/2)
                .size(10.dp)
                .clip(CircleShape)
                .background(containerColor)
        )

        Row(
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val backOffset = 15.dp.toPixel()
            content.forEachIndexed { index, title ->
                Text(text = title, modifier = Modifier
                    .weight(1f)
                    .clickable {
                        offsetX = list[index]
                        onItemClick(index)
                    }
                    .onGloballyPositioned {
                        if (index == 0 && list[0] == 0f) {
                            list[index] = it.positionInParent().x + (it.size.width / 2) - backOffset
                            offsetX = list[0]
                        } else {
                            list[index] = it.positionInParent().x + (it.size.width / 2) - backOffset
                        }
                    }, textAlign = TextAlign.Center,
                    color = contentColor
                )

            }
        }
    }
}


@Composable
private fun animateLinear(offset: Float): State<Offset> {
    val animOffset = animateOffsetAsState(
        targetValue = Offset(offset, 0f),
        animationSpec = tween(durationMillis = 1000)
    )
    val producer = produceState(initialValue = Offset(offset, 0f), key1 = animOffset.value) {
        this.value = animOffset.value
    }
    return producer
}

class IndentShapeRect(
    private val indentShapeData: IndentShapeData,
    private val cornerRadius: Float = 20f,
    private val offset: Float
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) = Outline.Generic(
        path = Path().addRoundRectWithIndent(
            size = size, offset = offset
        )
    )

    private fun Path.addRoundRectWithIndent(
        size: Size, offset: Float
    ): Path {
        reset()
        // adding top left arc
        arcTo(
            rect = Rect(
                left = 0f, top = 0f, right = 2 * cornerRadius, bottom = 2 * cornerRadius
            ), startAngleDegrees = 180.0f, sweepAngleDegrees = 90.0f, forceMoveTo = false
        )
        // adding top line first part
        lineTo(offset, 0f)
        // adding indent arc
        addPath(
            createIntentPath(
                Rect(
                    Offset(
                        x = offset, y = 0f
                    ), Size(indentShapeData.width, indentShapeData.height)
                )
            )
        )
        // adding top line second path
        lineTo(size.width - (cornerRadius), 0f)
        // adding top right arc
        arcTo(
            rect = Rect(
                left = size.width - (2 * cornerRadius), top = 0f, right = size.width, bottom = (2 * cornerRadius)
            ), startAngleDegrees = 270f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        // adding right line
        lineTo(size.width, size.height - (cornerRadius))
        // adding bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - (2 * cornerRadius),
                top = size.height - (2 * cornerRadius),
                right = size.width,
                bottom = size.height
            ), startAngleDegrees = 0f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        // adding bottom line
        lineTo(cornerRadius, size.height)
        // adding bottom left arc
        arcTo(
            rect = Rect(
                left = 0f, top = size.height - (2 * cornerRadius), right = 2 * cornerRadius, bottom = size.height
            ), startAngleDegrees = 90f, sweepAngleDegrees = 90f, forceMoveTo = false
        )
        // adding left line
        lineTo(0f, cornerRadius)
        close()
        return this
    }

    private fun createIntentPath(rect: Rect): Path {
        val maxX = 110f
        val maxY = 34f
        fun translate(x: Float, y: Float): PointF {
            return PointF(
                (x / maxX) * rect.width + rect.left, (y / maxY) * rect.height + rect.top
            )
        }

        val path = Path()
        val start = translate(0f, 0f)
        val mid = translate(55f, 34f)
        val end = translate(110f, 0f)

        val clt1 = translate(23f, 0f)
        val clt2 = translate(29f, 34f)
        val clt3 = translate(81f, 34f)
        val clt4 = translate(87f, 0f)
        path.moveTo(start.x, start.y)
        path.cubicTo(clt1.x, clt1.y, clt2.x, clt2.y, mid.x, mid.y)
        path.cubicTo(clt3.x, clt3.y, clt4.x, clt4.y, end.x, end.y)
        path.close()
        return path
    }

}


data class IndentShapeData(
    val width: Float, val height: Float
)