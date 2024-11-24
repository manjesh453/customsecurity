package com.security.pki.wrapper;

import com.security.pki.utils.RequestReadUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Getter
@Setter
public class CustomHttpRequestWrapper extends HttpServletRequestWrapper {
    private String body;

    public CustomHttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = RequestReadUtils.read(request);
    }

    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No-op
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

}
