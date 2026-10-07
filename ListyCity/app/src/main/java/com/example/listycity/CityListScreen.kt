package com.example.listycity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    onDeleteCity: (City) -> Unit, // Similar to onUpdateCity from the above line
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var editedCityName by remember { mutableStateOf("") }
    var editedProvinceName by remember { mutableStateOf("") }
    var isDeleteSelected by remember { mutableStateOf(false) }
    var targetCity by remember { mutableStateOf<City?>(null) }

    /* Lines 60-87
     * Source: Dialog | Jetpack Compose
     * URL: https://developer.android.com/develop/ui/compose/components/dialog
     * Description: Adapted AlertDialog implementation for city deletion confirmation.
     * Author: Android Developers
     * Date Accessed: October 4, 2026
     * License: https://developer.android.com/license
     */
    if (targetCity != null) { // Here I check if city is currently selected
        AlertDialog(
            onDismissRequest = { targetCity = null }, // When the user dismiss the request, I will clear the targetCity which was selected before
            title = { Text("Delete City?") },
            text = { Text("Delete ${targetCity?.name}, ${targetCity?.province}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val city = targetCity // I assign a variable to the targetCity
                        if (city != null) { // If the city select is not null then I call the onDeleteCity(city) below
                            onDeleteCity(city)
                        }
                        targetCity = null // After the targetCity is deleted, I set the targetCity value back to null
                        isDeleteSelected = false // And isDeleteSelected doesn't become selected until the user clicks on it again
                    }
                ) {
                   Text("DELETE")
                }
            },
            dismissButton = {
                // When dismissButton is clicked, if the user had previously selected any city to be deleted, set the targetCity to null when hit cancel
                TextButton(
                    onClick = { targetCity = null}
                ) {
                    Text("CANCEL")
                }
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showAddCityFields = !showAddCityFields
                    if (showAddCityFields) {
                        selectedCity = null
                        editedCityName = ""
                        editedProvinceName = ""
                    }
                }
            ) {
                Text("+")
            }
        }
        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            onAddCity(
                                City(
                                    name = newCityName,
                                    province = newProvinceName
                                )
                            )

                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    }
                ) {
                    Text("ADD CITY")
                }
            }
        }
        if (selectedCity != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editedCityName,
                    onValueChange = { editedCityName = it },
                    label = { Text("Updated City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = editedProvinceName,
                    onValueChange = { editedProvinceName = it },
                    label = { Text("Updated Province") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        val cityToUpdate = selectedCity
                        if (
                            cityToUpdate != null &&
                            editedCityName.isNotBlank() &&
                            editedProvinceName.isNotBlank()
                        ) {
                            onUpdateCity(
                                cityToUpdate,
                                City(
                                    name = editedCityName,
                                    province = editedProvinceName
                                )
                            )

                            selectedCity = null
                            editedCityName = ""
                            editedProvinceName = ""
                        }
                    }
                ) {
                    Text("UPDATE CITY")
                }
            }
        }
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(cities) { index, city ->
                    CityRow(
                        city = city,
                        onClick = {
                            if (isDeleteSelected) { // If isDeleteSelected is selected on click, I will set the targetCity to that city to be deleted
                                targetCity = city
                            } else {
                                showAddCityFields = false
                                newCityName = ""
                                newProvinceName = ""
                                selectedCity = city
                                editedCityName = city.name
                                editedProvinceName = city.province
                            }
                        }
                    )
                    if (index < cities.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
            Button(
                onClick = {
                    isDeleteSelected = !isDeleteSelected // This is my switch to check if isDeleteSelected is true or false
                    if (isDeleteSelected) { // If my isDeleteSelected is selected, then I will hide my add city field
                        showAddCityFields = false
                        selectedCity = null // SelectedCity becomes null here
                    }
                },
                modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)
            ) {
                Text(if (isDeleteSelected) "CANCEL DELETE" else "DELETE CITY")
            }
        }
    }
}


@Composable
fun CityRow(
    city: City,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCityTheme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> },
            onDeleteCity = {}
        )
    }
}