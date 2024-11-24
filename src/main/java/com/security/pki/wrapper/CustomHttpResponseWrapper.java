package com.security.pki.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomHttpResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintWriter writer = new PrintWriter(outputStream);

    public CustomHttpResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            public void write(byte[] b) throws IOException {
                outputStream.write(b);
            }
        };
    }

    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            outputStream.flush();
        }
    }

    public String getResponseData() {
        return outputStream.toString();
    }
}
