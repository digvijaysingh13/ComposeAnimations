package com.digi.ruff.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.digi.ruff.R
import com.digi.ruff.ui.theme.UxStyle
import androidx.compose.runtime.getValue



fun BeautifulShape(cornerRadius: Dp) = GenericShape { size, _ ->
    val cornerRadiusPx = cornerRadius.value
    val rect = Rect(0f, 0f, size.width, size.height)
    val topLeftCorner = Rect(0f, 0f, cornerRadiusPx, cornerRadiusPx)
    val topRightCorner = Rect(size.width - cornerRadiusPx, 0f, size.width, cornerRadiusPx)
    val bottomLeftCorner = Rect(0f, size.height - cornerRadiusPx, cornerRadiusPx, size.height)
    val bottomRightCorner =
        Rect(size.width - cornerRadiusPx, size.height - cornerRadiusPx, size.width, size.height)

    addRect(rect)
    addRect(topLeftCorner)
    addRect(topRightCorner)
    addRect(bottomLeftCorner)
    addRect(bottomRightCorner)
}



//    GenericShape { size, _ ->
//    val centerH = size.width / 2f
//    val multiplierW = 1.5f + size.height / size.width
//
//    moveTo(
//        x = centerH - centerH * progress * multiplierW,
//        y = size.height,
//    )
//    val currentWidth = (centerH * progress * multiplierW * 2.5f)
//    cubicTo(
//        x1 = centerH - centerH * progress * 1.5f,
//        y1 = size.height - currentWidth * 0.5f,
//        x2 = centerH + centerH * progress * 1.5f,
//        y2 = size.height - currentWidth * 0.5f,
//        x3 = centerH + centerH * progress * multiplierW,
//        y3 = size.height,
//    )
//}


@Composable
fun GenericBottomDialog() {
    val titles = listOf("Document", "Camera", "Gallery", "Audio", "Location", "Payment", "Contact", "Poll")

    val deltaXAnim = rememberInfiniteTransition()
    val dx by deltaXAnim.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(7, 94, 84), shape = RoundedCornerShape(10.dp))
            .graphicsLayer {
                shape = GenericShape { size, _ ->
                    val center = size.width / 2f
                    this.addRect(
                        Rect(
                            left = center - center * dx,
                            top = center - center * dx,
                            right = center + center * dx,
                            bottom = size.height
                        )
                    )
                    close()
                }
            }

            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(3), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(titles) {
                PickerItem(title = it)
            }
        }
    }

}


@Composable
fun SimplePickerDialog(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {

        PickerView()
    }
}


@Composable
fun PickerView() {
    val titles = listOf("Document", "Camera", "Gallery", "Audio", "Location", "Payment", "Contact", "Poll")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(7, 94, 84), shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(3), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(titles) {
                PickerItem(title = it)
            }
        }
    }
}

@Composable
fun PickerItem(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_svgrepo_com),
            contentDescription = "user profile",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
        )
        Text(
            text = title, modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentHeight()
                .fillMaxHeight(), style = UxStyle.textRegular, textAlign = TextAlign.Center
        )
    }
}


