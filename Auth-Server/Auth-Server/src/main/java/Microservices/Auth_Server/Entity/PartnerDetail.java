package Microservices.Auth_Server.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PartnerDetail {

    @Id
    private long partnerNumber;
    private String partnerName;
    @Column(unique = true)
    private String partnerUuid;
    @Column(unique = true)
    private String partnerSecret;

    public PartnerDetail() {
    }

    public PartnerDetail(long partnerNumber, String partnerName, String partnerUuid, String partnerSecret) {
        this.partnerNumber = partnerNumber;
        this.partnerName = partnerName;
        this.partnerUuid = partnerUuid;
        this.partnerSecret = partnerSecret;
    }

    public long getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(long partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
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
        return "PartnerDetail{" +
                "partnerNumber=" + partnerNumber +
                ", partnerName='" + partnerName + '\'' +
                ", partnerUuid='" + partnerUuid + '\'' +
                ", partnerSecret='" + partnerSecret + '\'' +
                '}';
    }
}
