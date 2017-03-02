/*
 * nectia-mail
 * Copyright (c) 2017 Nectia
 */

package com.nectia.mail;


import com.google.common.base.MoreObjects;

import java.io.InputStream;

/**
 * Dto para los archivos adjuntos
 *
 * @version 1.0
 */
public class EmailAttachedFile {
    private final String name;
    private final String mediaType;
    private final InputStream inputStream;

    /**
     * Constructor
     *
     * @param name        Nombre del archivo
     * @param mediaType   Tipo del archivo
     * @param inputStream Archivo como {@link InputStream}
     */
    public EmailAttachedFile(String name, String mediaType, InputStream inputStream) {
        this.name = name;
        this.mediaType = mediaType;
        this.inputStream = inputStream;
    }

    /**
     * @return Nombre del archivo
     */
    public String getName() {
        return name;
    }

    /**
     * @return Tipo del archivo
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * @return Archivo como {@link InputStream}
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("mediaType", mediaType)
                .add("inputStream", inputStream)
                .toString();
    }
}
