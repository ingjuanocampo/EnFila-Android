package com.ingjuanocampo.enfila.android.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.ingjuanocampo.enfila.android.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    hint: String = "Search",
    onSearch: (String) -> Unit = {},
    onClear: () -> Unit = {},
    accentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    var text: String by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it)
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(hint) },
        leadingIcon = {
            Icon(
                Icons.Default.Search, contentDescription = "Search",
                tint = accentColor
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = {
                    text = ""
                    onClear()
                    onSearch(text)
                    keyboardController?.hide()
                }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = accentColor)
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = textColor,
            cursorColor = accentColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(text)
            }
        )
    )
}


@Preview
@Composable
fun SearchBoxPreview() {
    AppTheme {
        SearchBox(
            onSearch = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
