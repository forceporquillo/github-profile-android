package dev.forcecodes.hov.data.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.forcecodes.hov.data.utils.NextPageIndexer
import javax.inject.Inject

/**
 * Concrete class implementation for storing the next page index ID from API get users response.
 *
 * Since there is no built-in implementation of pagination in the get [/users] API endpoint.
 * Therefore, we have to manually look it up on its header response instead.
 *
 * @see
 * https://docs.github.com/en/rest/users/users
 */
class NextLinkSharedPreference @Inject constructor(
    @ApplicationContext context: Context
) : LinkPreference {

    private val prefs by context.nextLinkSharedPref()

    override var nextPage: Int? = prefs.getInt(NEXT_PAGE, 0)
        set(value) {
            if (field != value) {
                if (value != null && value > (field ?: 0)) {
                    val prefsEditor = prefs.edit()
                    prefsEditor.putInt(NEXT_PAGE, value).apply()
                    field = value
                }
            }
        }

}

fun Context.nextLinkSharedPref() = lazy {
    getSharedPreferences(USER_NEXT_LINK, Context.MODE_PRIVATE)
}

private const val NEXT_PAGE = "next-page"
private const val USER_NEXT_LINK = "user_next_link"