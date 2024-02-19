package com.edxavier.wheels_equivalent.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edxavier.wheels_equivalent.R
import com.edxavier.wheels_equivalent.db.WheelData
import java.util.*

@Composable
fun ResultsCard(
    percDiff: Float,
    speedDiff: Float,
    stdWheel: WheelData,
    newWheel: WheelData,
    stdWheelDiameter: Int,
    newWheelDiameter: Int,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ){
        val mContext = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            Text(
                text =mContext.getString(R.string.resultados), modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onTertiary
            )
        }
        val textModifier = Modifier.weight(4f)
        val dataModifier = Modifier.weight(3f)
        val titlesStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onTertiaryContainer)
        val dataStyle = TextStyle(fontSize = 14.sp, textAlign = TextAlign.Center)
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                .padding(8.dp)
        ){
            Text(text = mContext.getString(R.string.data), modifier = textModifier, style = titlesStyle)
            Text(text = mContext.getString(R.string.paso1), modifier = dataModifier, style = titlesStyle, textAlign = TextAlign.Center)
            Text(text = mContext.getString(R.string.paso2), modifier = dataModifier, style = titlesStyle, textAlign = TextAlign.Center)
        }
        val percColor = if(percDiff in (-3f..3f)) Color(0xff388e3c) else Color.Red
        val sign = if(percDiff >0) "+" else ""
        val newWheelText = buildAnnotatedString{
            append("$newWheelDiameter ")
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.SemiBold, color = percColor,
                fontSize = 12.sp
            )) {
                append("($sign${String.format(Locale.getDefault(), "%.2f", percDiff)}%)")
            }
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(text = "${mContext.getString(R.string.diameter_label)} (mm)", modifier = textModifier, style = titlesStyle)
            Text(text = stdWheelDiameter.toString(), modifier = dataModifier, style = dataStyle)
            Text(text = newWheelText, modifier = dataModifier, style = dataStyle)
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(text = "${mContext.getString(R.string.max_load_label)} (kg)", modifier = textModifier, style = titlesStyle)
            Text(text = stdWheel.load.valor, modifier = dataModifier, style = dataStyle)
            Text(text = newWheel.load.valor, modifier = dataModifier, style = dataStyle)
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(text = "${mContext.getString(R.string.max_speed_label)} (km/h)", modifier = textModifier, style = titlesStyle)
            Text(text = stdWheel.speed.valor, modifier = dataModifier, style = dataStyle)
            Text(text = newWheel.speed.valor, modifier = dataModifier, style = dataStyle)
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(text = "${mContext.getString(R.string.speedometer)} (km/h)", modifier = textModifier, style = titlesStyle)
            Text(text = "100", modifier = dataModifier, style = dataStyle)
            Text(text = String.format(Locale.getDefault(), "%.2f", speedDiff), modifier = dataModifier, style = dataStyle)
        }

        Divider(modifier = Modifier
            .padding(8.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseSurface))
        val resultText = buildAnnotatedString{
            append(mContext.getString(R.string.dif_porc))
            withStyle(style = SpanStyle(
                fontWeight = FontWeight.SemiBold, color = percColor,
            )) {
                append(" $sign${String.format(Locale.getDefault(), "%.2f", percDiff)}% ")
            }
            if (percDiff >= 0)
                append(mContext.getString(R.string.dif_porc_mas))
            else
                append(mContext.getString(R.string.dif_porc_menos))
            if(percDiff in (-3f..3f))
                append(mContext.getString(R.string.dif_porc_si))
            else
                append(mContext.getString(R.string.dif_porc_no))

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (percDiff in (-3f..3f)) Color(0xffe8f5e9) else Color(0xffffebee))
        ) {
            Column {
                Text(
                    text = resultText, textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontSize = 13.sp
                )
                Text(
                    text = mContext.getString(R.string.recuerda1),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    fontSize = 13.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}