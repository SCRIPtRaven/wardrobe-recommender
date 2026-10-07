package app.wardrobe

import android.app.Application

class WardrobeApplication : Application() {
    val container: AppContainer by lazy { AppContainer() }
}
