package com.seanshubin.condorcet.backend.service

object CharacterTypesPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        fun addCharacterType(map: Map<Int, Int>, codePoint: Int): Map<Int, Int> {
            val characterType = Character.getType(codePoint)
            val oldQuantity = map[characterType] ?: 0
            val newQuantity = oldQuantity + 1
            return map + Pair(characterType, newQuantity)
        }

        fun addCharacter(map: Map<Int, String>, codePoint: Int): Map<Int, String> {
            val characterType = Character.getType(codePoint)
            val oldValue = map[characterType] ?: ""
            val newValue = oldValue + Character.valueOf(codePoint.toChar())
            return map + Pair(characterType, newValue)
        }

        val quantities = (0..65535).fold(mapOf(), ::addCharacterType)
        quantities.toSortedMap().map { (key, value) ->
            Pair(CharacterTypeEnum.fromCode(key), value)
        }.forEach(::println)
        println(quantities.values.sum())

        val values = (0..65535).fold(mapOf(), ::addCharacter)
        values.toSortedMap().map { (key, value) ->
            val trimmedValue = if (value.length > 1000) value.substring(0, 1000) else value
            Pair(CharacterTypeEnum.fromCode(key), trimmedValue)
        }.forEach(::println)
    }

    enum class CharacterTypeEnum(val code: Int) {
        UNASSIGNED(0),
        UPPERCASE_LETTER(1),
        LOWERCASE_LETTER(2),
        TITLECASE_LETTER(3),
        MODIFIER_LETTER(4),
        OTHER_LETTER(5),
        NON_SPACING_MARK(6),
        ENCLOSING_MARK(7),
        COMBINING_SPACING_MARK(8),
        DECIMAL_DIGIT_NUMBER(9),
        LETTER_NUMBER(10),
        OTHER_NUMBER(11),
        SPACE_SEPARATOR(12),
        LINE_SEPARATOR(13),
        PARAGRAPH_SEPARATOR(14),
        CONTROL(15),
        FORMAT(16),
        PRIVATE_USE(18),
        SURROGATE(19),
        DASH_PUNCTUATION(20),
        START_PUNCTUATION(21),
        END_PUNCTUATION(22),
        CONNECTOR_PUNCTUATION(23),
        OTHER_PUNCTUATION(24),
        MATH_SYMBOL(25),
        CURRENCY_SYMBOL(26),
        MODIFIER_SYMBOL(27),
        OTHER_SYMBOL(28),
        INITIAL_QUOTE_PUNCTUATION(29),
        FINAL_QUOTE_PUNCTUATION(30);

        companion object {
            fun fromCode(code: Int): CharacterTypeEnum =
                values().find { code == it.code } ?: throw UnsupportedOperationException("Unsupported code $code")
        }
    }
}
