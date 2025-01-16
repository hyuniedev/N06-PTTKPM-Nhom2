package com.example.ryokoumobile.model.entity

import com.google.firebase.Timestamp

data class TourBooked(
    var id: String = "",
    var numPerson: Int = 0,
    var idTour: String = "",
    var startDay: Timestamp = Timestamp.now()
)
