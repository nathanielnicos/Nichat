package id.nns.nico_chat.utils

import id.nns.nico_chat.data.entity.UserEntity
import id.nns.nico_chat.data.response.google_sheet.UserResponse

object UserConverter {
    private fun responseToEntity(user: UserResponse): UserEntity =
        UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            photoUrl = user.photoUrl,
            isActive = user.isActive,
        )

    fun responseListToEntityList(stories: List<UserResponse>): List<UserEntity> {
        val newUsers: MutableList<UserEntity> = mutableListOf()
        stories.forEach {
            newUsers.add(
                responseToEntity(it)
            )
        }
        return newUsers
    }
}
