package co.selim.nemrut.db

import io.vertx.core.Future
import org.jooq.DSLContext

interface Database {
  fun <T : Any?> withDsl(block: (DSLContext) -> T): Future<T>

  fun <T : Any?> withTransaction(block: (DSLContext) -> T): Future<T>
}
