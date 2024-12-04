package com.skopzz.melodycover.cover

import com.skopzz.melodycover.R
import kotlinx.serialization.Serializable

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

  /**
   * 图片路径
   */
  var imagePath: String? = "/storage/emulated/0/Android/data/com.skopzz.melodycover/files/bar.coverImg",

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
