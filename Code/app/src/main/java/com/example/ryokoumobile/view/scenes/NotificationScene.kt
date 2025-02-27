package com.example.ryokoumobile.view.scenes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ryokoumobile.R
import com.example.ryokoumobile.model.controller.DataController
import com.example.ryokoumobile.model.entity.Notification
import com.example.ryokoumobile.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScene(
    navController: NavController,
    notificationVM: NotificationViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông báo") },
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = {
                        notificationVM.updateIsRead()
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Log.d("HyuNie", notificationVM.uiState.collectAsState().value.size.toString())
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            itemsIndexed(DataController.notificationVM.uiState.value) { index, notify ->
                if (index != 0) {
                    HorizontalDivider()
                }
                ItemNotification(notify, notificationVM)
            }
        }
    }
}

@Composable
private fun ItemNotification(notification: Notification, notificationVM: NotificationViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(0.1f), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(15.dp)
                        .background(
                            color = if (notification.seen) Color.Transparent else Color.Red,
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = if (notification.seen) Color.DarkGray else Color.Transparent
                        )
                )
            }
            Column(modifier = Modifier.weight(0.9f)) {
                Text(notification.timeSend.toDate().toString())
                Text(notification.content)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(notificationVM.getCompanyOfNotification(notification))
                }
            }
        }
    }
}