package com.skopzz.melodycover.util

import kotlinx.serialization.json.Json

fun defaultJson(): Json = Json { encodeDefaults = true }