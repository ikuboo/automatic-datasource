package com.jd.auction.common.core.parsing.lexer.dialect.mysql;


import com.jd.auction.common.core.parsing.lexer.token.Keyword;


public enum MySQLKeyword implements Keyword {
    
    SHOW, 
    DUAL, 
    LIMIT, 
    OFFSET, 
    VALUE, 
    FORCE, 
    PARTITION, 
    DISTINCTROW, 
    KILL, 
    QUICK, 
    BINARY, 
    CACHE, 
    SQL_CACHE, 
    SQL_NO_CACHE, 
    SQL_SMALL_RESULT, 
    SQL_BIG_RESULT, 
    SQL_BUFFER_RESULT, 
    SQL_CALC_FOUND_ROWS, 
    LOW_PRIORITY, 
    HIGH_PRIORITY, 
    OPTIMIZE, 
    ANALYZE, 
    IGNORE, 
    CHANGE,
    FIRST,
    SPATIAL,
    ALGORITHM,
    COLLATE, 
    DISCARD,
    IMPORT,
    VALIDATION, 
    REORGANIZE,
    EXCHANGE,
    REBUILD,
    REPAIR,
    REMOVE,
    UPGRADE,
    KEY_BLOCK_SIZE,
    AUTO_INCREMENT,
    AVG_ROW_LENGTH,
    CHECKSUM,
    COMPRESSION,
    CONNECTION,
    DIRECTORY,
    DELAY_KEY_WRITE,
    ENCRYPTION,
    ENGINE,
    INSERT_METHOD,
    MAX_ROWS,
    MIN_ROWS,
    PACK_KEYS,
    ROW_FORMAT,
    DYNAMIC,
    FIXED,
    COMPRESSED,
    REDUNDANT,
    COMPACT,
    STATS_AUTO_RECALC,
    STATS_PERSISTENT,
    STATS_SAMPLE_PAGES, 
    DISK,
    MEMORY,
    ROLLUP,
    RESTRICT,
    STRAIGHT_JOIN, 
    REGEXP
}
