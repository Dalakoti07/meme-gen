package com.dalakoti07.android.memegenerator.nativefilters

import android.graphics.Bitmap

object NativeFilters {
    init {
        System.loadLibrary("nativefilters")
    }

    external fun applyTint(bitmap: Bitmap, argbColor: Int, strength: Float)
    external fun applySketch(bitmap: Bitmap, edgeSigma: Float, shadeSigma: Float)
}


