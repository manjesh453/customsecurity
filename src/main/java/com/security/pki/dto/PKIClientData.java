package com.security.pki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PKIClientData {
    private String signature;
    private String secretKey;
    private String data;
    private String clientKey;

    public static PKIClientDataBuilder builder() {
        return new PKIClientDataBuilder();
    }

    public static class PKIClientDataBuilder {
        private String signature;
        private String secretKey;
        private String data;
        private String clientKey;

        PKIClientDataBuilder() {
        }

        public PKIClientDataBuilder signature(String signature) {
            this.signature = signature;
            return this;
        }

        public PKIClientDataBuilder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public PKIClientDataBuilder data(String data) {
            this.data = data;
            return this;
        }

        public PKIClientDataBuilder clientKey(String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public PKIClientData build() {
            return new PKIClientData(this.signature, this.secretKey, this.data, this.clientKey);
        }

        public String toString() {
            return "PKIClientData.PKIClientDataBuilder(signature=" + this.signature + ", secretKey=" + this.secretKey + ", data=" + this.data + ", clientKey=" + this.clientKey + ")";
        }
    }
}
