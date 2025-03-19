package com.ainotebook.data.model

enum class SortOption(val displayName: String) {
    CREATED_DESC("Latest First"),
    CREATED_ASC("Oldest First"),
    MODIFIED_DESC("Recently Modified"),
    MODIFIED_ASC("Least Recently Modified"),
    TITLE_ASC("Title A-Z"),
    TITLE_DESC("Title Z-A"),
    TAG_COUNT_DESC("Most Tags"),
    TAG_COUNT_ASC("Least Tags")
} 