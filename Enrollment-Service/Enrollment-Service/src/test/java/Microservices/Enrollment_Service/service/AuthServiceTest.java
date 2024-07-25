package Microservices.Enrollment_Service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import Microservices.Enrollment_Service.Dto.BillingDetail;
import Microservices.Enrollment_Service.Dto.CardDetail;
import Microservices.Enrollment_Service.Dto.SubscriberData;
import Microservices.Enrollment_Service.Dto.SubscriberDto;
import Microservices.Enrollment_Service.Repository.PartnerRepository;
import Microservices.Enrollment_Service.Service.AuthService;
import Microservices.Enrollment_Service.Utils.SubscriberDtoUtils;
import Microservices.Enrollment_Service.exception.ErrorCodes;
import Microservices.Enrollment_Service.exception.ValidationException;

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
		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(null);
		});
		assertEquals(ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testNullPartnerCredential() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		subscriber.setPartnerCredential(null);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.NULL_PARTNER_DATA.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_PARTNER_DATA.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testNullPartnerSecret() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		when(subscriber.getPartnerCredential().getPartnerSecret()).thenReturn(null);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.NULL_PARTNER_SECRET.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_PARTNER_SECRET.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testNullPartnerUUID() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		when(subscriber.getPartnerCredential().getPartnerUuid()).thenReturn(null);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.NULL_PARTNER_UUID.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_PARTNER_UUID.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testNotAlphaNumericValue() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		when(subscriber.getPartnerCredential().getPartnerSecret()).thenReturn("ehfvb%&djwb");

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.ALPHANUMERIC_ERROR.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.ALPHANUMERIC_ERROR.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testNullEnrollmentDetail() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		subscriber.setEnrollmentDetail(null);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.NULL_ENROLLMENT_DATA.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_ENROLLMENT_DATA.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testPartnerNumberLength() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		when(subscriber.getEnrollmentDetail().getPartnerNumber()).thenReturn(420L);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.INVALID_PARTNER_NUMBER.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.INVALID_PARTNER_NUMBER.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testNullSubscriberDetail() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		subscriber.getEnrollmentDetail().setSubscriberData(null);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testName() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("1netweb");
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("22web");

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.ALPHABETIC_ERROR.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.ALPHABETIC_ERROR.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	void testInvalidMobileNumber() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber()).thenReturn(83727131L);
		
		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.INVALID_MOBILE_NUMBER.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.INVALID_MOBILE_NUMBER.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testBillingDetails() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber()).thenReturn(8736234567L);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(null);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.NULL_BILLING_DETAILS.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_BILLING_DETAILS.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testAddress() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		BillingDetail billingDetail = mock(BillingDetail.class);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber()).thenReturn(9876543210L);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("");

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.EMPTY_ADDRESS_ERROR.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.EMPTY_ADDRESS_ERROR.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testnullCardDetail() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		BillingDetail billingDetail = mock(BillingDetail.class);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber()).thenReturn(9876543210L);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("bdq");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail()).thenReturn(null);

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.NULL_CARD_DETAILS.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.NULL_CARD_DETAILS.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testCardNumber() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		BillingDetail billingDetail = mock(BillingDetail.class);
		CardDetail cardDetail = mock(CardDetail.class);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber()).thenReturn(9876543210L);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("bdq");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail())
				.thenReturn(cardDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber())
				.thenReturn("123456789101");

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.INVALID_CARD_NUMBER.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.INVALID_CARD_NUMBER.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	public void testCardExpiry() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		BillingDetail billingDetail = mock(BillingDetail.class);
		CardDetail cardDetail = mock(CardDetail.class);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber()).thenReturn(9876543210L);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("bdq");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail())
				.thenReturn(cardDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber())
				.thenReturn("1234567891012345");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardExpiry())
				.thenReturn(LocalDate.of(2024, 07, 15));

		ValidationException thrown = assertThrows(ValidationException.class, () -> {
			service.ValidateResponse(subscriber);
		});
		assertEquals(ErrorCodes.EXPIRED_CARD.getErrorCode(), thrown.getErrorcode());
		assertEquals(ErrorCodes.EXPIRED_CARD.getErrorMessage(), thrown.getMessage());
		assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
	}

	@Test
	void testValidResponse() {
		SubscriberDto subscriber = SubscriberDtoUtils.createSubscriberDto();
		SubscriberData subscriberData = mock(SubscriberData.class);
		BillingDetail billingDetail = mock(BillingDetail.class);
		CardDetail cardDetail = mock(CardDetail.class);
		lenient().when(subscriber.getEnrollmentDetail().getSubscriberData()).thenReturn(subscriberData);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()).thenReturn("netweb");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getLastName()).thenReturn("web");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber()).thenReturn(7834671245L);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()).thenReturn(billingDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress()).thenReturn("bdq");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail())
				.thenReturn(cardDetail);
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber())
				.thenReturn("1234567891012345");
		when(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardExpiry())
				.thenReturn(LocalDate.of(2028, 07, 15));

		assertTrue(service.ValidateResponse(subscriber));
	}

}