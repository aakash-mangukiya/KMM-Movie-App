package com.example.moviesapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviesapp.theme.LocalColorProvider
import com.example.moviesapp.theme.LocalSpacingProvider
import com.example.moviesapp.theme.LocalTypographyProvider
import com.example.moviesapp.theme.MovieAppTheme

@Composable
fun InfoView(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {

    val spacingProvider = LocalSpacingProvider.current
    val typographyProvider = LocalTypographyProvider.current
    val colorProvider = LocalColorProvider.current

    Column(
        modifier = modifier
    ) {

        Text(
            text = value,
            style = typographyProvider.subHeadLine,
            overflow = TextOverflow.Ellipsis,
            color = colorProvider.textColor.text_strong
        )

        Text(
            modifier = Modifier.padding(top = spacingProvider.spacing_1),
            text = title,
            style = typographyProvider.label,
            overflow = TextOverflow.Ellipsis,
            color = colorProvider.textColor.text_medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoViewPreview(){
    MovieAppTheme {
        InfoView(
            title = "Status",
            value = "Released"
        )
    }
}