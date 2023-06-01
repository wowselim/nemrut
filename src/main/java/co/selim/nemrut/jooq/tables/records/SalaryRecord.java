/*
 * This file is generated by jOOQ.
 */
package co.selim.nemrut.jooq.tables.records;


import co.selim.nemrut.jooq.tables.Salary;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SalaryRecord extends UpdatableRecordImpl<SalaryRecord> implements Record7<UUID, UUID, UUID, String, Long, LocalDateTime, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.salary.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.salary.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.salary.role_id</code>.
     */
    public void setRoleId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.salary.role_id</code>.
     */
    public UUID getRoleId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.salary.company_id</code>.
     */
    public void setCompanyId(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.salary.company_id</code>.
     */
    public UUID getCompanyId() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>public.salary.currency</code>.
     */
    public void setCurrency(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.salary.currency</code>.
     */
    public String getCurrency() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.salary.amount</code>.
     */
    public void setAmount(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.salary.amount</code>.
     */
    public Long getAmount() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.salary.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.salary.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.salary.updated_at</code>.
     */
    public void setUpdatedAt(LocalDateTime value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.salary.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<UUID, UUID, UUID, String, Long, LocalDateTime, LocalDateTime> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<UUID, UUID, UUID, String, Long, LocalDateTime, LocalDateTime> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Salary.SALARY.ID;
    }

    @Override
    public Field<UUID> field2() {
        return Salary.SALARY.ROLE_ID;
    }

    @Override
    public Field<UUID> field3() {
        return Salary.SALARY.COMPANY_ID;
    }

    @Override
    public Field<String> field4() {
        return Salary.SALARY.CURRENCY;
    }

    @Override
    public Field<Long> field5() {
        return Salary.SALARY.AMOUNT;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return Salary.SALARY.CREATED_AT;
    }

    @Override
    public Field<LocalDateTime> field7() {
        return Salary.SALARY.UPDATED_AT;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public UUID component2() {
        return getRoleId();
    }

    @Override
    public UUID component3() {
        return getCompanyId();
    }

    @Override
    public String component4() {
        return getCurrency();
    }

    @Override
    public Long component5() {
        return getAmount();
    }

    @Override
    public LocalDateTime component6() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime component7() {
        return getUpdatedAt();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public UUID value2() {
        return getRoleId();
    }

    @Override
    public UUID value3() {
        return getCompanyId();
    }

    @Override
    public String value4() {
        return getCurrency();
    }

    @Override
    public Long value5() {
        return getAmount();
    }

    @Override
    public LocalDateTime value6() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime value7() {
        return getUpdatedAt();
    }

    @Override
    public SalaryRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public SalaryRecord value2(UUID value) {
        setRoleId(value);
        return this;
    }

    @Override
    public SalaryRecord value3(UUID value) {
        setCompanyId(value);
        return this;
    }

    @Override
    public SalaryRecord value4(String value) {
        setCurrency(value);
        return this;
    }

    @Override
    public SalaryRecord value5(Long value) {
        setAmount(value);
        return this;
    }

    @Override
    public SalaryRecord value6(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public SalaryRecord value7(LocalDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    public SalaryRecord values(UUID value1, UUID value2, UUID value3, String value4, Long value5, LocalDateTime value6, LocalDateTime value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SalaryRecord
     */
    public SalaryRecord() {
        super(Salary.SALARY);
    }

    /**
     * Create a detached, initialised SalaryRecord
     */
    public SalaryRecord(UUID id, UUID roleId, UUID companyId, String currency, Long amount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(Salary.SALARY);

        setId(id);
        setRoleId(roleId);
        setCompanyId(companyId);
        setCurrency(currency);
        setAmount(amount);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    /**
     * Create a detached, initialised SalaryRecord
     */
    public SalaryRecord(co.selim.nemrut.jooq.tables.pojos.Salary value) {
        super(Salary.SALARY);

        if (value != null) {
            setId(value.id());
            setRoleId(value.roleId());
            setCompanyId(value.companyId());
            setCurrency(value.currency());
            setAmount(value.amount());
            setCreatedAt(value.createdAt());
            setUpdatedAt(value.updatedAt());
        }
    }
}
