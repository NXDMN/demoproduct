package com.nxdmn.demoproduct

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import java.io.IOException

fun readImageFromPath(context: Context, path: String): Bitmap?{
    if(path == "") return null
    var bitmap: Bitmap? = null
    val uri = Uri.parse(path)
    if(uri != null){
        try {
            context.contentResolver.apply{
                takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
        catch (e: IOException){
            e.printStackTrace()
        }
    }

    return bitmap
}