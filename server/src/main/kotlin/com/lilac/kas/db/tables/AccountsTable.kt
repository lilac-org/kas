package com.lilac.kas.db.tables

import com.lilac.kas.domain.enum.AccountType
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable

object AccountsTable: UUIDTable("accounts") {
    val userId = uuid("user_id").index()
    val name = varchar("name", 100).index()
    val color = varchar("color", 16).nullable()
    val type = enumerationByName<AccountType>("type", 32)
    val balance = decimal("balance", 12, 2)
    val createdAt = long("created_at").clientDefault { System.currentTimeMillis() }
    val updatedAt = long("updated_at").clientDefault { System.currentTimeMillis() }
}