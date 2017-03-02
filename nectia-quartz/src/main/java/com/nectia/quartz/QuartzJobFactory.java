/*
 * nectia-quartz
 * Copyright (c) 2017 Nectia
 */

package com.nectia.quartz;

import com.nectia.helpers.PropertiesAccessor;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.inject.Inject;
import javax.inject.Named;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Clase de configuracion utilizada para inticializar los JOBs de quartz con la anotación {@link QuartzCron}
 *
 * @version 1.0
 */
@Named
public class QuartzJobFactory {
    public static final String GROUP = "gestcon-group";
    private static final Logger LOG = LoggerFactory.getLogger(QuartzJobFactory.class);

    private SchedulerFactoryBean quartzScheduler;
    private PropertiesAccessor propertiesAccessor;

    @Inject
    public QuartzJobFactory(SchedulerFactoryBean quartzScheduler, PropertiesAccessor propertiesAccessor) {
        this.quartzScheduler = quartzScheduler;
        this.propertiesAccessor = propertiesAccessor;

        createJobs();
    }

    private void createJobs() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(QuartzCron.class));

        for (BeanDefinition beanDefinition : scanner.findCandidateComponents("com.enel.gestcon")) {
            try {
                createJob(Class.forName(beanDefinition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                LOG.error("Ha ocurrido un error al obtener la clase: " + beanDefinition.getBeanClassName(), e);
            }
        }
    }

    private void createJob(Class jobClass) {
        QuartzCron annotation = (QuartzCron) jobClass.getAnnotation(QuartzCron.class);
        String cronValue = propertiesAccessor.getProperty(annotation.cron());
        LOG.debug("Creando JOB {}, property: {}, cron: {}", jobClass.getSimpleName(), annotation.cron(), cronValue);

        String jobName = jobClass.getSimpleName() + "-job";
        String triggerName = jobClass.getSimpleName() + "-trigger";

        try {
            quartzScheduler.getScheduler().deleteJob(new JobKey(jobName, GROUP));
        } catch (Exception e) {
            LOG.error("Ha ocurrido un error al eliminar el job " + jobName, e);
        }

        JobDetail job = newJob(jobClass).withIdentity(jobName, GROUP)
                .build();


        Trigger trigger = newTrigger().withIdentity(triggerName, GROUP)
                .withSchedule(cronSchedule(cronValue)).build();

        try {
            quartzScheduler.getScheduler().scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            LOG.error("Ha ocurrido un error al crear job " + jobName, e);
        }
    }
}
