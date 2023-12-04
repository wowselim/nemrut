/*
 * This file is generated by jOOQ.
 */
package co.selim.nemrut.jooq.tables;


import co.selim.nemrut.jooq.Keys;
import co.selim.nemrut.jooq.Public;
import co.selim.nemrut.jooq.tables.records.UserAccountRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
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
public class UserAccount extends TableImpl<UserAccountRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.user_account</code>
     */
    public static final UserAccount USER_ACCOUNT = new UserAccount();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserAccountRecord> getRecordType() {
        return UserAccountRecord.class;
    }

    /**
     * The column <code>public.user_account.id</code>.
     */
    public final TableField<UserAccountRecord, UUID> ID = createField(DSL.name("id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("gen_random_uuid()"), SQLDataType.UUID)), this, "");

    /**
     * The column <code>public.user_account.username</code>.
     */
    public final TableField<UserAccountRecord, String> USERNAME = createField(DSL.name("username"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.user_account.password</code>.
     */
    public final TableField<UserAccountRecord, String> PASSWORD = createField(DSL.name("password"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.user_account.created_at</code>.
     */
    public final TableField<UserAccountRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.user_account.updated_at</code>.
     */
    public final TableField<UserAccountRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "");

    private UserAccount(Name alias, Table<UserAccountRecord> aliased) {
        this(alias, aliased, null);
    }

    private UserAccount(Name alias, Table<UserAccountRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.user_account</code> table reference
     */
    public UserAccount(String alias) {
        this(DSL.name(alias), USER_ACCOUNT);
    }

    /**
     * Create an aliased <code>public.user_account</code> table reference
     */
    public UserAccount(Name alias) {
        this(alias, USER_ACCOUNT);
    }

    /**
     * Create a <code>public.user_account</code> table reference
     */
    public UserAccount() {
        this(DSL.name("user_account"), null);
    }

    public <O extends Record> UserAccount(Table<O> child, ForeignKey<O, UserAccountRecord> key) {
        super(child, key, USER_ACCOUNT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<UserAccountRecord> getPrimaryKey() {
        return Keys.USER_ACCOUNT_PKEY;
    }

    @Override
    public List<UniqueKey<UserAccountRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.USER_ACCOUNT_USERNAME_KEY);
    }

    @Override
    public UserAccount as(String alias) {
        return new UserAccount(DSL.name(alias), this);
    }

    @Override
    public UserAccount as(Name alias) {
        return new UserAccount(alias, this);
    }

    @Override
    public UserAccount as(Table<?> alias) {
        return new UserAccount(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserAccount rename(String name) {
        return new UserAccount(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserAccount rename(Name name) {
        return new UserAccount(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserAccount rename(Table<?> name) {
        return new UserAccount(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, String, String, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super UUID, ? super String, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super UUID, ? super String, ? super String, ? super LocalDateTime, ? super LocalDateTime, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}