# spring-job
Spring Job 和 Quartz 定时任务示例

主要配置在 src/main/resources/spring/application-context.xml 文件中

### Quartz
Quartz 使用配置文件配置需要定时调度的类和方法
application-context.xml
```xml
<!--任务类-->
<bean id="testQuartz" class="com.hza.task.SendEMailTask">
</bean>

<!-- 将需要执行的定时任务注入JOB中。 -->
<bean id="testJob" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
    <property name="targetObject" ref="testQuartz"></property>
    <!-- 任务类中需要执行的方法 -->
    <property name="targetMethod" value="sendTextEmail"></property>
    <!--false: 上一次未执行完成的，要等待再执行。 -->
    <property name="concurrent" value="false"></property>
</bean>
<bean id="testJob2" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
    <property name="targetObject" ref="testQuartz"></property>
    <!-- 任务类中需要执行的方法 -->
    <property name="targetMethod" value="sendTextEmail2"></property>
    <!--if false: 上一次未执行完成的，要等待再执行。 -->
    <property name="concurrent" value="false"></property>
</bean>

<!-- 基本的触发器 -->
<bean id="testTrigger" class="org.springframework.scheduling.quartz.SimpleTriggerFactoryBean">
    <property name="jobDetail" ref="testJob"></property>
    <!--开始延迟-->
    <!--<property name="startDelay" value="3000"></property>-->
    <property name="repeatInterval" value="10000"></property>
</bean>

<!--使用cron表达式的触发器-->
<bean id="testCronTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
    <property name="jobDetail" ref="testJob2"></property>
    <property name="cronExpression" value="0/10 * * * * ?"></property>
</bean>

<!--将触发器添加到Scheduler-->
<bean id="schedulerFactoryBean" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    <property name="triggers">
        <list>
            <ref bean="testTrigger"></ref>
            <ref bean="testCronTrigger"></ref>
        </list>
    </property>
</bean>
```
### Spring job
主要使用注解在需要定时调度的类和方法上直接进行配置，在配置文件中设扫描
application-context.xml
```xml
<!--启用spring task注解-->
<task:annotation-driven/>
<context:annotation-config/>
<bean class="org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor"/>
<!--指定扫描位置-->
<!--<context:component-scan base-package="com.hza.task"/>--><!--启用spring task-->
```
MyTask.java
```java
@Component
public class MyTask {
    @Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    public void myTask(){
        System.out.println("我的任务A "+new Date());
    }
}
```

### Run

添加了 jetty 插件
使用 mvn 命令运行

    mvn clean jetty:run

