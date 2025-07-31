package com.training.query.Training.Query.service;

import com.training.query.Training.Query.util.CronUtils;
import com.training.query.Training.Query.collections.Job;
import com.training.query.Training.Query.collections.Scheduler;
import com.training.query.Training.Query.collections.TrainingTrackingTask;
import com.training.query.Training.Query.exception.SchedulerNotFoundException;
import com.training.query.Training.Query.repository.JobRepository;
import com.training.query.Training.Query.repository.SchedulerRepository;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class SchedulerService {

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    @Lazy
    private TrainingTrackingTask trainingTrackingTask;

    @PostConstruct
    public void init() {
        taskScheduler.initialize();
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void trainingEscalationJob() {
        trainingTrackingTask.run();
    }

    public void saveJobExecution(Job job) {
        jobRepository.save(job);
    }

    public List<Scheduler> getAllSchedulers() {
        return schedulerRepository.findAll();
    }

    public Scheduler saveScheduler(Scheduler scheduler) {
        System.out.println("Saving scheduler: " + scheduler);


        scheduler.setNextRunTime(CronUtils.getNextRunTime(scheduler.getCronExpression()));
        scheduler.setLastRunTime(null);

        Scheduler saved = schedulerRepository.save(scheduler);
        System.out.println("Saved scheduler with ID: " + saved.getId());

        return saved;
    }

    public void deleteSchedulerById(String id) {
        schedulerRepository.deleteById(new ObjectId(id));
    }

    public void startScheduler(String id) {
        Scheduler scheduler = schedulerRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new SchedulerNotFoundException("Scheduler not found: " + id));

        startJob(scheduler);
    }

    public void stopScheduler(String id) {
        Scheduler scheduler = schedulerRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new SchedulerNotFoundException("Scheduler not found: " + id));

        stopJob(scheduler);
    }

    public void startJob(Scheduler scheduler) {
        if (!scheduler.isEnabled()) {
            throw new RuntimeException("Scheduler is disabled: " + scheduler.getId());
        }

        Runnable task = getRunnableForJob(scheduler.getJobname(), scheduler.getId());
        CronTrigger trigger = new CronTrigger(scheduler.getCronExpression());

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, trigger);
        scheduledTasks.put(scheduler.getId(), scheduledFuture);

    }

    public void stopJob(Scheduler scheduler) {
        String schedulerId = scheduler.getId();
        ScheduledFuture<?> future = scheduledTasks.get(schedulerId);
        if (future != null) {
            future.cancel(true);
            scheduledTasks.remove(schedulerId);
        }
    }

    public Runnable getRunnableForJob(String jobType, String schedulerId) {
        if ("training-tracking".equalsIgnoreCase(jobType)) {
            return trainingTrackingTask; // TrainingTrackingTask implements Runnable
        }
        return () -> System.out.println("Unknown job type: " + jobType);
    }


    public void logJobExecution(String schedulerId, String status) {
        Job job = new Job();
        job.setSchedulerId(schedulerId);
        job.setStatus(status);
        job.setLastRunTime(new Date());
        job.setNextRunTime(null);
        job.setMessage("Executed via scheduler");
        jobRepository.save(job);
    }

    public Scheduler createScheduler(Scheduler scheduler) {

        Date nextRun = CronUtils.getNextRunTime(scheduler.getTime());
        scheduler.setNextRunTime(nextRun);

        return schedulerRepository.save(scheduler);
    }
    public Scheduler updateScheduler(Scheduler scheduler) {

        Date nextRun = CronUtils.getNextRunTime(scheduler.getTime());
        scheduler.setNextRunTime(nextRun);

        return schedulerRepository.save(scheduler);
    }

}
