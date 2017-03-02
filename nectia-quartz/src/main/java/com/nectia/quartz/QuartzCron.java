/*
 * nectia-quartz
 * Copyright (c) 2017 Nectia
 */

package com.nectia.quartz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacion utilizada para crear un job de quartz en base a la case con la anotacion, esta debe implementar {@link org.quartz.Job}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzCron {
    /**
     * Valor del cron, este debe ser un properti con la nomenclatura ${propiedad} y el valor de la propiedad debe serguir la nomenclatura de quartz
     * http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger.html
     */
    String cron();
}
