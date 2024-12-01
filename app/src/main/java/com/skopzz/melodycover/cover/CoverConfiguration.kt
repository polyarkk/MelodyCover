package com.skopzz.melodycover.cover

import android.graphics.PointF

class CoverConfiguration {
    var key: String = "";

    var name: String = "";

    /**
     * 类型（纯色/图片）
     */
    var barType: CoverType = CoverType.COLOR

    /**
     * 尺寸
     */
    var size: PointF = PointF(400f, 150f)

    /**
     * 颜色
     */
    var color: Int = 0x6aa6ff

    /**
     * 图片路径
     */
    var image: String? = null

    /**
     * 图片缩放比例
     */
    var imageScale: Float = 1f

    /**
     * 图片偏移，超出上隐条尺寸的部分将被隐藏
     */
    var imageOffset: PointF = PointF(0f, 0f)
}
