package Microservices.Enrollment_Service.exception;

public enum ErrorCodes {

	NULL_RESPONSE(1001, "Response data is null"),
	NULL_PARTNER_DATA(1002,"partner data is null"),
	NULL_PARTNER_SECRET(1003,"partner secret is null"),
	NULL_PARTNER_UUID(1004,"partner uuid is null"),
	ALPHANUMERIC_ERROR(1005,"no alpha numeric value present in partner detail"),
	NULL_ENROLLMENT_DATA(1006,"enrollment data is null"),
	INVALID_PARTNER_NUMBER(1007,"Partner Number should be 8 digit"),
	NULL_SUBSCRIBER_ERROR(1008,"Subscriber Detail is null"),
	ALPHABETIC_ERROR(1009,"Name should only have alphabets"),
	NULL_BILLING_DETAILS(1010,"Billing detail is null"),
	EMPTY_ADDRESS_ERROR(1011,"Address is empty"),
	NULL_CARD_DETAILS(1012,"Card detail is null"),
	INVALID_CARD_NUMBER(1013,"Card number is invalid"),
	EXPIRED_CARD(1014,"Card has expired"),
	
	NO_PARTNER_EXIST(1015,"No such partner exist"),
	INVALID_UUID(1016,"Partner number is invalid,wrong UUID"),
	INVALID_SECRET_KEY(1017,"Partner number is invalid,wrong Secret Key");
	
	
	private int errorCode;
	private String errorMessage;
	
	private ErrorCodes(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
}
