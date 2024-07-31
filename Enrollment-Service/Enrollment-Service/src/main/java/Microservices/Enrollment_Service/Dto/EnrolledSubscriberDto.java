package Microservices.Enrollment_Service.Dto;

import Microservices.Enrollment_Service.Entity.Subscriber;

public class EnrolledSubscriberDto {
    private String token;
    private Subscriber subscriber;

    public EnrolledSubscriberDto() {
    }

    public EnrolledSubscriberDto(String token, Subscriber subscriber) {
        this.token = token;
        this.subscriber = subscriber;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
