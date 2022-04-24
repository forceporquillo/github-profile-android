package dev.forcecodes.hov.core.model

sealed class UserUiModel {
    open val id: Int? = null

    data class User(
        override val id: Int,
        val name: String?
    ): UserUiModel()

    override fun equals(other: Any?): Boolean {
        if (other !is UserUiModel) {
            throw AssertionError()
        }
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
