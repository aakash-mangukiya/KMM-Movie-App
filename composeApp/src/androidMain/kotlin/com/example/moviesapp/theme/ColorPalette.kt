package com.example.moviesapp.theme

import androidx.compose.ui.graphics.Color

data class ColorPalette(
    val textColor: TextColor = TextColor(),
    val backgroundColor: BackgroundColor = BackgroundColor()
)
data class TextColor(
    val text_strong: Color = text_strong_color,
    val text_medium: Color = text_medium_color,
    val text_week: Color = text_week_color,
    val text_black: Color = black,
    val text_white: Color = white,
    val text_error: Color = error_color,
    val text_success: Color = success_color
)

data class BackgroundColor(
    val bg_strong: Color = bg_strong_color,
    val bg_medium: Color = bg_medium_color,
    val bg_week: Color = bg_week_color,
    val bg_black: Color = black,
    val bg_white: Color = white,
)