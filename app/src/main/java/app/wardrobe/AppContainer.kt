package app.wardrobe

import app.wardrobe.data.InMemoryWardrobeRepository
import app.wardrobe.data.SampleData
import app.wardrobe.data.SampleRecognizer
import app.wardrobe.data.WardrobeRepository
import app.wardrobe.domain.model.Forecast
import java.time.LocalDate

/** Creates the app's shared objects. Screens get their dependencies from here. */
class AppContainer(
    val wardrobeRepository: WardrobeRepository = InMemoryWardrobeRepository(SampleData.items),
    val recognizer: SampleRecognizer = SampleRecognizer(SampleData.photoSuggestions),
) {
    /** The week ahead, starting today. Milestone 1 uses the sample Kaunas week. */
    fun forecast(): Forecast = SampleData.forecast(LocalDate.now())
}
