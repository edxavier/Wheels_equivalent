package com.edxavier.wheels_equivalent.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edxavier.wheels_equivalent.db.*
import com.edxavier.wheels_equivalent.db.WheelsDB
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WheelViewModel(context:Context): ViewModel() {
    private val _uiState = MutableStateFlow(WheelUiState())
    val uiState: StateFlow<WheelUiState> = _uiState.asStateFlow()
    val dao = WheelsDB.getInstance(context).wheelsDao()
    var repo: RepoWheels = RepoWheels(dao)

    init {
        loadData()
    }

    fun initializeData(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            RepoWheels.initWidths(context)
            RepoWheels.initProfiles(context)
            RepoWheels.initRims(context)
            RepoWheels.initSpeeds(context)
            RepoWheels.initLoads(context)
            loadData()
        }
    }

    fun loadData(){
        val cardData = WheelDataDto()
        cardData.widths = repo.getWidths()
        cardData.profiles = repo.getProfiles()
        cardData.rims = repo.getRims()
        cardData.loads = repo.getLoads()
        cardData.speeds = repo.getSpeeds()
        _uiState.update { state ->
            state.copy(
                data = cardData,
            )
        }
        if(cardData.speeds.isNotEmpty()) {
            setWheelsData()
        }
    }

    fun setWheelsData(){
        val standardWheel = WheelData()
        val newWheel = WheelData()
        standardWheel.width = _uiState.value.data.widths[Prefs.getInt("oai", 4)]
        standardWheel.profile = _uiState.value.data.profiles[Prefs.getInt("opi", 4)]
        standardWheel.rim = _uiState.value.data.rims[Prefs.getInt("odi", 4)]
        standardWheel.load = _uiState.value.data.loads[Prefs.getInt("oci", 4)]
        standardWheel.speed = _uiState.value.data.speeds[Prefs.getInt("ovi", 4)]

        newWheel.width = _uiState.value.data.widths[Prefs.getInt("nai", 4)]
        newWheel.profile = _uiState.value.data.profiles[Prefs.getInt("npi", 4)]
        newWheel.rim = _uiState.value.data.rims[Prefs.getInt("ndi", 4)]
        newWheel.load = _uiState.value.data.loads[Prefs.getInt("nci", 4)]
        newWheel.speed = _uiState.value.data.speeds[Prefs.getInt("nvi", 4)]

        _uiState.update { state ->
            state.copy(
                stdWheel = standardWheel,
                newWheel = newWheel
            )
        }
        calculateEquivalence()
    }

    private fun calculateEquivalence(){
        val stdWheel = _uiState.value.stdWheel
        val newWheel = _uiState.value.newWheel
        val resWheel = stdWheel.compareWheel(newWheel)
        _uiState.update { state ->
            state.copy(
                percDifference = resWheel.percDiff,
                stdWheelDiameter = stdWheel.getDiameter().toInt(),
                newWheelDiameter = newWheel.getDiameter().toInt(),
                speedDifference = resWheel.speedDiff
            )
        }
    }
    fun getSuggestions(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { state ->
                state.copy(
                    loading = true
                )
            }
            val suggestions = repo.getSuggestions(_uiState.value.stdWheel)
            _uiState.update { state ->
                state.copy(
                    wheelSuggestions = suggestions,
                    loading = false
                )
            }
        }
    }

}