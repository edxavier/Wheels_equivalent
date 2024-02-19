package com.edxavier.wheels_equivalent.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edxavier.wheels_equivalent.R

@Composable
fun Header() {
    val mContext = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = mContext.getString(R.string.titulo1), fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,  fontSize = 14.sp, modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = mContext.getString(R.string.subtitle), fontSize = 12.sp,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
            lineHeight = 14.sp
        )
    }
}