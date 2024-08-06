@file:Suppress("Deprecation")

package dev.forcecodes.android.gitprofile.ui.details

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import dev.forcecodes.android.gitprofile.ui.details.orgs.OrganizationsFragment
import dev.forcecodes.android.gitprofile.ui.details.repos.RepositoriesFragment
import dev.forcecodes.android.gitprofile.ui.details.starred.StarredRepositoriesFragment

internal const val MAX_PAGER_SIZE = 3

class DetailsStatePagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    private val titles = listOf("Repositories", "Organizations", "Starred")

    override fun getCount(): Int {
        return MAX_PAGER_SIZE
    }

    override fun getItem(position: Int): BaseDetailsFragment {
        return when (position) {
            0 -> RepositoriesFragment()
            1 -> OrganizationsFragment()
            2 -> StarredRepositoriesFragment()
            else -> throw IllegalStateException()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

}
