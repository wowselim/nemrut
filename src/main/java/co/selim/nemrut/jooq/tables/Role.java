/*
 * This file is generated by jOOQ.
 */
package co.selim.nemrut.jooq.tables;


import co.selim.nemrut.jooq.Keys;
import co.selim.nemrut.jooq.Public;
import co.selim.nemrut.jooq.tables.records.RoleRecord;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Role extends TableImpl<RoleRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.role</code>
     */
    public static final Role ROLE = new Role();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RoleRecord> getRecordType() {
        return RoleRecord.class;
    }

    /**
     * The column <code>public.role.id</code>.
     */
    public final TableField<RoleRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field("gen_random_uuid()", SQLDataType.UUID)), this, "");

    /**
     * The column <code>public.role.title</code>.
     */
    public final TableField<RoleRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.role.created_at</code>.
     */
    public final TableField<RoleRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.role.updated_at</code>.
     */
    public final TableField<RoleRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");

    private Role(Name alias, Table<RoleRecord> aliased) {
        this(alias, aliased, null);
    }

    private Role(Name alias, Table<RoleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.role</code> table reference
     */
    public Role(String alias) {
        this(DSL.name(alias), ROLE);
    }

    /**
     * Create an aliased <code>public.role</code> table reference
     */
    public Role(Name alias) {
        this(alias, ROLE);
    }

    /**
     * Create a <code>public.role</code> table reference
     */
    public Role() {
        this(DSL.name("role"), null);
    }

    public <O extends Record> Role(Table<O> child, ForeignKey<O, RoleRecord> key) {
        super(child, key, ROLE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<RoleRecord> getPrimaryKey() {
        return Keys.ROLE_PKEY;
    }

    @Override
    public Role as(String alias) {
        return new Role(DSL.name(alias), this);
    }

    @Override
    public Role as(Name alias) {
        return new Role(alias, this);
    }

    @Override
    public Role as(Table<?> alias) {
        return new Role(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Role rename(String name) {
        return new Role(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Role rename(Name name) {
        return new Role(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Role rename(Table<?> name) {
        return new Role(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, String, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super UUID, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super UUID, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
