package com.security.pki.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.pki.dto.ClientPKIResponse;
import com.security.pki.dto.PkiData;
import com.security.pki.exception.CoreClientException;
import com.security.pki.helper.PkiMessage;
import com.security.pki.utils.PkiUtil;
import com.security.pki.utils.SecurityUtil;
import com.security.pki.wrapper.CustomHttpRequestWrapper;
import com.security.pki.wrapper.CustomHttpResponseWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@AllArgsConstructor
public class PkiFilter extends SecurityUtil implements Filter {
    private final PkiUtil pkiUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        CustomHttpRequestWrapper customRequest = new CustomHttpRequestWrapper(req);
        CustomHttpRequestWrapper copyCustomRequest = new CustomHttpRequestWrapper(req);
        CustomHttpResponseWrapper customResponse = new CustomHttpResponseWrapper(res);
        BeanUtils.copyProperties(customRequest, copyCustomRequest);
        String encryptedData = customRequest.getBody();
        String clientSignaturePublicKey = "";
        try {
            clientSignaturePublicKey = pkiUtil.getSignaturePublicKey(customRequest);
            if (!encryptedData.isEmpty()) {
                decryptAndSetData(customRequest, clientSignaturePublicKey);
            }
        } catch (Exception e) {
            handleFilterException(res, e);
            return;
        }
        chain.doFilter(customRequest, customResponse);
        if (!customResponse.getResponseData().isEmpty()) {
            PkiData data = null;
            try {
                data = encryptData(customResponse.getResponseData(), copyCustomRequest);
            } catch (CoreClientException ex) {
                throw new RuntimeException(ex);
            }
            OutputStream outputStream = res.getOutputStream();
            outputStream.write(objectMapper.writeValueAsString(data).getBytes());
            outputStream.flush();
            outputStream.close();
        }

    }

    private void decryptAndSetData(CustomHttpRequestWrapper customRequest, String clientSignaturePublicKey) throws CoreClientException {
        String encryptedPayload = customRequest.getBody();
        String decryptedData = "";
        if (!(encryptedPayload).isEmpty()) {
            String encryptionPrivateKey = pkiUtil.getEncryptionPrivateKey(customRequest);
            decryptedData = SecurityUtil.responseValidator(encryptedPayload, clientSignaturePublicKey, encryptionPrivateKey);
        }
        customRequest.setBody(decryptedData);
    }

    private void handleFilterException(HttpServletResponse httpServletResponse, Exception exe) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        httpServletResponse.setContentType("application/json");
        PrintWriter out = httpServletResponse.getWriter();
        out.print(mapper.writeValueAsString(ClientPKIResponse.builder()
                .code(PkiMessage.INVALID_TOKEN_SIGNATURE_CODE)
                .message(PkiMessage.INVALID_TOKEN_SIGNATURE)
                .build()));
        out.flush();
    }

    private PkiData encryptData(String payload, CustomHttpRequestWrapper requestWrapper) throws CoreClientException {
        String clientEncryptionPublicKey = pkiUtil.getEncryptionPublicKey(requestWrapper);
        String signaturePrivateKey = pkiUtil.getSignaturePrivateKey(requestWrapper);
        return SecurityUtil.encryptPayloadAndGenerateSignature(payload, clientEncryptionPublicKey, signaturePrivateKey);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
