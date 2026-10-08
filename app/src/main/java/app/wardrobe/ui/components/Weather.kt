package app.wardrobe.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.wardrobe.R
import app.wardrobe.domain.model.DayForecast
import app.wardrobe.domain.model.WeatherCondition
import app.wardrobe.ui.label
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@DrawableRes
fun WeatherCondition.icon(): Int = when (this) {
    WeatherCondition.CLEAR -> R.drawable.ic_sunny
    WeatherCondition.PARTLY_CLOUDY -> R.drawable.ic_partly_cloudy_day
    WeatherCondition.CLOUDY -> R.drawable.ic_cloud
    WeatherCondition.RAIN -> R.drawable.ic_rainy
    WeatherCondition.SNOW -> R.drawable.ic_weather_snowy
}

/** "Today", "Tomorrow", or a short date such as "Sat 10". */
@Composable
fun dayLabel(date: LocalDate, today: LocalDate): String = when (date) {
    today -> stringResource(R.string.day_today)
    today.plusDays(1) -> stringResource(R.string.day_tomorrow)
    // INFO(limit): English day names to match the English-only UI. Use the app locale once translations exist.
    else -> date.format(DateTimeFormatter.ofPattern("EEE d", Locale.ENGLISH))
}

/** The weather for one day: condition, temperature range and chance of precipitation. */
@Composable
fun WeatherSummary(location: String, dayLabel: String, day: DayForecast, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Icon(painterResource(day.condition.icon()), contentDescription = null, modifier = Modifier.size(48.dp))
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.weather_place_day, location, dayLabel),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(stringResource(day.condition.label()), style = MaterialTheme.typography.titleLarge)
                Text(
                    text = stringResource(R.string.weather_temperature_range, day.minTemperatureC, day.maxTemperatureC),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(R.string.weather_precipitation, day.precipitationChancePercent),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
