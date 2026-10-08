package app.wardrobe.domain.model

/** What image recognition thinks a photographed item is. The user confirms or corrects it. */
data class ItemSuggestion(val garment: Garment, val color: ItemColor)
