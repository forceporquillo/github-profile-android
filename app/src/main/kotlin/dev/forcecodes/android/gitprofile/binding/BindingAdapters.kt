package dev.forcecodes.android.gitprofile.binding

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.view.postOnAnimationDelayed
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import dev.forcecodes.android.gitprofile.util.BlurTransformation
import dev.forcecodes.android.gitprofile.util.GlideApp
import dev.forcecodes.android.gitprofile.util.toUserContentUri
import dev.forcecodes.gitprofile.core.model.empty
import dev.forcecodes.gitprofile.domain.usecase.details.DetailsViewState

@BindingAdapter("loadImage")
fun ImageView.loadImage(id: Int) {
    GlideApp.with(context)
        .asBitmap()
        .signature(ObjectKey(id))
        .override(width, height)
        .load(id.toString().toUserContentUri)
        .into(this)

}

@BindingAdapter("blurImage")
fun ImageView.bindBlurImage(id: Int) {
    GlideApp.with(context)
        .asBitmap()
        .signature(ObjectKey(id))
        .load(id.toString().toUserContentUri)
        .override(width, height)
        .apply(RequestOptions.bitmapTransform(BlurTransformation()))
        .into(this)
}

@BindingAdapter(value = ["followers", "following"], requireAll = true)
fun TextView.spannable(followers: String?, following: String?) {
    val spannable = SpannableStringBuilder()
        .bold {
            append(followers.empty())
        }
        .append(" followers")
        .append(" • ")
        .bold {
            append(following.empty())
        }.append(" following")
    text = spannable
}

@BindingAdapter("delayedVisibility")
fun View.delayedVisibility(viewState: DetailsViewState) {
    if (!viewState.isForceRefresh) {
        postOnAnimationDelayed(500L) {
            visibility = if (viewState.isLoading) View.VISIBLE else View.GONE
        }
    }
}

@BindingAdapter("profileView")
fun ImageView.profileView(viewState: DetailsViewState) {
    if (!viewState.isForceRefresh) {
        viewState.data?.id?.let { loadImage(it) }
    }
}
