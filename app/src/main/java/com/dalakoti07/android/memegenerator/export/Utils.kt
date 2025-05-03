package com.dalakoti07.android.memegenerator.export

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
    val file = File(context.cacheDir, "$fileName.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return file
}
