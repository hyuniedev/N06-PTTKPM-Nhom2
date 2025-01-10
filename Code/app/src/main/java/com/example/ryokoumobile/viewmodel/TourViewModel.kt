package com.example.ryokoumobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.ryokoumobile.model.controller.DataController
import com.example.ryokoumobile.model.controller.FirebaseController
import com.example.ryokoumobile.model.entity.Tour
import com.example.ryokoumobile.model.repository.Scenes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TourViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<List<Tour>>(emptyList())
    val uiState = _uiState.asStateFlow()

    init {
        LoadDataTour()
    }

    private fun LoadDataTour() {
        val lsTour = mutableListOf<Tour>()
        FirebaseController.firestore.collection("tours")
            .get()
            .addOnSuccessListener { result ->
                try {
                    for (doc in result) {
                        val tour = doc.toObject(Tour::class.java)
                        tour.id = doc.id
                        lsTour.add(tour)
                    }
                } catch (e: Exception) {
                    Log.e("HyuNie", "Load a error tour")
                }
                _uiState.update { lsTour }
            }
            .addOnFailureListener { e -> Log.e("HyuNie", "Error on $e") }
    }

    fun getIsFavoriteTour(tour: Tour): Boolean {
        return DataController.user.value?.lsFavoriteTour?.contains(
            tour.id
        ) ?: false
    }

    fun getTourFromID(idTour: String): Tour {
        return _uiState.value.first {
            it.id == idTour
        }
    }

    fun navigationToTourDetail(navController: NavController, tour: Tour) {
        navController.navigate(
            Scenes.TourDetail.route.replace(
                "{tourId}",
                tour.id
            )
        )
    }
}