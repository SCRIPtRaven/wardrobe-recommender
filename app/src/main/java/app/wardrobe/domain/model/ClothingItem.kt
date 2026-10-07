package app.wardrobe.domain.model

/** One piece of clothing in the user's wardrobe. */
data class ClothingItem(
    val id: String,
    val name: String,
    val garment: Garment,
    val color: ItemColor,
    val warmth: Warmth,
    val seasons: Set<Season>,
) {
    val category: Category get() = garment.category
}

/** The four categories an outfit is built from. */
enum class Category { TOP, BOTTOM, FOOTWEAR, OUTERWEAR }

/** The kind of garment. It decides the category and the illustration shown until real photos exist. */
enum class Garment(val category: Category) {
    T_SHIRT(Category.TOP),
    SHIRT(Category.TOP),
    SWEATER(Category.TOP),
    JEANS(Category.BOTTOM),
    TROUSERS(Category.BOTTOM),
    SHORTS(Category.BOTTOM),
    SKIRT(Category.BOTTOM),
    SNEAKERS(Category.FOOTWEAR),
    BOOTS(Category.FOOTWEAR),
    JACKET(Category.OUTERWEAR),
    COAT(Category.OUTERWEAR),
}

/** How warm a garment is. The user sets it, since a photo can't show it. */
enum class Warmth { LIGHT, MEDIUM, WARM }

enum class Season { SPRING, SUMMER, AUTUMN, WINTER }
