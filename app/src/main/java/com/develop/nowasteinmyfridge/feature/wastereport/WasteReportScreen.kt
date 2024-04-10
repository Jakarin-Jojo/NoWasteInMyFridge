package com.develop.nowasteinmyfridge.feature.wastereport


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.develop.nowasteinmyfridge.data.model.Report
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun WasteReportScreen(
    wasteReportViewModel: WasteReportViewModel = hiltViewModel(),
) {
    val wasteReports by wasteReportViewModel.wasteReportState

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (wasteReports.isNotEmpty()) {
            Text(text = "Food Waste Report Weekly", fontSize = 24.sp, textAlign = TextAlign.Center)
            BarChart(wasteReports, "Weekly Performance Report")
        } else {
            Text(text = "No Food Waste Report information", fontSize = 24.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun BarChart(
    wasteReports: List<Report>,
    chartTitle: String
) {
    val barEntries = wasteReports.mapIndexed { index, report ->
        BarEntry(index.toFloat(), report.performance.toFloat())
    }

    AndroidView(
        modifier = Modifier
            .height(500.dp)
            .fillMaxSize(),
        factory = { context ->
            BarChart(context).apply {
                setDrawBarShadow(false)
                setDrawValueAboveBar(true)
                description.isEnabled = true
                description.text = chartTitle // Set chart title as description text
                setTouchEnabled(false)
                setDrawGridBackground(false)
                setPinchZoom(false)
                isDoubleTapToZoomEnabled = false
                animateY(1000, Easing.EaseInOutQuad)

                // Set X-axis
                val xAxis = xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(true)
                xAxis.granularity = 1f // Ensure only one label per entry
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index >= 0 && index < wasteReports.size) {
                            wasteReports[index].week
                        } else {
                            "" // Empty string for non-existent labels
                        }
                    }
                }

                // Set Y-axis
                val yAxisRight = axisRight
                yAxisRight.isEnabled = false
                val yAxisLeft = axisLeft
                yAxisLeft.axisMinimum = 0f

                // Set up bar data and data set
                val barDataSet = BarDataSet(barEntries, "Performance")

                val data = BarData(barDataSet)
                data.barWidth = 0.5f

                setData(data)
            }
        }
    )
}