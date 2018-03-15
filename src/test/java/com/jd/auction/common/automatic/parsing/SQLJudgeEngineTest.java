package com.jd.auction.common.automatic.parsing;


import com.jd.auction.common.automatic.constant.SQLType;
import org.junit.Assert;
import org.junit.Test;

public class SQLJudgeEngineTest {
    @Test
    public void judgeSQLTypeDQL() throws Exception {
        String sql = "update t1 set a = '1' where id in(select * from table_name where id = ssss)";
        SQLJudgeEngine sqlJudgeEngine = new SQLJudgeEngine(sql);
        SQLType sqlType = sqlJudgeEngine.judgeSQLType();
        Assert.assertTrue(sqlJudgeEngine.judgeSQLType().equals(SQLType.DQL));
    }
}
