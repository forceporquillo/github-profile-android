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

package dev.forcecodes.android.gitprofile.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.RSRuntimeException
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

class BlurTransformation : BitmapTransform() {

    companion object {
        private const val RADIUS = 25
        private const val DOWN_SAMPLING = 10
    }

    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransForm: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {

        val scaleWidth = toTransForm.width / DOWN_SAMPLING
        val scaleHeight = toTransForm.height / DOWN_SAMPLING

        val bitmap = pool.get(scaleWidth, scaleHeight, Bitmap.Config.ARGB_8888)

        setCanvasBitmapDensity(toTransForm, bitmap)

        val canvas = Canvas(bitmap)
        DOWN_SAMPLING.toFloat().let { canvas.scale(1 / it, 1 / it) }
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransForm, 0f, 0f, paint)

        return try {
            RenderScriptBlur.blur(context, bitmap, RADIUS)!!
        } catch (exception: RSRuntimeException) {
            FastBlurAlgorithm.blur(bitmap, RADIUS, true)!!
        }
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(("dev.forcecodes.android.gitprofile$RADIUS$DOWN_SAMPLING").toByteArray(Key.CHARSET))
    }
}