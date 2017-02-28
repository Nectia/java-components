/*
 * nectia-mail
 * Copyright (c) 2017 Nectia
 */

package com.nectia.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Implementacion de envio del correo por smtp, para implementar esto se deben enviar las propiedades como el siguiente ejemplo para gmail:
 * <p>
 * Properties properties = System.getProperties();
 * props.put("mail.smtp.auth", "true");
 * props.put("mail.smtp.starttls.enable", "true");
 * props.put("mail.smtp.host", "smtp.gmail.com");
 * props.put("mail.smtp.port", "587");
 */
public class EmailServiceImpl extends AbstractEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final Properties properties;
    private Class<MultipartBuilder> builderType;
    private Authenticator authenticator;

    /**
     * Constructor con todos los parametros posibles
     *
     * @param properties    Propiedades de configuracion del envio de correo
     * @param authenticator Implementacion de {@link Authenticator} usado para autenticar
     * @param builderType
     */
    public EmailServiceImpl(Properties properties, Authenticator authenticator, Class<MultipartBuilder> builderType) {
        super();
        this.properties = properties;
        this.authenticator = authenticator;
        this.builderType = builderType;
    }

    /**
     * Constructor con properties y autenticator y MultipartBuilder por defecto
     *
     * @param properties    Propiedades de configuracion del envio de correo
     * @param authenticator Implementacion de {@link Authenticator} usado para autenticar
     */
    public EmailServiceImpl(Properties properties, Authenticator authenticator) {
        super();
        this.properties = properties;
        this.authenticator = authenticator;
        this.builderType = MultipartBuilder.class;
    }

    /**
     * Constructor con properties, con implementación propia de MultipartBuilder y sin autenticación
     *
     * @param properties  Propiedades de configuracion del envio de correo
     * @param builderType
     */
    public EmailServiceImpl(Properties properties, Class<MultipartBuilder> builderType) {
        super();
        this.properties = properties;
        this.builderType = builderType;
    }

    /**
     * Constructor con properties, MultipartBuilder por defecto y sin autenticación
     *
     * @param properties Propiedades de configuracion del envio de correo
     */
    public EmailServiceImpl(Properties properties) {
        super();
        this.properties = properties;
        this.builderType = MultipartBuilder.class;
    }

    /**
     * Envia un correo usando servicio STMP
     *
     * @param emailDTO Detalle del correo a enviar
     */
    public void sendEmail(EmailDTO emailDTO) {
        LOG.info("Enviando correo");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Detalle correo: {}", emailDTO);
        }
        try {
            Session session;

            if (authenticator == null) {
                session = Session.getDefaultInstance(properties);
            } else {
                session = Session.getInstance(properties, authenticator);
            }


            List<String> to = cleanList(emailDTO.getTo());
            List<String> cc = deleteDuplicates(to, cleanList(emailDTO.getCc()));
            MimeMessage message = new MimeMessage(session);
            if (null != cc) {

                message.addRecipients(Message.RecipientType.CC, makeAddres(cc));

            }

            message.addFrom(makeAddres(Collections.singletonList("gestcon@enel.com")));
            message.addRecipients(Message.RecipientType.TO, makeAddres(to));


            message.setSubject(emailDTO.getSubject());

            MultipartBuilder builder = builderType.newInstance();
            builder.setEmailDTO(emailDTO);

            message.setContent(builder.build());
            Transport.send(message);

            LOG.info("Correo enviado correctamente");

        } catch (Exception e) {
            LOG.error("Ha ocurrido un error al enviar el correo: ", e);
        }
    }
}