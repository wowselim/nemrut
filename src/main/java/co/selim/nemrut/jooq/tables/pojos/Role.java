/*
 * This file is generated by jOOQ.
 */
package co.selim.nemrut.jooq.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public record Role(
    UUID id,
    String title,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) implements Serializable {

    private static final long serialVersionUID = 1L;

}
