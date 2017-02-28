/*
 * nectia--helpers
 * Copyright (c) 2017 Nectia
 */

package com.nectia.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase para obtener properties en tiempo de ejecicion
 *
 * @version 1.0
 */
@Named
public class PropertiesAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesAccessor.class);

    private final AbstractBeanFactory beanFactory;

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @Inject
    protected PropertiesAccessor(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Retorna una propiedad dado su key
     *
     * @param key Identificador de la propiedad
     * @return Valor de la propiedad
     */
    public String getProperty(String key) {
        LOG.debug("Obteniendo propiedad: {}", key);

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        String foundProp = null;
        try {
            foundProp = beanFactory.resolveEmbeddedValue(key.trim());
            cache.put(key, foundProp);
        } catch (IllegalArgumentException ex) {
            LOG.warn("Propiedad no encontrada: {}", key);
        }

        return foundProp;
    }
}
