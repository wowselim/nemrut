/*
 * This file is generated by jOOQ.
 */
package co.selim.nemrut.jooq;


import co.selim.nemrut.jooq.tables.FlywaySchemaHistory;
import co.selim.nemrut.jooq.tables.Note;
import co.selim.nemrut.jooq.tables.records.FlywaySchemaHistoryRecord;
import co.selim.nemrut.jooq.tables.records.NoteRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<NoteRecord> NOTE_PKEY = Internal.createUniqueKey(Note.NOTE, DSL.name("note_pkey"), new TableField[] { Note.NOTE.ID }, true);
}
