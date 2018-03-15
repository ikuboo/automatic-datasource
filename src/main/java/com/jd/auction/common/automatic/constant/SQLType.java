package com.jd.auction.common.core.constant;

/**
 * SQL Type
 * @author yuanchunsen@jd.com
 *         2018/3/9.
 */
public enum SQLType {
    /**
     * Data Query Language.
     *
     * <p>Such as {@code SELECT}.</p>
     */
    DQL,


    /**
     * Data Manipulation Language.
     *
     * <p>Such as {@code INSERT}, {@code UPDATE}, {@code DELETE}.</p>
     */
    DML,


    /**
     * Data Definition Language.
     *
     * <p>Such as {@code CREATE}, {@code ALTER}, {@code DROP}, {@code TRUNCATE}.</p>
     */
    DDL,


    /**
     * Transaction Control Language.
     *
     * <p>Such as {@code SET}, {@code COMMIT}, {@code ROLLBACK}, {@code SAVEPOIINT}, {@code BEGIN}.</p>
     */
    TCL

}
