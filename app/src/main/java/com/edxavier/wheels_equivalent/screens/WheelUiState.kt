package com.edxavier.wheels_equivalent.screens

import com.edxavier.wheels_equivalent.db.WheelData
import com.edxavier.wheels_equivalent.db.WheelDataDto

data class WheelUiState(
    var stdWheel: WheelData = WheelData(),
    var newWheel: WheelData = WheelData(),
    var data: WheelDataDto = WheelDataDto(),
    var stdWheelDiameter: Int = 0,
    var newWheelDiameter: Int = 0,
    var speedDifference: Float = 0f,
    var percDifference: Float = 0f,
    var wheelSuggestions: List<WheelData> = listOf(),
    var loading: Boolean = false
)