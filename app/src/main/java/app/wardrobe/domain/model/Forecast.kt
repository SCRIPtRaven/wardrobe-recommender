package app.wardrobe.domain.model

import java.time.LocalDate

/** Daily forecast for the user's chosen location. */
data class Forecast(
    val location: String,
    val days: List<DayForecast>,
)

data class DayForecast(
    val date: LocalDate,
    val minTemperatureC: Int,
    val maxTemperatureC: Int,
    val condition: WeatherCondition,
    val precipitationChancePercent: Int,
)

enum class WeatherCondition { CLEAR, PARTLY_CLOUDY, CLOUDY, RAIN, SNOW }
