package Microservices.Auth_Server.Service;

import Microservices.Auth_Server.Dto.ResponseDto;
import Microservices.Auth_Server.Dto.SubscriberDto;
import Microservices.Auth_Server.Entity.PartnerDetail;
import Microservices.Auth_Server.Entity.Subscriber;
import Microservices.Auth_Server.Repository.PartnerRepository;
import Microservices.Auth_Server.Repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private SubscriberRepository repository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public static boolean isAlphaNumeric(String str) {
        String regex = "^[a-zA-Z0-9]*$";
        return str.matches(regex);
    }



    public String saveUser(Subscriber credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "user added to the system";
    }

    public static boolean isAlpha(
            String str)
    {
        if (str == null || str == "") {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character
                    .isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isUUID(String uuid) {
        String uuidPattern =
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$" + "|" +
                        "^[0-9a-fA-F]{32}$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(uuidPattern);

        // Match the input against the pattern
        Matcher matcher = pattern.matcher(uuid);

        return matcher.matches();
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public ResponseDto ValidateResponse(SubscriberDto subscriber){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(400);

        if(subscriber ==  null){
            responseDto.setMessage("Response data is null");
            return responseDto;
        } else if(subscriber.getPartnerCredential() == null ){
            responseDto.setMessage("partner data is null");
            return responseDto;
        } else if(subscriber.getPartnerCredential().getPartnerSecret() == null ||
                subscriber.getPartnerCredential().getPartnerSecret().isEmpty()
        ){
            responseDto.setMessage("partner secret is null");
            return responseDto;
        } else if(subscriber.getPartnerCredential().getPartnerUuid() == null ||
                subscriber.getPartnerCredential().getPartnerUuid().isEmpty()
        ){
            responseDto.setMessage("partner uuid is null");
            return responseDto;
        }else if(!isAlphaNumeric(subscriber.getPartnerCredential().getPartnerSecret()) ||
                !isUUID(subscriber.getPartnerCredential().getPartnerUuid())){
            responseDto.setMessage("no alpha numeric value present in partner detail");
            return responseDto;
        } else if (subscriber.getEnrollmentDetail()==null) {
            responseDto.setMessage("enrollment data is null");
            return responseDto;
        } else if(subscriber.getEnrollmentDetail().getPartnerNumber()>99999999 ||
                subscriber.getEnrollmentDetail().getPartnerNumber()<10000000 ){
            responseDto.setMessage("Partner Number should be 8 digit");
            return responseDto;
        }else if(subscriber.getEnrollmentDetail().getSubscriberData()==null){
            responseDto.setMessage("Subscriber Detail is null");
            return responseDto;
        } else if(!isAlpha(subscriber.getEnrollmentDetail().getSubscriberData().getFirstName()) ||
                !isAlpha(subscriber.getEnrollmentDetail().getSubscriberData().getLastName())
        ) {
            responseDto.setMessage("Name should only have alphabets");
            return responseDto;
        } else if(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail()==null){
            responseDto.setMessage("Billing detail is null");
            return responseDto;
        } else if(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getAddress().length()==0){
            responseDto.setMessage("Address is empty");
            return responseDto;
        } else if(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail()==null){
            responseDto.setMessage("Card detail is null");
            return responseDto;
        } else if(16>subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber().length() ||
                subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardNumber().length()<16
        ){
            responseDto.setMessage("Card number is invalid");
            return responseDto;
        } else if(subscriber.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail().getCardExpiry().isBefore(LocalDate.now())){
            responseDto.setMessage("Card has expired");
            return responseDto;
        }
        return null;
    }

    public ResponseDto checkPartnerNumber(SubscriberDto user){
        PartnerDetail partnerDetail = partnerRepository.findByPartnerNumber(user.getEnrollmentDetail().getPartnerNumber());
        if(partnerDetail == null){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setCode(400);
            responseDto.setMessage("No such partner exist");
            return responseDto;
        } else if (!partnerDetail.getPartnerUuid().equals(user.getPartnerCredential().getPartnerUuid())) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setCode(400);
            responseDto.setMessage("Partner number is invalid,wrong UUID");
            return responseDto;
        }
        else if (!partnerDetail.getPartnerSecret().equals(user.getPartnerCredential().getPartnerSecret())) {
            ResponseDto responseDto = new ResponseDto();
            responseDto.setCode(400);
            responseDto.setMessage("Partner number is invalid,wrong Secret Key");
            return responseDto;
        }
            return null;
    }



}