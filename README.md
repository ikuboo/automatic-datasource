`automatic-datasource` 自动数据源
===
## 功能介绍：
  * 1.读写分离，支持mysql一主多从
  * 2.从库点负载均衡，支持随机，轮询，权重 
  * 3.从库故障自动切换

## 接入方式:
```JAVA
<!--主库数据源-->
<bean id="dataSource_m" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${jdbc.driver}" />
    <property name="url" value="${jdbc.master.url}" />
    <property name="username" value="${jdbc.master.username}" />
    <property name="password" value="${jdbc.master.password}" />
</bean>
<!--从库1数据源-->
<bean id="dataSource_s1" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${jdbc.driver}" />
    <property name="url" value="${jdbc.slave1.url}" />
    <property name="username" value="${jdbc.slave1.username}" />
    <property name="password" value="${jdbc.slave1.password}" />
</bean>

<!--从库2数据源-->
<bean id="dataSource_s2" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${jdbc.driver}" />
    <property name="url" value="${jdbc.slave2.url}" />
    <property name="username" value="${jdbc.slave2.username}" />
    <property name="password" value="${jdbc.slave2.password}" />
</bean>
<!--主库NamedDataSource-->
<bean id="masterNamedDataSource" class="com.jd.auction.common.automatic.datasouce.NamedDataSource">
    <constructor-arg name="name" value="master"/>
    <constructor-arg name="dataSource" ref="dataSource_m"/>
</bean>
<!--从库1NamedDataSource-->
<bean id="slave1NamedDataSource" class="com.jd.auction.common.automatic.datasouce.NamedDataSource">
    <constructor-arg name="name" value="slave1"/>
    <constructor-arg name="dataSource" ref="dataSource_s1"/>
    <!--权重值(只有负载策略权重轮询的时候生效)-->
    <constructor-arg name="weight" value="1" />
</bean>
<!--从库2NamedDataSource-->
<bean id="slave2NamedDataSource" class="com.jd.auction.common.automatic.datasouce.NamedDataSource">
    <constructor-arg name="name" value="slave2"/>
    <constructor-arg name="dataSource" ref="dataSource_s2"/>
    <!--权重值(只有负载策略权重轮询的时候生效)-->
    <constructor-arg name="weight" value="1" />
</bean>

<!--指定从库负载均衡策略为权重轮询（如不指定，默认为轮询策略）-->
<bean id="loadBalanceStrategy" class="org.springframework.beans.factory.config.FieldRetrievingFactoryBean">
    <property name="staticField" value="com.jd.auction.common.automatic.constant.LoadBalanceStrategy.RANDOM" />
</bean>
<!--autoMaticDataSource 实现了dataSource接口，可直接注入Hibernate,MyBatis等ORM框架使用-->
<bean id="autoMaticDataSource" class="com.jd.auction.common.automatic.datasouce.AutoMaticDataSource">
    <property name="masterDataSource" ref="masterNamedDataSource" />
    <property name="slaveDataSources">
        <list>
            <ref bean="slave1NamedDataSource" />
            <ref bean="slave2NamedDataSource" />
        </list>
    </property>
    <!--配置数据源状态监听器，不配置默认打印ERROR级别的日志-->
    <!-- 
      <property name="statusChangeListen" ref="实现com.jd.auction.common.automatic.monitor.statuslisten.StatusChangeListen接口"/>
    -->
    <!--从库负载策略-->
    <property name="loadBalanceStrategy" ref="loadBalanceStrategy" />
</bean>
```
