package com.highestaim.exitsheet

import android.graphics.Path

data class FingerPath (
    var color:Int = 0,
    var emboss:Boolean = false,
    var blur:Boolean = false,
    var strokeWidth:Int = 0,
    var path: Path? = null
)