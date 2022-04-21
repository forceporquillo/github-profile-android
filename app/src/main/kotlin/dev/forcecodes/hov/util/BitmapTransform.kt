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

package dev.forcecodes.hov.util

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.request.target.Target
import java.security.MessageDigest

abstract class BitmapTransform : Transformation<Bitmap> {
    override fun transform(
        context: Context, resource: Resource<Bitmap>, outWidth: Int, outHeight: Int
    ): Resource<Bitmap> {
        val bitmapPool = Glide.get(context).bitmapPool
        val toTransform = resource.get()
        val targetWidth = if (outWidth == Target.SIZE_ORIGINAL) toTransform.width else outWidth
        val targetHeight = if (outHeight == Target.SIZE_ORIGINAL) toTransform.height else outHeight
        val appContext = context.applicationContext
        val transformed = transform(appContext, bitmapPool, toTransform, targetWidth, targetHeight)
        return if (toTransform == transformed) resource
        else (BitmapResource.obtain(transformed, bitmapPool)!!)
    }

    abstract fun transform(
        context: Context,
        pool: BitmapPool,
        toTransForm: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap

    fun setCanvasBitmapDensity(toTransform: Bitmap, canvasBitmap: Bitmap) {
        canvasBitmap.density = toTransform.density
    }

    abstract override fun updateDiskCacheKey(messageDigest: MessageDigest)
}