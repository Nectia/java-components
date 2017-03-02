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

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Clase de configuracion utilizada para inticializar los JOBs de quartz con la anotación {@link QuartzCron}
 * Se utiliza con Spring y Quarz, se debe instanciar como bean.
 *
 * @version 1.1
 */
public class QuartzJobFactory {
    private static final Logger LOG = LoggerFactory.getLogger(QuartzJobFactory.class);

    private final SchedulerFactoryBean quartzScheduler;
    private final PropertiesAccessor propertiesAccessor;
    private final String group;

    /**
     * Constructor de el job factory
     *
     * @param quartzScheduler    SchedulerFactoryBean
     * @param propertiesAccessor Clase utilizada para acceder a los properties en tiempo de ejecución
     * @param group              Grupo de quartz en el cual se crean tanto los jobs como triggers
     */
    public QuartzJobFactory(SchedulerFactoryBean quartzScheduler, PropertiesAccessor propertiesAccessor, String group) {
        this.quartzScheduler = quartzScheduler;
        this.propertiesAccessor = propertiesAccessor;
        this.group = group;

        createJobs();
    }

    /**
     * Busca los jobs con la anotación {@link QuartzCron} y los crea
     */
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

    /**
     * Crea un nuevo job
     *
     * @param jobClass Clase del job
     */
    private void createJob(Class jobClass) {
        QuartzCron annotation = (QuartzCron) jobClass.getAnnotation(QuartzCron.class);
        String cronValue = propertiesAccessor.getProperty(annotation.cron());
        LOG.debug("Creando JOB {}, property: {}, cron: {}", jobClass.getSimpleName(), annotation.cron(), cronValue);

        String jobName = jobClass.getSimpleName() + "-job";
        String triggerName = jobClass.getSimpleName() + "-trigger";

        try {
            quartzScheduler.getScheduler().deleteJob(new JobKey(jobName, group));
        } catch (Exception e) {
            LOG.error("Ha ocurrido un error al eliminar el job " + jobName, e);
        }

        JobDetail job = newJob(jobClass).withIdentity(jobName, group)
                .build();


        Trigger trigger = newTrigger().withIdentity(triggerName, group)
                .withSchedule(cronSchedule(cronValue)).build();

        try {
            quartzScheduler.getScheduler().scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            LOG.error("Ha ocurrido un error al crear job " + jobName, e);
        }
    }
}
