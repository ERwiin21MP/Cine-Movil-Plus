package com.erwiin21mp.cinemovilplus.core.ext

import java.text.Normalizer

fun String.removeAccents(): String = Normalizer.normalize(this, Normalizer.Form.NFD)
    .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")