package com.edxavier.wheels_equivalent.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MultiToggleButton(
    modifier: Modifier = Modifier,
    defaultSelected: Int = 0,
    toggleOptions: List<String>,
    onToggleChange: (Int) -> Unit
) {
    val selectedTint = MaterialTheme.colorScheme.primary
    val unselectedTint = Color(0xFFDDE6ED)
    var selection by remember { mutableIntStateOf(defaultSelected) }


    Row(modifier = modifier
        .height(IntrinsicSize.Min)
        .clip(RoundedCornerShape(8.dp))
    ) {
        toggleOptions.forEachIndexed { index, toggleState ->
            val isSelected = selection == index
            val backgroundTint = if (isSelected) selectedTint else unselectedTint
            val textColor = if (isSelected) Color.White else Color.Unspecified

            if (index != 0) {
                VerticalDivider()
            }
            Row(
                modifier = Modifier
                    .background(backgroundTint)
                    .weight(1f)
                    .toggleable(
                        value = isSelected,
                        enabled = true,
                        onValueChange = { selected ->
                            if (selected) {
                                onToggleChange(index)
                                selection = index
                            }
                        })
            ) {
                Text(
                    toggleState, color = textColor,
                    modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = if(isSelected) FontWeight.SemiBold else null
                )
            }

        }
    }
}