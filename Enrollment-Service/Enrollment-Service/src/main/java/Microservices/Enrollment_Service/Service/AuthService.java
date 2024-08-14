package Microservices.Enrollment_Service.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Microservices.Enrollment_Service.Dto.EnrollmentStatus;
import Microservices.Enrollment_Service.Publisher.EnrollmentStatusProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Microservices.Enrollment_Service.Dto.ExceptionResponse;
import Microservices.Enrollment_Service.Dto.SubscriberDto;
import Microservices.Enrollment_Service.Entity.PersonalDetails;
import Microservices.Enrollment_Service.Entity.Subscriber;
import Microservices.Enrollment_Service.Repository.PersonalDetailsRepository;
import Microservices.Enrollment_Service.Repository.SubscriberRepository;
import Microservices.Enrollment_Service.exception.DuplicateEntryException;
import Microservices.Enrollment_Service.exception.ErrorCodes;
import Microservices.Enrollment_Service.exception.ValidationException;

@Service
public class AuthService {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static SecureRandom random = new SecureRandom();
	@Autowired
	private SubscriberRepository subscriberRepository;
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	@Autowired
	private EnrollmentStatusProducer enrollmentStatusProducer;

	Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	public static String generateRandomAlphaNumeric() {
		StringBuilder sb = new StringBuilder(10);
		for (int i = 0; i < 10; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(randomIndex));
		}
		return sb.toString();
	}

	public static boolean isAlphaNumeric(String str) {
		String regex = "^[a-zA-Z0-9]*$";
		return str.matches(regex);
	}

	public static boolean isAlpha(String str) {
		if (str == null || str == "") {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isLetter(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isUUID(String uuid) {
		String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$" + "|"
				+ "^[0-9a-fA-F]{32}$";

		// Compile the regex pattern
		Pattern pattern = Pattern.compile(uuidPattern);

		// Match the input against the pattern
		Matcher matcher = pattern.matcher(uuid);

		return matcher.matches();
	}

	public boolean ValidateResponse(SubscriberDto subscriber) {

		if (subscriber == null) {
			throw new ValidationException(ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorCode(),
					ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorMessage(), HttpStatus.BAD_REQUEST);
		} else if (subscriber.getPartnerCredential() == null) {
			throw new ValidationException(ErrorCodes.NULL_PARTNER_DATA.getErrorCode(),
					ErrorCodes.NULL_PARTNER_DATA.getErrorMessage(), HttpStatus.BAD_REQUEST);
		} else if (subscriber.getPartnerCredential().getPartnerSecret() == null
				|| subscriber.getPartnerCredential().getPartnerSecret().isEmpty()) {
			throw new ValidationException(ErrorCodes.NULL_PARTNER_SECRET.getErrorCode(),
					ErrorCodes.NULL_PARTNER_SECRET.getErrorMessage(), HttpStatus.BAD_REQUEST);
		} else if (subscriber.getPartnerCredential().getPartnerUuid() == null
				|| subscriber.getPartnerCredential().getPartnerUuid().isEmpty()) {
			throw new ValidationException(ErrorCodes.NULL_PARTNER_UUID.getErrorCode(),
					ErrorCodes.NULL_PARTNER_UUID.getErrorMessage(), HttpStatus.BAD_REQUEST);
		} else if (subscriber.getPartnerCredential().getPartnerUuid().length() != 36) {
			throw new ValidationException(ErrorCodes.INVALID_UUID.getErrorCode(),
					ErrorCodes.INVALID_UUID.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (!isAlphaNumeric(subscriber.getPartnerCredential().getPartnerSecret())
				|| !isUUID(subscriber.getPartnerCredential().getPartnerUuid())) {
			throw new ValidationException(ErrorCodes.ALPHANUMERIC_ERROR.getErrorCode(),
					ErrorCodes.ALPHANUMERIC_ERROR.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (subscriber.getEnrollmentDetail() == null) {
			throw new ValidationException(ErrorCodes.NULL_ENROLLMENT_DATA.getErrorCode(),
					ErrorCodes.NULL_ENROLLMENT_DATA.getErrorMessage(), HttpStatus.BAD_REQUEST);
		} else if (subscriber.getEnrollmentDetail().getPartnerNumber() > 99999999
				|| subscriber.getEnrollmentDetail().getPartnerNumber() < 10000000) {
			throw new ValidationException(ErrorCodes.INVALID_PARTNER_NUMBER.getErrorCode(),
					ErrorCodes.INVALID_PARTNER_NUMBER.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (subscriber.getEnrollmentDetail().getSubscriberData() == null) {

			throw new ValidationException(ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorCode(),
					ErrorCodes.NULL_SUBSCRIBER_ERROR.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (!isAlpha(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName())
				|| !isAlpha(subscriber.getEnrollmentDetail().getSubscriberData().getLastName())) {

			throw new ValidationException(ErrorCodes.ALPHABETIC_ERROR.getErrorCode(),
					ErrorCodes.ALPHABETIC_ERROR.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (String.valueOf(subscriber.getEnrollmentDetail().getSubscriberData().getPhoneNumber())
				.length() != 10) {
			throw new ValidationException(ErrorCodes.INVALID_MOBILE_NUMBER.getErrorCode(),
					ErrorCodes.INVALID_MOBILE_NUMBER.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail() == null) {

			throw new ValidationException(ErrorCodes.NULL_BILLING_DETAILS.getErrorCode(),
					ErrorCodes.NULL_BILLING_DETAILS.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress().length() == 0) {

			throw new ValidationException(ErrorCodes.EMPTY_ADDRESS_ERROR.getErrorCode(),
					ErrorCodes.EMPTY_ADDRESS_ERROR.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail() == null) {

			throw new ValidationException(ErrorCodes.NULL_CARD_DETAILS.getErrorCode(),
					ErrorCodes.NULL_CARD_DETAILS.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail()
				.getCardNumber().length() != 16) {

			throw new ValidationException(ErrorCodes.INVALID_CARD_NUMBER.getErrorCode(),
					ErrorCodes.INVALID_CARD_NUMBER.getErrorMessage(), HttpStatus.BAD_REQUEST);

		} else if (subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail()
				.getCardExpiry().isBefore(LocalDate.now())) {
			throw new ValidationException(ErrorCodes.EXPIRED_CARD.getErrorCode(),
					ErrorCodes.EXPIRED_CARD.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}
		return true;
	}

	@Transactional
	public Subscriber enrollNewSubscriber(SubscriberDto user, String subscriberNumber) {
		final PersonalDetails personalDetails;

		try {
			personalDetails = personalDetailsRepository.save(new PersonalDetails(
					user.getEnrollmentDetail().getSubscriberData().getFirstName(),
					user.getEnrollmentDetail().getSubscriberData().getLastName(),
					user.getEnrollmentDetail().getSubscriberData().getPhoneNumber(),
					user.getEnrollmentDetail().getSubscriberData().getEmail(),
					user.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress(),
					user.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber(),
					user.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardType(),
					user.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardHolder(),
					user.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardExpiry()));
		} catch (RuntimeException ex) {
			CompletableFuture.runAsync(()->{
				enrollmentStatusProducer.sendMessage(new EnrollmentStatus(LocalDate.now().toString(),"ENROLLMENT-FAILURE"));
			});
			LOGGER.error("Original exception: {}", ex.getMessage());
			String errorMessage = ExceptionResponse.parseErrorMessage(ex.getMessage());
			throw new DuplicateEntryException(ErrorCodes.DUPLICATE_ENTRY.getErrorCode(), errorMessage,
					HttpStatus.BAD_REQUEST);
		}

		try {
			Subscriber subscriber = new Subscriber(subscriberNumber, user.getEnrollmentDetail().getPartnerNumber(),
					user.getEnrollmentDetail().getSubscriptionData().getSubtypeNumber(), personalDetails);
			CompletableFuture.runAsync(()->{
				enrollmentStatusProducer.sendMessage(new EnrollmentStatus(LocalDate.now().toString(),"ENROLLMENT-SUCCESS"));
			});
			return subscriberRepository.save(subscriber);

		} catch (RuntimeException ex) {
			CompletableFuture.runAsync(()->{
				enrollmentStatusProducer.sendMessage(new EnrollmentStatus(LocalDate.now().toString(),"ENROLLMENT-FAILURE"));
			});
			LOGGER.error("Original exception: {}", ex.getMessage());
			throw new DuplicateEntryException(ErrorCodes.DUPLICATE_ENTRY.getErrorCode(),
					"Some issue Occured While Creating Your Subscriber Number,Please Try Again",
					HttpStatus.BAD_REQUEST);

		}

	}
}