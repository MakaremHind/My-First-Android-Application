package com.automacorp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.automacorp.ui.theme.AutomacorpTheme
import android.widget.Toast

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AutomacorpTopAppBar(
    title: String? = null,
    navigateBack: () -> Unit = {},
    context: Context? = null // Nullable context for preview compatibility
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )

    val actions: @Composable RowScope.() -> Unit = {
        // Rooms Action
        IconButton(onClick = {
            context?.let {
                val intent = Intent(it, RoomListActivity::class.java)
                it.startActivity(intent)
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_rooms),
                contentDescription = stringResource(R.string.app_go_room_description)
            )
        }

        // Mail Action
        IconButton(onClick = {
            context?.let {
                val mailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:hind.a.makarem@gmail.com"))
                if (mailIntent.resolveActivity(it.packageManager) != null) {
                    it.startActivity(mailIntent)
                } else {
                    // Optional: Show a toast message if no email client is available
                    Toast.makeText(it, "No email app found", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_mail),
                contentDescription = stringResource(R.string.app_go_mail_description)
            )
        }

        // GitHub Action
        IconButton(onClick = {
            context?.let {
                val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MakaremHind"))
                if (githubIntent.resolveActivity(it.packageManager) != null) {
                    it.startActivity(githubIntent)
                } else {
                    // Optional: Show a toast message if no browser is available
                    Toast.makeText(it, "No browser app found", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_github),
                contentDescription = stringResource(R.string.app_go_github_description)
            )
        }
    }

    if (title == null) {
        TopAppBar(
            title = { Text("") },
            colors = colors,
            actions = actions
        )
    } else {
        MediumTopAppBar(
            title = { Text(title) },
            colors = colors,
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.app_go_back_description)
                    )
                }
            },
            actions = actions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AutomacorpTopAppBarHomePreview() {
    AutomacorpTheme {
        AutomacorpTopAppBar(
            title = null,
            navigateBack = {} // No-op for preview
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AutomacorpTopAppBarPreview() {
    AutomacorpTheme {
        AutomacorpTopAppBar(
            title = "A Page",
            navigateBack = {} // No-op for preview
        )
    }
}
