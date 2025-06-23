package com.silver.demo.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextoUtils {

	private static final Set<String> PALABRAS_MINUSCULAS = new HashSet<>(
            Arrays.asList("de", "la", "del", "los", "las", "y", "a", "en", "el"));
	
	public static String formatoPrimeraLetraMayuscula(String texto) {
        if (texto == null || texto.isBlank())
            return texto;

        String[] palabras = texto.trim().toLowerCase().split("\\s+");

        for (int i = 0; i < palabras.length; i++) {
            if (i == 0 || !PALABRAS_MINUSCULAS.contains(palabras[i])) {
                palabras[i] = palabras[i].substring(0, 1).toUpperCase() + palabras[i].substring(1);
            }
        }

        return String.join(" ", palabras);
    }
	
	public static String formatoTodoMinuscula(String texto) {
        return texto == null ? null : texto.trim().toLowerCase();
    }
	
}
