package Microservices.Auth_Server.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import Microservices.Auth_Server.Dto.*;
import Microservices.Auth_Server.Utils.SubscriberDtoUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Microservices.Auth_Server.Entity.PartnerDetail;
import Microservices.Auth_Server.Repository.PartnerRepository;
import Microservices.Auth_Server.Service.AuthService;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  PartnerRepository partnerRepository;

  @InjectMocks
  AuthService service;

  @Test
  public void testValidPartnerUuid() {
    assertTrue(AuthService.isUUID("9c494d20-7e3f-4bf3-b136-5fac32d547f4"));
  }

  @Test
  public void testNullPartnerUuid() {
  }

  @Test
  public void testInvalidPartnerUuid() {
    assertFalse(AuthService.isUUID("12345678-1234-1234-1234-1234567890abc"));
  }

  @Test
  public void testValidAlphaNumeric() {
    assertTrue(AuthService.isAlphaNumeric("abcd1234abcd1234"));
  }

  @Test
  public void testInvalidAlphaNumeric() {
    assertFalse(AuthService.isAlphaNumeric("abcd&$$dnb"));
  }

  @Test
  public void testNullSubscriber() {
    SubscriberDto subscriber = null;
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400, responseDto.getCode());
    assertEquals("Response data is null", responseDto.getMessage());
  }

  @Test
  public void testNullPartnerCredential(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    subscriber.setPartnerCredential(null);
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("partner data is null",responseDto.getMessage());
  }

  @Test
  public void testNullPartnerSecret(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    when(subscriber.getPartnerCredential().getPartnerSecret()).thenReturn(null);
    ResponseDto res = service.ValidateResponse(subscriber);
    assertEquals(400,res.getCode());
    assertEquals("partner secret is null",res.getMessage());
  }

  @Test
  public void testNullPartnerUUID(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    when(subscriber.getPartnerCredential().getPartnerUuid()).thenReturn(null);
    ResponseDto res = service.ValidateResponse(subscriber);
    assertEquals(400,res.getCode());
    assertEquals("partner uuid is null",res.getMessage());
  }

  @Test
  public void testNotAlphaNumericValue(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    when(subscriber.getPartnerCredential().getPartnerSecret()).thenReturn("ehfvb%&djwb");
    ResponseDto res = service.ValidateResponse(subscriber);
    assertEquals(400,res.getCode());
    assertEquals("no alpha numeric value present in partner detail",res.getMessage());
  }

  @Test
  public void testNullEnrollmentDetail(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    subscriber.setEnrollmentDetail(null);
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("enrollment data is null",responseDto.getMessage());
  }

  @Test
  public void testPartnerNumberLength(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    when(subscriber.getEnrollmentDetail().getPartnerNumber()).thenReturn(420L);
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Partner Number should be 8 digit",responseDto.getMessage());
  }

  @Test
  public void testNullSubscriberDetail(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    subscriber.getEnrollmentDetail().setSubscriberData(null);
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Subscriber Detail is null",responseDto.getMessage());
  }

  @Test
  public void testName(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    SubscriberData subscriberData = mock(SubscriberData.class);
    lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
    lenient().when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("1netweb");
    lenient().when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("22web");
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Name should only have alphabets",responseDto.getMessage());
  }

  @Test
  public void testBillingDetails(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    SubscriberData subscriberData = mock(SubscriberData.class);
    when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(null);
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Billing detail is null",responseDto.getMessage());
  }

  @Test
  public void testAddress(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    SubscriberData subscriberData = mock(SubscriberData.class);
    BillingDetail billingDetail = mock(BillingDetail.class);
    lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("");
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Address is empty",responseDto.getMessage());
  }

  @Test
  public void testnullCardDetail(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    SubscriberData subscriberData = mock(SubscriberData.class);
    BillingDetail billingDetail = mock(BillingDetail.class);
    lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("bdq");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail()).thenReturn(null);
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Card detail is null",responseDto.getMessage());
  }

  @Test
  public void testCardNumber(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    SubscriberData subscriberData = mock(SubscriberData.class);
    BillingDetail billingDetail = mock(BillingDetail.class);
    CardDetail cardDetail = mock(CardDetail.class);
    lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("bdq");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail()).thenReturn(cardDetail);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber()).thenReturn("123456");
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Card number is invalid",responseDto.getMessage());
  }

  @Test
  public void testCardExpiry(){
    SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
    SubscriberData subscriberData = mock(SubscriberData.class);
    BillingDetail billingDetail = mock(BillingDetail.class);
    CardDetail cardDetail = mock(CardDetail.class);
    lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("bdq");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail()).thenReturn(cardDetail);
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber()).thenReturn("1234567891012345");
    when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardExpiry()).thenReturn(LocalDate.of(2024,07,15));
    ResponseDto responseDto = service.ValidateResponse(subscriber);
    assertEquals(400,responseDto.getCode());
    assertEquals("Card has expired",responseDto.getMessage());
  }

  @Test
  public void testPartnerNotFound() {
    SubscriberDto user = SubscriberDtoUtils.createSubscriberDto();
    when(partnerRepository.findByPartnerNumber(ArgumentMatchers.anyLong())).thenReturn(null);
    ResponseDto responseDto = service.checkPartnerNumber(user);
    assertEquals(400, responseDto.getCode());
    assertEquals("No such partner exist", responseDto.getMessage());
  }

  @Test
  public void testInvalidPartnerByUuid() {
    SubscriberDto subscriberDto = SubscriberDtoUtils.createSubscriberDto();
    PartnerDetail mockPartnerDetail = new PartnerDetail();
    mockPartnerDetail.setPartnerUuid("wedfehbfdiu2hdiu2hw");
    when(partnerRepository.findByPartnerNumber(anyLong())).thenReturn(mockPartnerDetail);
    ResponseDto responseDto = service.checkPartnerNumber(subscriberDto);
    assertNotNull(responseDto);
    assertEquals(400, responseDto.getCode());
    assertEquals("Partner number is invalid,wrong UUID", responseDto.getMessage());
  }

  @Test
  public void testInvalidPartnerBySecret() {
    SubscriberDto subscriberDto = SubscriberDtoUtils.createSubscriberDto();

    PartnerDetail mockPartnerDetail = new PartnerDetail();
    mockPartnerDetail.setPartnerUuid("9c494d20-7e3f-4bf3-b136-5fac32d547f4");
    mockPartnerDetail.setPartnerSecret("jhbdhbdhbd12");

    when(partnerRepository.findByPartnerNumber(anyLong())).thenReturn(mockPartnerDetail);
    ResponseDto responseDto = service.checkPartnerNumber(subscriberDto);

    assertNotNull(responseDto);
    assertEquals("Partner number is invalid,wrong Secret Key", responseDto.getMessage());
    assertEquals(400, responseDto.getCode());

  }

  @Test
  public void testValidPartnerNumber() {
    SubscriberDto subscriberDto = SubscriberDtoUtils.createSubscriberDto();

    PartnerDetail mockPartnerDetail = new PartnerDetail();
    mockPartnerDetail.setPartnerUuid("9c494d20-7e3f-4bf3-b136-5fac32d547f4");
    mockPartnerDetail.setPartnerSecret("abcd1234abcd1234");

    when(partnerRepository.findByPartnerNumber(anyLong())).thenReturn(mockPartnerDetail);
    ResponseDto responseDto = service.checkPartnerNumber(subscriberDto);

    assertNull(responseDto);
  }

}