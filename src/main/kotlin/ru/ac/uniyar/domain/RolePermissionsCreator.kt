package ru.ac.uniyar.domain

import ru.ac.uniyar.domain.storage.RolePermissions
import ru.ac.uniyar.domain.storage.Store
import java.nio.file.Path
import java.util.UUID

val REGISTERED_USER_ROLE = RolePermissions.GUEST_ROLE.copy(
    id = UUID.fromString("08fc459c-db6d-11ec-9d64-0242ac120002"),
    name = "Автор рецептов",
    recipeCreation = true,
    recipeEdit = true,
    ingredientCreation = true,
)

val ADMIN_ROLE = RolePermissions.GUEST_ROLE.copy(
    id = UUID.fromString("6b3b4b90-db6d-11ec-9d64-0242ac120002"),
    name = "Администратор",
    ingredientCreation = true,
    ingredientAccept = true,
    ingredientEdit = true,
    recipeHide = true,
)

fun main(){
    val store = Store(Path.of("storage.json"))
    val permissionsRepository = store.rolePermissionsRepository
    permissionsRepository.add(REGISTERED_USER_ROLE)
    permissionsRepository.add(ADMIN_ROLE)
    store.save()
}
