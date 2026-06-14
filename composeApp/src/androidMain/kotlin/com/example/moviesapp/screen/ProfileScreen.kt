@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.moviesapp.screen

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.moviesapp.R
import com.example.moviesapp.components.LoaderIndicator
import com.example.moviesapp.presentation.events.ProfileEvent
import com.example.moviesapp.presentation.state.ProfileUiState
import com.example.moviesapp.presentation.viewmodel.ProfileViewModel
import com.example.moviesapp.theme.LocalColorProvider
import com.example.moviesapp.theme.LocalRadiusProvider
import com.example.moviesapp.theme.LocalSpacingProvider
import com.example.moviesapp.theme.LocalTypographyProvider
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileScreen(
    onNavigationBack: () -> Unit,
) {
    BackHandler {
        onNavigationBack()
    }

    val activity = LocalActivity.current as ComponentActivity
    val viewModel: ProfileViewModel = koinViewModel(parameters = { parametersOf(activity) })

    val uiState = viewModel.uiState.collectAsState().value

    Profile(
        uiState = uiState,
        onNavigationBack = onNavigationBack,
        handleEvent = viewModel::handleEvent
    )
}

@Composable
fun Profile(
    uiState: ProfileUiState,
    onNavigationBack: () -> Unit,
    handleEvent: (ProfileEvent) -> Unit,
) {

    val spacingProvider = LocalSpacingProvider.current
    val typographyProvider = LocalTypographyProvider.current
    val colorProvider = LocalColorProvider.current
    val radiusProvider = LocalRadiusProvider.current

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = colorProvider.backgroundColor.bg_white,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_title),
                        style = typographyProvider.titleMedium,
                        color = colorProvider.textColor.text_white
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorProvider.backgroundColor.bg_strong
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigationBack) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            tint = colorProvider.backgroundColor.bg_white,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->

        when {
            uiState.isLoading -> {
                LoaderIndicator(
                    color = colorProvider.backgroundColor.bg_strong
                )
            }

            else -> {

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                        .fillMaxWidth()
                        .padding(
                            vertical = spacingProvider.spacing_4,
                            horizontal = spacingProvider.spacing_4
                        ),
                    verticalArrangement = Arrangement.spacedBy(spacingProvider.spacing_4),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                color = colorProvider.backgroundColor.bg_black.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                            .border(
                                width = 2.dp,
                                color = colorProvider.backgroundColor.bg_black.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                            .clip(shape = CircleShape)
                            .clickable { showBottomSheet = true },
                        model = when {
                            uiState.profileData?.imageUrl.isNullOrEmpty()
                                .not() -> uiState.profileData?.imageUrl

                            uiState.imageData?.uri.isNullOrEmpty().not() -> uiState.imageData?.uri
                            else -> null
                        },
                        contentDescription = stringResource(R.string.profile_image_content_description),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_placeholder),
                        error = painterResource(id = R.drawable.ic_placeholder),
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.profileData?.firstName.orEmpty(),
                        onValueChange = { handleEvent(ProfileEvent.UpdateFirstName(it)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text(stringResource(R.string.label_first_name)) }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.profileData?.lastName.orEmpty(),
                        onValueChange = { handleEvent(ProfileEvent.UpdateLastName(it)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text(stringResource(R.string.label_last_name)) }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.profileData?.email.orEmpty(),
                        onValueChange = { handleEvent(ProfileEvent.UpdateEmail(it)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        label = { Text(stringResource(R.string.label_email)) }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = uiState.profileData?.phoneNumber.orEmpty(),
                        onValueChange = { handleEvent(ProfileEvent.UpdatePhoneNumber(it)) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        label = { Text(stringResource(R.string.label_phone_number)) }
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorProvider.backgroundColor.bg_strong,
                            contentColor = colorProvider.textColor.text_white
                        ),
                        shape = RoundedCornerShape(radiusProvider.large),
                        onClick = {
                            uiState.profileData?.let {
                                handleEvent(ProfileEvent.SaveProfile(it))
                            }
                        }
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.btn_save),
                            textAlign = TextAlign.Center,
                            style = typographyProvider.titleSmall,
                            color = colorProvider.textColor.text_white
                        )
                    }
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    showBottomSheet = false
                                    handleEvent(ProfileEvent.PickImageFromGallery)
                                }
                            ) {
                                Text(stringResource(R.string.btn_gallery))
                            }

                            Button(
                                onClick = {
                                    showBottomSheet = false
                                    handleEvent(ProfileEvent.PickImageFromCamera)
                                }
                            ) {
                                Text(stringResource(R.string.btn_camera))
                            }
                        }
                    }
                }
            }
        }
    }
}