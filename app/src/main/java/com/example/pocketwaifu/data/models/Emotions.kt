package com.example.pocketwaifu.data.models

enum class Emotions(val key: String) {
    JOY("joy"),
    ANGER("anger"),
    LOVE("love"),
    FEAR("fear"),
    NEUTRAL("neutral");

    companion object {
        fun fromString(value: String?): Emotions {
            return entries.find { it.key == value?.lowercase() } ?: NEUTRAL
        }
    }
}