package com.example.ryokoumobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ryokoumobile.model.controller.DataController
import com.example.ryokoumobile.model.controller.FirebaseController
import com.example.ryokoumobile.model.entity.Company
import com.example.ryokoumobile.model.entity.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(listOf<Notification>())
    var uiState = _uiState.asStateFlow()

    fun loadInitData() {
        val ls = mutableListOf<Notification>()
        try {
            FirebaseController.firestore
                .collection("notification")
                .whereEqualTo("toId", DataController.user.value!!.id)
                .get()
                .addOnSuccessListener { task ->
                    try {
                        for (i in task) {
                            val data = i.toObject(Notification::class.java)
                            ls.add(data)
                        }
                        _uiState.update { ls }
                        Log.d("HyuNie", "size: ${ls.size}")
                    } catch (e: Exception) {
                        Log.e("HyuNie", "${e.message}")
                    }
                }
                .addOnFailureListener {
                    Log.e("HyuNie", "${it.message}")
                }
        } catch (e: Exception) {
            Log.e("HyuNie", "${e.message}")
        }
    }

    fun updateIsRead() {
        val ls = uiState.value
        for (i in ls) {
            i.seen = true
            FirebaseController.firestore.collection("notification")
                .document(i.id).set(i)
        }
    }

    fun getCompanyOfNotification(notification: Notification): String {
        var result = "admin"
        FirebaseController.firestore.collection("companys").document(notification.fromId).get()
            .addOnSuccessListener {
                val company = it.toObject(Company::class.java)
                result = company?.name ?: "admin"
            }
        return result
    }
}