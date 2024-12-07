package com.skopzz.melodycover.cover

import com.skopzz.melodycover.COVER_DIR
import com.skopzz.melodycover.R
import com.skopzz.melodycover.util.defaultJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
data class CoverConfiguration(
  var key: String = "default",

  var name: String = "默认",

  /**
   * 类型（纯色/图片）
   */
  var coverType: CoverType = CoverType.IMAGE,

  /**
   * 颜色
   */
  var color: ULong = 0xffff6a6au,

  /**
   * 尺寸
   */
  var width: Int = 400,

  var height: Int = 150,

  var imageId: Int = R.drawable.sample_image,

  /**
   * 图片缩放比例
   */
  var imageScale: Float = 1f,

  /**
   * 图片四周隐藏的大小，最大值为图片高度或宽度 * 缩放比例
   */
  var imageInsetL: Int = 0,
  var imageInsetR: Int = 0,
  var imageInsetT: Int = 0,
  var imageInsetB: Int = 0,

  var imageUpdateMs: Long = System.currentTimeMillis(),
)

fun CoverConfiguration.getImagePath(): String = "$COVER_DIR/$key/cover_img"
fun CoverConfiguration.getConfigPath(): String = "$COVER_DIR/$key/config.json"

fun existsCoverConfiguration(key: String): Boolean = File("$COVER_DIR/$key/config.json").exists()

fun saveCoverConfiguration(conf: CoverConfiguration) {
  val json = defaultJson().encodeToString(conf)

  val file = File(conf.getConfigPath())

  if (file.exists()) {
    file.delete()
  } else {
    file.parentFile?.mkdirs()
  }

  file.createNewFile()
  file.writeText(json)
}

fun loadCoverConfiguration(key: String): CoverConfiguration? {
  val file = File("$COVER_DIR/$key/config.json")

  if (!file.exists()) {
    return null
  }

  val json = file.readText()

  return defaultJson().decodeFromString(json)
}
