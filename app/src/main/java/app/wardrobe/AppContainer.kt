package app.wardrobe

import app.wardrobe.data.InMemoryWardrobeRepository
import app.wardrobe.data.SampleData
import app.wardrobe.data.WardrobeRepository

/** Creates the app's shared objects. Screens get their dependencies from here. */
class AppContainer(
    val wardrobeRepository: WardrobeRepository = InMemoryWardrobeRepository(SampleData.items),
)
