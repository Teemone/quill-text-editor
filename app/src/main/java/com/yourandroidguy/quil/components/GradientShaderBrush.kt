package com.yourandroidguy.quil.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode

class GradientShaderBrush(
    private val colors: List<Color>,
    private val offset: Float
): ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val widthOffset = size.width * offset
        val heightOffset = size.height * offset
        return LinearGradientShader(
            colors = colors,
            from = Offset(widthOffset, heightOffset),
            to = Offset(widthOffset + size.width, heightOffset + size.height),
            tileMode = TileMode.Mirror
        )
    }
}