package com.digi.ruff.custom

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Composable
fun Dp.toPixel(): Float = with(LocalDensity.current) {
    this@toPixel.toPx()
}

@Composable
fun floatToDp(value: Float) = with(LocalDensity.current) {
    Dp(value / density)
}