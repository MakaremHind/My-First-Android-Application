package com.automacorp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.automacorp.ui.theme.AutomacorpTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AutomacorpTopAppBar(
    title: String? = null,
    navigateBack: () -> Unit = {},
    extraActions: @Composable (RowScope.() -> Unit) = {} // Allow passing additional actions
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )

    val defaultActions: @Composable RowScope.() -> Unit = {
        val context = LocalContext.current

        // Rooms Action
        IconButton(onClick = {
            Toast.makeText(context, "Rooms Icon Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, RoomListActivity::class.java)
            context.startActivity(intent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_rooms),
                contentDescription = stringResource(R.string.app_go_room_description)
            )
        }

        // Mail Action
        IconButton(onClick = {
            val mailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:hind.a.makarem@gmail.com"))
            context.startActivity(mailIntent)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_mail),
                contentDescription = stringResource(R.string.app_go_mail_description)
            )
        }

        // GitHub Action
        IconButton(onClick = {
            val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MakaremHind"))
            context.startActivity(githubIntent)
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
            actions = {
                defaultActions()
                extraActions() // Include additional actions
            }
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
            actions = {
                defaultActions()
                extraActions() // Include additional actions
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AutomacorpTopAppBarPreview() {
    AutomacorpTheme {
        AutomacorpTopAppBar(
            title = "Sample Title",
            navigateBack = { /* No-op for preview */ },
            extraActions = {
                IconButton(onClick = { /* Example Extra Action */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Room")
                }
            }
        )
    }
}
