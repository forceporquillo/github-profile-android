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
import android.renderscript.Allocation.MipmapControl.MIPMAP_NONE
import android.renderscript.Element
import android.renderscript.RSRuntimeException
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object RenderScriptBlur {
    @Throws(RSRuntimeException::class)
    fun blur(
        context: Context?,
        bitmap: Bitmap?,
        radius: Int
    ): Bitmap? {
        var rs: RenderScript? = null
        var input: Allocation? = null
        var output: Allocation? = null
        var blur: ScriptIntrinsicBlur? = null
        try {
            rs = RenderScript.create(context)
            rs.messageHandler = RenderScript.RSMessageHandler()
            input = Allocation.createFromBitmap(
                rs, bitmap, MIPMAP_NONE,
                Allocation.USAGE_SCRIPT
            )
            output = Allocation.createTyped(rs, input.type)
            blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

            blur.apply {
                setInput(input)
                setRadius(radius.toFloat())
                forEach(output)
            }

            output.copyTo(bitmap)
        } finally {
            if (rs != null) {
                RenderScript.releaseAllContexts()
            }
            input?.destroy()
            output?.destroy()
            blur?.destroy()
        }
        return bitmap
    }
}