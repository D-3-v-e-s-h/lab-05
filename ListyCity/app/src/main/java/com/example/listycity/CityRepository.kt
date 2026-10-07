package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) { // Checking if any error occurred when you are taking a snapshot
                return@addSnapshotListener
            }
            _cities.clear() // Values inside private val cities listed above will be cleared

            snapshot?.documents?.forEach { document ->
                val city = document.toObject(City::class.java)
                if (city != null) {
                    _cities.add(city)
                }
            }
        }
    }

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }

    fun deleteCity(city: City) {
        citiesRef.document(city.name).delete() // Similar to addCity function from above
    }

    fun updateCity(oldCity: City, updatedCity: City) {
    /* TA's initial code from lab slides: citiesRef.document(oldCity.name).set(updatedCity) (Accessed: Oct-3-2026 from slides)
       You cannot rename an existing Document ID, you must delete the old document and create new one with new name as its ID */
        citiesRef.document(oldCity.name).delete() // This deletes the old ID
        citiesRef.document(updatedCity.name).set(updatedCity) // This creates the new ID
    }
}