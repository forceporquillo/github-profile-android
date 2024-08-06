package dev.forcecodes.gitprofile.data.prefs

import android.content.Context
import javax.inject.Inject
import kotlin.math.max

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
    @dagger.hilt.android.qualifiers.ApplicationContext context: Context
) : LinkPreference {

    private val prefs by context.nextLinkSharedPref()

    override var nextPage: Int? = prefs.getInt(NEXT_PAGE, 0)
        set(value) {
            if (field != value) {
                if (value != null) {
                    val page = max(field ?: 0, value)
                    val prefsEditor = prefs.edit()
                    prefsEditor.putInt(NEXT_PAGE, page).apply()
                    field = page
                }
            }
        }

}

fun Context.nextLinkSharedPref() = lazy {
    getSharedPreferences(USER_NEXT_LINK, Context.MODE_PRIVATE)
}

private const val NEXT_PAGE = "next-page"
private const val USER_NEXT_LINK = "user_next_link"