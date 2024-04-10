package com.develop.nowasteinmyfridge.feature.wastereport


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.nowasteinmyfridge.data.model.Report
import com.develop.nowasteinmyfridge.domain.GetFoodWasteReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WasteReportViewModel @Inject constructor(
    private val getFoodWasteReportUseCase: GetFoodWasteReportUseCase,
) : ViewModel() {
    private val _wasteReportState = mutableStateOf<List<Report>>(emptyList())
    val wasteReportState: State<List<Report>>
        get() = _wasteReportState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getFoodWasteReport()
        }
    }

    private suspend fun getFoodWasteReport() {
        try {
            _wasteReportState.value = getFoodWasteReportUseCase.invoke()
            Log.e("getFoodWasteReportUseCase", "Success to getFoodWasteReportUseCase")
        } catch (e: Exception) {
            Log.e("getFoodWasteReportUseCase", "Unable to getFoodWasteReportUseCase", e)
        }
    }
}