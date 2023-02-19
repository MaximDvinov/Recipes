package com.dvinov.recipes

import kotlin.time.Duration

fun String.getMinute(): Long {
    if (this.isBlank()){
        return 0
    }
    return Duration.parse(this).inWholeMinutes
}

fun Int.getDifficultyString(): String = when (this) {
    0, 1 -> "easy"
    2 -> "medium"
    else -> "hard"
}

