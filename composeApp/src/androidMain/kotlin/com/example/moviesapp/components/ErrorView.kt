package com.example.moviesapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviesapp.R
import com.example.moviesapp.theme.LocalColorProvider
import com.example.moviesapp.theme.LocalSpacingProvider
import com.example.moviesapp.theme.LocalTypographyProvider

@Composable
fun ErrorView(
    error: String,
    onRetry: () -> Unit
) {

    val spacingProvider = LocalSpacingProvider.current
    val typographyProvider = LocalTypographyProvider.current
    val colorProvider = LocalColorProvider.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorProvider.backgroundColor.bg_white)
            .padding(spacingProvider.spacing_6),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = error,
            textAlign = TextAlign.Center,
            style = typographyProvider.titleSmall,
            color = colorProvider.textColor.text_strong
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = spacingProvider.spacing_4),
            shape = ButtonDefaults.shape,
            colors = ButtonDefaults.buttonColors(containerColor = colorProvider.backgroundColor.bg_strong)
        ) {
            Text(
                text = stringResource(R.string.btn_retry),
                style = typographyProvider.titleSmall,
                color = colorProvider.textColor.text_white
            )
        }
    }
}

@Preview
@Composable
private fun ErrorViewPreview() {
    ErrorView(
        error = "An error occurred while fetching movies. Please try again.",
        onRetry = { }
    )
}