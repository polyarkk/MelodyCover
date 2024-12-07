package com.skopzz.melodycover.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
fun defaultJson(): Json = Json {
  encodeDefaults = true
  prettyPrint = true
  prettyPrintIndent = " ".repeat(2)
}