package co.selim.nemrut.db

import org.jooq.DSLContext

interface Database {
  suspend fun <T : Any?> withDsl(block: suspend (DSLContext) -> T): T
}
