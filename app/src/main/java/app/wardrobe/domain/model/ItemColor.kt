package app.wardrobe.domain.model

/**
 * The main color of a garment, from a fixed set the user can pick from.
 *
 * [argb] is a representative swatch. [isNeutral] marks colors that pair with almost anything,
 * which the outfit rules treat differently from accent colors.
 */
enum class ItemColor(val argb: Long, val isNeutral: Boolean) {
    WHITE(0xFFF4F2EE, isNeutral = true),
    CREAM(0xFFEADFC8, isNeutral = true),
    BEIGE(0xFFD5BE98, isNeutral = true),
    CAMEL(0xFFB4884F, isNeutral = true),
    BROWN(0xFF6B4A30, isNeutral = true),
    GREY(0xFF8C8C8A, isNeutral = true),
    BLACK(0xFF242424, isNeutral = true),
    NAVY(0xFF26324D, isNeutral = true),
    DENIM(0xFF4B6585, isNeutral = true),
    LIGHT_BLUE(0xFFA8C3DF, isNeutral = false),
    BLUE(0xFF3C67A5, isNeutral = false),
    GREEN(0xFF3F7A50, isNeutral = false),
    OLIVE(0xFF6A6E3B, isNeutral = false),
    YELLOW(0xFFE0BE45, isNeutral = false),
    ORANGE(0xFFD57F3B, isNeutral = false),
    RED(0xFFB0393A, isNeutral = false),
    BURGUNDY(0xFF6D2335, isNeutral = false),
    PINK(0xFFE5A8B4, isNeutral = false),
    PURPLE(0xFF6B4E8A, isNeutral = false),
}
