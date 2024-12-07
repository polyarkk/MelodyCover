package com.skopzz.melodycover.util

import androidx.compose.ui.graphics.Color

fun isValidArgbHexString(s: String): Boolean {
  val offset = if (s[0] == '#') {
    if (s.length != 9) {
      return false
    }

    1
  } else if (s.length == 8) {
    return false
  } else {
    0
  }

  for (i in (0 + offset)..<(8 + offset)) {
    val ch = s[i]

    if (!ch.isDigit() && (ch < 'A' || ch > 'F') && (ch < 'a' || ch > 'f')) {
      return false
    }
  }

  return true;
}

fun Color.toHexString(): String = "#${(value shr 32).toString(16)}"
fun Color.toHex(): ULong = value shr 32
fun Color.Companion.fromHexString(s: String): Color =
  Color(
    if (s.startsWith("#")) {
      s.substring(1..<s.length)
    } else {
      s
    }.toULong(16) shl 32
  )

fun Color.Companion.fromHex(u: ULong): Color = Color(u shl 32)