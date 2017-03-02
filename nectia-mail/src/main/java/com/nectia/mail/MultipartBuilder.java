/*
 * nectia-mail
 * Copyright (c) 2017 Nectia
 */

package com.nectia.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;

import static com.nectia.mail.AbstractEmailService.CONTENT_MEDIATYPE;

/**
 * Clase utilizada para construir el Multipart a enviar, se puede heredar de esta clase e implementar el método build,
 * así se podria agregar un pie en el correo personalizado por ejemplo.
 *
 * @version 1.0
 */
public class MultipartBuilder {
    private EmailDTO emailDTO;

    /**
     * Establece el EmailDto
     *
     * @param emailDTO
     * @return Retorna la instancia actual del builder
     */
    public MultipartBuilder setEmailDTO(EmailDTO emailDTO) {
        this.emailDTO = emailDTO;
        return this;
    }

    /**
     * Crea una instancia de MimeMultipart y la retorna
     *
     * @return Instancia de MimeMultipart
     * @throws MessagingException
     * @throws IOException
     */
    public Multipart build() throws MessagingException, IOException {
        Multipart multipart = new MimeMultipart();

        multipart = addContent(emailDTO, multipart);
        multipart = addAttachedFiles(emailDTO, multipart);
        return multipart;
    }


    /**
     * Agrega archivos ardjuntos
     *
     * @param emailDTO
     * @param multipart
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    protected Multipart addAttachedFiles(EmailDTO emailDTO, Multipart multipart) throws MessagingException, IOException {
        if (emailDTO.getAttachedFile() != null && !emailDTO.getAttachedFile().isEmpty()) {
            for (EmailAttachedFile attachedFile : emailDTO.getAttachedFile()) {
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                if (attachedFile.getInputStream() != null) {
                    DataSource document = new ByteArrayDataSource(attachedFile.getInputStream(),
                                                                  attachedFile.getMediaType());
                    messageBodyPart.setDataHandler(new DataHandler(document));
                    messageBodyPart.setFileName(attachedFile.getName());
                    multipart.addBodyPart(messageBodyPart);
                }
            }
        }

        return multipart;
    }

    /**
     * Agrega el contenido como @{link CONTENT_MEDIATYPE}
     *
     * @param emailDTO
     * @param multipart
     * @return
     * @throws MessagingException
     */
    protected Multipart addContent(EmailDTO emailDTO, Multipart multipart) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(emailDTO.getText(), CONTENT_MEDIATYPE);
        multipart.addBodyPart(messageBodyPart);
        return multipart;
    }
}
