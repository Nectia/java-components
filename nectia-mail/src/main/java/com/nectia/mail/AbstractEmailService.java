/*
 * nectia-mail
 * Copyright (c) 2017 Nectia
 */

package com.nectia.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Clase abstracta que contiene la base para enviar correos
 *
 * @version 1.0
 */
public abstract class AbstractEmailService {

    public static final String CONTENT_MEDIATYPE = "text/html; charset=utf-8";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractEmailService.class);
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern;

    /**
     * Constructor de la clase
     */
    public AbstractEmailService() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    /**
     * Metodo encargado de enviar el correo, el cual debe ser implementado por la clase que herede de AbstractEmailService
     *
     * @param emailDTO Detalle del correo a enviar
     */
    public abstract void sendEmail(EmailDTO emailDTO);

    /**
     * Clase encargada de eliminar correos duplicados, de la segunda lista
     *
     * @param firstList  Lista de correos de destino
     * @param secondList Lista correos de copia
     * @return Nueva lista de correos de copia sin duplicados
     */
    public List<String> deleteDuplicates(List<String> firstList, List<String> secondList) {
        List<String> response = new ArrayList<>();
        if (secondList == null) {
            return null;
        }
        for (String mail : secondList) {
            if (!firstList.contains(mail)) {
                response.add(mail);
            } else {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Mail repetido: " + mail);
                }

            }
        }

        return response;
    }

    /**
     * Limpia un alista de correos duplicados en la mista, correos en null y en blanco
     *
     * @param originalList Lista a limpiar
     * @return Lista sin correos null, en blanco o duplicados
     */
    public List<String> cleanList(List<String> originalList) {
        List<String> response = new ArrayList<>();

        if (originalList == null) {
            return null;
        }

        for (String mail : originalList) {
            if (null == mail || mail.isEmpty() || response.contains(mail)) {
                continue;
            }

            response.add(mail);
        }
        return response;
    }

    /**
     * Convierte una lista de String a un array de {@link Address}
     *
     * @param listMails Lista de correos como String
     * @return Array de Address
     */
    public Address[] makeAddres(List<String> listMails) {

        List<Address> response = new ArrayList<>();

        if (listMails != null && !listMails.isEmpty()) {
            for (String mail : listMails) {
                try {
                    if (pattern.matcher(mail).matches())
                        response.add(new InternetAddress(mail));
                } catch (Exception e) {
                    LOG.warn("No es posible convertir a correo: {}", mail);
                }
            }
        }

        return response.toArray(new Address[0]);
    }
}
