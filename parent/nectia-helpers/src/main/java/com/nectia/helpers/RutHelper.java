/*
 * nectia-helpers
 * Copyright (c) 2017 Nectia
 */

package com.nectia.helpers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Funciones basicas para el uso de ruts
 *
 * @version 1.0
 */
public final class RutHelper {
    private static final Logger LOG = LoggerFactory.getLogger(RutHelper.class);

    /**
     * Elimina tanto el . como el - de un rut
     *
     * @param rut Rut
     * @return Rut sin formato
     */
    public static String getRutWithoutFormat(String rut) {
        if (rut == null || "".equals(rut)) return "";

        return rut.replace(".", "").replace("-", "");
    }

    /**
     * Indica si un rut es valido o no
     *
     * @param rut Rut
     * @return true si el rut es correcto y false de lo contrario
     */
    public static boolean isValid(String rut) {

        boolean isValid = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                isValid = true;
            }

        } catch (Exception e) {
            LOG.error("Ha ocurrido un error", e);
        }
        return isValid;
    }
}
