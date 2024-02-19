package com.edxavier.wheels_equivalent.screens

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edxavier.wheels_equivalent.R
import java.util.Locale


@Composable
fun SuggestionsScreen(
    viewModel: WheelViewModel
) {
    val ctx = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    Column {
        val mod = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
        Text(
            text = ctx.getString(R.string.suggestions_note), lineHeight = 13.sp,
            fontSize = 12.sp, textAlign = TextAlign.Center, modifier = mod,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "${ctx.getString(R.string.eq_title)}\n${state.stdWheel} -> ${ctx.getString(R.string.eq_vel_variacion)}",
            fontSize = 16.sp, textAlign = TextAlign.Center, modifier = mod,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        for (sugg in state.wheelSuggestions.reversed()){
            val speedDiffStr = String.format(Locale.getDefault(), "%.2f", sugg.speedDiff)
            HorizontalDivider()
            Text(
                text = "$sugg  ->  ${speedDiffStr.padStart(6, '0')} km/h",
                fontSize = 15.sp, textAlign = TextAlign.Justify, modifier = mod,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}