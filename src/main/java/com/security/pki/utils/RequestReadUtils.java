package com.security.pki.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.io.*;

public class RequestReadUtils {
    private static final int BUFFER_SIZE = 1024 * 8;

    public static String read(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringWriter writer = new StringWriter();
        write(reader, writer);
        return writer.getBuffer().toString();

    }

    public static long write(BufferedReader reader, StringWriter writer) throws IOException {
        return write(reader, writer, BUFFER_SIZE);
    }

    public static long write(Reader reader, Writer writer, int bufferSize) throws IOException {
        int read;
        long total = 0;
        char[] buffer = new char[bufferSize];
        while ((read = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, read);
            total += read;
        }
        return total;
    }
}
