/*
 * This file is generated by jOOQ.
 */
package co.selim.nemrut.jooq.tables.records;


import co.selim.nemrut.jooq.tables.Company;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CompanyRecord extends UpdatableRecordImpl<CompanyRecord> implements Record4<UUID, String, LocalDateTime, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.company.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.company.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.company.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.company.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.company.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.company.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>public.company.updated_at</code>.
     */
    public void setUpdatedAt(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.company.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<UUID, String, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<UUID, String, LocalDateTime, LocalDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Company.COMPANY.ID;
    }

    @Override
    public Field<String> field2() {
        return Company.COMPANY.NAME;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return Company.COMPANY.CREATED_AT;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Company.COMPANY.UPDATED_AT;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public LocalDateTime component3() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime component4() {
        return getUpdatedAt();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public LocalDateTime value3() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime value4() {
        return getUpdatedAt();
    }

    @Override
    public CompanyRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public CompanyRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public CompanyRecord value3(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public CompanyRecord value4(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public CompanyRecord values(UUID value1, String value2, LocalDateTime value3, LocalDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CompanyRecord
     */
    public CompanyRecord() {
        super(Company.COMPANY);
    }

    /**
     * Create a detached, initialised CompanyRecord
     */
    public CompanyRecord(UUID id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(Company.COMPANY);

        setId(id);
        setName(name);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    /**
     * Create a detached, initialised CompanyRecord
     */
    public CompanyRecord(co.selim.nemrut.jooq.tables.pojos.Company value) {
        super(Company.COMPANY);

        if (value != null) {
            setId(value.id());
            setName(value.name());
            setCreatedAt(value.createdAt());
            setUpdatedAt(value.updatedAt());
        }
    }
}
