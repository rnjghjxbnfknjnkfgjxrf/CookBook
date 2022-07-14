package ru.ac.uniyar.domain.queries

import ru.ac.uniyar.domain.storage.RolePermissionsRepository
import java.util.UUID

class FetchPermissionsViaToken(private val permissionsRepository: RolePermissionsRepository) {

    operator fun invoke(token: UUID) = permissionsRepository.fetch(token)
}
