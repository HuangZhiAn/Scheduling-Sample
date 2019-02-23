# SchedulingDemo
Spring-scheduling 和 Quartz 定时任务 Demo

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
### Spring-scheduling
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
Spring-scheduling注解简单用法，MyTask.java
```java
@Component
public class MyTask {
    @Scheduled(cron="0/5 * *  * * ? ")   //每5秒执行一次
    public void myTask(){
        System.out.println("我的任务A "+new Date());
    }
}
```
CRON Expressions
================

A CRON expression is a string representing the schedule for a particular command to execute.  The parts of a CRON schedule are as follows:

    *    *    *    *    *
    -    -    -    -    -
    |    |    |    |    |
    |    |    |    |    |
    |    |    |    |    +----- day of week (0 - 7) (Sunday=0 or 7)
    |    |    |    +---------- month (1 - 12)
    |    |    +--------------- day of month (1 - 31)
    |    +-------------------- hour (0 - 23)
    +------------------------- min (0 - 59)
### **Specials Characters In Expression**

-   ***  (all)**  – it is used to specify that event should happen for every time unit. For example,  _“*”_  in the <_minute>_  field – means “for every minute”
-   **? (any)**  – it is utilized in the <_day-of-month>_  and <_day-of -week>_ fields  to denote the arbitrary value – neglect the field value. For example, if we want to fire a script at “5th of every month” irrespective of what the day of the week falls on that date, then we specify a “_?_” in the <_day-of-week>_  field
-   **– (range)** – it is used to determine the value range. For example, “_10-11_” in  _<hour>_  field means “10th and 11th hours”
-   **, (values)** – it is used to specify multiple values. For example, “_MON, WED, FRI”_  in <_day-of-week>_  field means on the days “Monday, Wednesday, and Friday”
-   **/ (increments)** – it is used to specify the incremental values. For example, a  _“5/15”_  in the <_minute>_ field, means at “5, 20, 35 and 50 minutes of an hour”
-   **L (last)** – it has different meanings when used in various fields. For example, if it’s applied in the <_day-of-month>_ field, then it means last day of the month, i.e. “31st for January” and so on as per the calendar month. It can be used with an offset value, like “_L-3_“, which denotes the “third to last day of the calendar month”. In the <_day-of-week>_, it specifies the “last day of a week”. It can also be used with another value in <_day-of-week>_, like “_6L_“, which denotes the “last Friday”
-   **W (weekday)** – it is used to specify the weekday (Monday to Friday) nearest to a given day of the month. For example, if we specify “_10W_” in the <_day-of-month>_ field, then it means the “weekday near to 10th of that month”. So if “10th” is a Saturday, then the job will be triggered on “9th”, and if “10th” is a Sunday, then it will trigger on “11th”. If you specify “_1W_” in the <_day-of-month>_  and if “1st” is Saturday, then the job will be triggered on “3rd” which is Monday, and it will not jump back to the previous month
-   **#**  – it is used to specify the “_N_-th” occurrence of a weekday of the month, for example, “3rd Friday of the month” can be indicated as “_6#3_“

### Run

添加了 jetty 插件
使用 mvn 命令直接运行

    mvn clean jetty:run

