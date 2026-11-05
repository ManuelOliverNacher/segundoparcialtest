package ar.edu.examen.iiot.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.edu.examen.iiot.model.Reading
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    state: DashboardState,
    onFilterChanged: (String) -> Unit,
    onThresholdClick: () -> Unit,
    onHistoryClick: (String) -> Unit,
    onDismissNotification: () -> Unit
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Equipos monitoreados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Alertas: ${state.alertCount}",
                    color = if (state.alertCount > 0) Color(0xFFB3261E) else Color(0xFF256D41),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.filter,
                onValueChange = onFilterChanged,
                label = { Text("Buscar equipo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = onThresholdClick) {
                    Text("Umbral: ${state.threshold.toInt()} °C")
                }
                if (state.loading) {
                    Text("Sincronizando...", modifier = Modifier.padding(top = 12.dp))
                }
            }
            if (state.notification != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    color = Color(0xFFFFF3D9),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(state.notification, color = Color(0xFF5F4500))
                        Button(onClick = onDismissNotification) { Text("Cerrar aviso") }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            if (state.readings.isEmpty()) {
                Text(
                    "No hay equipos para mostrar.",
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.readings, key = { it.machineId }) { reading ->
                    EquipmentCard(reading, onHistoryClick)
                }
                item {
                    Text(
                        "Datos de demostración: ThingSpeak aporta una medición " +
                            "pública al Compresor A; el resto es simulado. " +
                            "Los sensores físicos corresponden al teléfono.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EquipmentCard(reading: Reading, onHistoryClick: (String) -> Unit) {
    val alert = reading.temperature >= 65.0
    val time = SimpleDateFormat("dd/MM HH:mm", Locale("es", "AR"))
        .format(Date(reading.timestamp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(reading.machineName, fontWeight = FontWeight.Bold)
                    Text(reading.machineId, style = MaterialTheme.typography.labelSmall)
                }
                Text(
                    if (alert) "ALERTA" else "NORMAL",
                    color = if (alert) Color(0xFFB3261E) else Color(0xFF238147),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "${"%.1f".format(reading.temperature)} °C",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Humedad ${"%.0f".format(reading.humidity)} %  ·  " +
                    "Vibración ${"%.1f".format(reading.vibration)} mm/s",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "$time · ${reading.source}",
                color = Color(0xFF64748B),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            OutlinedButton(
                onClick = { onHistoryClick(reading.machineId) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Ver historial")
            }
        }
    }
}
