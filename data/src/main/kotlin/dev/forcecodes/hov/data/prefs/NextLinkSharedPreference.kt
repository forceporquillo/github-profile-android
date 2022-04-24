package dev.forcecodes.hov.data.prefs

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
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

    private val prefs by lazy {
        context.getSharedPreferences(USER_NEXT_LINK, Context.MODE_PRIVATE)
    }

    override var nextPage: Int? = prefs.getInt(NEXT_PAGE, 0)
        set(value) {
            if (field != value) {
                val prefsEditor = prefs.edit()
                if (value != null) {
                    prefsEditor.putInt(NEXT_PAGE, value).apply()
                    field = value
                }
            }
        }

    companion object {
        private const val NEXT_PAGE = "next-page"
        private const val USER_NEXT_LINK = "user_next_link"
    }

}