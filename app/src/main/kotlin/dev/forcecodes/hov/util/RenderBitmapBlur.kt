/*
 * Copyright 2020 strongforce1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("deprecation")

package dev.forcecodes.hov.util

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log

object RenderBitmapBlur {

    fun blurRenderScript(context: Context, smallBitmap: Bitmap, radius: Int): Bitmap? {
        var newSmallBitmap: Bitmap? = null
        try {
            newSmallBitmap = convertRGB565toARGB888(smallBitmap)
        } catch (exception: Exception) {
            Log.e("RenderBitmapBlur", exception.toString())
        }

        val bitmap = Bitmap.createBitmap(
            newSmallBitmap!!.width, smallBitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val renderScript = RenderScript.create(context)

        val blurInput = Allocation.createFromBitmap(renderScript, newSmallBitmap)
        val blurOutput = Allocation.createFromBitmap(renderScript, bitmap)

        val blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

        blur.apply {
            setInput(blurInput)
            setRadius(radius.toFloat())
            forEach(blurInput)
        }

        blurOutput.copyTo(bitmap)
        renderScript.destroy()

        return bitmap
    }

    private fun convertRGB565toARGB888(image: Bitmap): Bitmap = with(image) {
        val numPixels = width * height
        val pixels = IntArray(numPixels)

        getPixels(pixels, 0, width, 0, 0, width, height)

        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
        return result
    }
}