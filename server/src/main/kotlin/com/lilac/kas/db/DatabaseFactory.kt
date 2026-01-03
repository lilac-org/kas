package com.lilac.kas.db

import com.lilac.kas.db.tables.AccountsTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {
    fun Application.configureDatabase() {
        val cfg = environment.config

        val jdbcUrl = cfg.propertyOrNull("postgres.url")?.getString() ?: error("Missing postgres.url")
        val username = cfg.propertyOrNull("postgres.username")?.getString() ?: error("Missing postgres.username")
        val password = cfg.propertyOrNull("postgres.password")?.getString() ?: error("Missing postgres.password")
        val maximumPoolSize = cfg.propertyOrNull("datasource.maximumPoolSize")?.getString()?.toInt() ?: 10

        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            this.maximumPoolSize = maximumPoolSize
            this.isAutoCommit = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }

        Database.connect(HikariDataSource(config))

        createTables()
    }

    private fun createTables() = transaction {
        SchemaUtils.create(
            AccountsTable
        )
        ensureUpdatedAtFunction()

        applyUpdatedAtTrigger(AccountsTable.tableName)
    }

    private fun JdbcTransaction.ensureUpdatedAtFunction() {
        exec(
            """
        CREATE OR REPLACE FUNCTION set_updated_at()
        RETURNS TRIGGER AS $$
        BEGIN
            NEW.updated_at = (EXTRACT(EPOCH FROM NOW()) * 1000)::BIGINT;
            RETURN NEW;
        END;
        $$ LANGUAGE plpgsql;
        """.trimIndent()
        )
    }

    private fun JdbcTransaction.applyUpdatedAtTrigger(
        tableName: String,
        triggerName: String = "${tableName}_set_updated_at"
    ) {
        exec(
            """
        DO $$
        BEGIN
            IF NOT EXISTS (
                SELECT 1 FROM pg_trigger WHERE tgname = '$triggerName'
            ) THEN
                CREATE TRIGGER $triggerName
                BEFORE UPDATE ON $tableName
                FOR EACH ROW
                EXECUTE FUNCTION set_updated_at();
            END IF;
        END;
        $$;
        """.trimIndent()
        )
    }
}