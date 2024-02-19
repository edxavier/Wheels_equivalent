package com.edxavier.wheels_equivalent.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edxavier.wheels_equivalent.R
import com.edxavier.wheels_equivalent.db.WheelDataDto
import com.pixplicity.easyprefs.library.Prefs

@Composable
fun CardWheelData(
    cardTitle: String? = null,
    cardSubTitle: String? = null,
    data: WheelDataDto,
    codePrefix: String,
    onCardItemSelected: (index: Int, code:String) -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ){
        val mContext = LocalContext.current
        var widthIndex by remember { mutableStateOf(Prefs.getInt("${codePrefix}ai", 4)) }
        var profileIndex by remember { mutableStateOf(Prefs.getInt("${codePrefix}pi", 4)) }
        var rimIndex by remember { mutableStateOf(Prefs.getInt("${codePrefix}di", 4)) }
        var loadIndex by remember { mutableStateOf(Prefs.getInt("${codePrefix}ci", 4)) }
        var speedIndex by remember { mutableStateOf(Prefs.getInt("${codePrefix}vi", 4)) }

        cardTitle?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                Column {
                    Text(
                        text = cardTitle, modifier = Modifier.fillMaxWidth().padding(top=8.dp),
                        textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                    )
                    cardSubTitle?.let{
                        Text(
                            text = cardSubTitle, modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LargeDropdownMenu(
                    label = mContext.getString(R.string.ancho),
                    items = data.widths,
                    selectedIndex = widthIndex,
                    onItemSelected = { index, _ ->
                        widthIndex = index
                        onCardItemSelected(index, "${codePrefix}ai")
                    },
                    modifier = Modifier.weight(1f)
                )
                LargeDropdownMenu(
                    label = mContext.getString(R.string.perfil),
                    items = data.profiles,
                    selectedIndex = profileIndex,
                    onItemSelected = { index, _ ->
                        profileIndex = index
                        onCardItemSelected(index, "${codePrefix}pi")
                    },
                    modifier = Modifier.weight(1f)
                )
                LargeDropdownMenu(
                    label = mContext.getString(R.string.diametro),
                    items = data.rims,
                    selectedIndex = rimIndex,
                    onItemSelected = { index, _ -> rimIndex = index
                        onCardItemSelected(index, "${codePrefix}di")
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LargeDropdownMenu(
                    label = mContext.getString(R.string.indice_carga),
                    items = data.loads,
                    selectedIndex = loadIndex,
                    onItemSelected = { index, _ -> loadIndex = index
                        onCardItemSelected(index, "${codePrefix}ci")
                    },
                    modifier = Modifier.weight(1f)
                )
                LargeDropdownMenu(
                    label = mContext.getString(R.string.indice_velocidad),
                    items = data.speeds,
                    selectedIndex = speedIndex,
                    onItemSelected = { index, _ -> speedIndex = index
                        onCardItemSelected(index, "${codePrefix}vi")
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            content?.let {
                content()
            }
        }
    }
}