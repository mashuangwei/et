package com.msw.et.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.msw.et.entity.Teacher;
import com.msw.et.job.ChickenJob;
import com.msw.et.mapper.TeacherMapper;
import com.msw.et.service.JobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobServiceIml extends ServiceImpl<TeacherMapper, Teacher> implements JobService {
    /**
     * 注入任务调度器
     */
    @Autowired
    private Scheduler scheduler;

    @Override
    public void buildGoodStockTimer() {
        //任务名称
        String name = UUID.randomUUID().toString();
        //任务所属分组
        String group = ChickenJob.class.getName();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *");
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(ChickenJob.class).withIdentity(name, group).build();
        //设置任务传递商品编号参数
        jobDetail.getJobDataMap().put("goodId", "hi goodid");
        //创建任务触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
        //将触发器与任务绑定到调度器内
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
