package co.selim.nemrut.db

import org.jooq.DSLContext

interface Database {
  fun <T : Any?> withDsl(block: (DSLContext) -> T): T

  fun <T : Any?> withTransaction(block: (DSLContext) -> T): T
}
