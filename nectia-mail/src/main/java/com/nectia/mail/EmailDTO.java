/*
 * {project-name}
 * Copyright (c) 2017 Nectia
 */

package com.nectia.mail;

import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * Dto que contiene el detalle del correo a enviar
 *
 * @version 1.0
 */
public class EmailDTO {

    private final String from;
    private final List<String> to;
    private final List<String> cc;
    private final String subject;
    private final String text;
    private final List<EmailAttachedFile> attachedFile;

    /**
     * Constructor del dto con todos los parametros
     *
     * @param from         Origen del correo
     * @param to           Listado de correos de destino
     * @param cc           Listado de correos de copia
     * @param subject      Asunto
     * @param text         Contenido del correo (html)
     * @param attachedFile Archivos adjuntos
     */
    public EmailDTO(String from, List<String> to, List<String> cc, String subject, String text, List<EmailAttachedFile> attachedFile) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.text = text;
        this.attachedFile = attachedFile;
    }

    /**
     * Constructor del dto con copia y sin archivos adjuntos
     *
     * @param from    Origen del correo
     * @param to      Listado de correos de destino
     * @param cc      Listado de correos de copia
     * @param subject Asunto
     * @param text    Contenido del correo (html)
     */
    public EmailDTO(String from, List<String> to, List<String> cc, String subject, String text) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.text = text;
        this.attachedFile = null;
    }

    /**
     * Constructor del dto sin copia y con archivos adjuntos
     *
     * @param from         Origen del correo
     * @param to           Listado de correos de destino
     * @param subject      Asunto
     * @param text         Contenido del correo (html)
     * @param attachedFile Archivos adjuntos
     */
    public EmailDTO(String from, List<String> to, String subject, String text, List<EmailAttachedFile> attachedFile) {
        this.from = from;
        this.to = to;
        this.cc = null;
        this.subject = subject;
        this.text = text;
        this.attachedFile = attachedFile;
    }

    /**
     * Constructor del dto sin copia y sin archivos adjuntos
     *
     * @param from    Origen del correo
     * @param to      Listado de correos de destino
     * @param subject Asunto
     * @param text    Contenido del correo (html)
     */
    public EmailDTO(String from, List<String> to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.cc = null;
        this.subject = subject;
        this.text = text;
        this.attachedFile = null;
    }

    /**
     * @return Origen del correo
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return Listado de correos de destino
     */
    public List<String> getTo() {
        return to;
    }

    /**
     * @return Listado de correos de copia
     */
    public List<String> getCc() {
        return cc;
    }

    /**
     * @return Asunto
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return Contenido del correo (html)
     */
    public String getText() {
        return text;
    }

    /**
     * @return Archivos adjuntos
     */
    public List<EmailAttachedFile> getAttachedFile() {
        return attachedFile;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("from", from)
                .add("to", to)
                .add("cc", cc)
                .add("subject", subject)
                .add("text", text)
                .toString();
    }
}
