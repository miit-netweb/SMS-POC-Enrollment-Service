package Microservices.Auth_Server.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PartnerCredential {
    @NotNull
    private String partnerUuid;
    @NotNull
    private String partnerSecret;

    public PartnerCredential() {
    }

    public PartnerCredential(String uuid, String secret) {
        partnerUuid=uuid;
        partnerSecret=secret;
    }

    public String getPartnerUuid() {
        return partnerUuid;
    }

    public void setPartnerUuid(String partnerUuid) {
        this.partnerUuid = partnerUuid;
    }

    public String getPartnerSecret() {
        return partnerSecret;
    }

    public void setPartnerSecret(String partnerSecret) {
        this.partnerSecret = partnerSecret;
    }

    @Override
    public String toString() {
        return "PartnerCredential{" +
                "partnerUuid='" + partnerUuid + '\'' +
                ", partnerSecret='" + partnerSecret + '\'' +
                '}';
    }
}
