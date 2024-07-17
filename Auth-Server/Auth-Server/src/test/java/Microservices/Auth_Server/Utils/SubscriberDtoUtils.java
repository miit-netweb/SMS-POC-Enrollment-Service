package Microservices.Auth_Server.Utils;

import Microservices.Auth_Server.Dto.EnrollmentDetail;
import Microservices.Auth_Server.Dto.PartnerCredential;
<<<<<<< HEAD
import Microservices.Auth_Server.Dto.SubscriberData;
=======
>>>>>>> 3b4a613d27fb378deabcefa388a1d5b92f57d45f
import Microservices.Auth_Server.Dto.SubscriberDto;

import static org.mockito.Mockito.*;

public class SubscriberDtoUtils {

//    public static SubscriberDto createSubscriberDto() {
//        SubscriberDto subscriberDto = new SubscriberDto();
//
//        PartnerCredential partnerCredentialMock =new PartnerCredential("9c494d20-7e3f-4bf3-b136-5fac32d547f4", "abcd1234abcd1234");
//        EnrollmentDetail enrollmentDetailMock = mock(EnrollmentDetail.class);
//        subscriberDto.setEnrollmentDetail(enrollmentDetailMock);
//        subscriberDto.setPartnerCredential(partnerCredentialMock);
//
//        subscriberDto.getEnrollmentDetail().setPartnerNumber(88889999);
//        subscriberDto.setPartnerCredential(partnerCredentialMock);
//        return subscriberDto;
//    }



    public static SubscriberDto createSubscriberDto() {
        SubscriberDto subscriberDto = new SubscriberDto();

        // Mocking PartnerCredential
        PartnerCredential partnerCredentialMock = mock(PartnerCredential.class);
        lenient().when(partnerCredentialMock.getPartnerSecret()).thenReturn("abcd1234abcd1234");
        lenient().when(partnerCredentialMock.getPartnerUuid()).thenReturn("9c494d20-7e3f-4bf3-b136-5fac32d547f4");

        // Mocking EnrollmentDetail
        EnrollmentDetail enrollmentDetailMock = mock(EnrollmentDetail.class);
<<<<<<< HEAD
        //Mocking  SubscriberData
=======
>>>>>>> 3b4a613d27fb378deabcefa388a1d5b92f57d45f

        // Setting up behavior for EnrollmentDetail mock
        lenient().when(enrollmentDetailMock.getPartnerNumber()).thenReturn(88889999L);

        // Setting mocked objects into SubscriberDto
        subscriberDto.setEnrollmentDetail(enrollmentDetailMock);
        subscriberDto.setPartnerCredential(partnerCredentialMock);

        return subscriberDto;
    }

<<<<<<< HEAD
}
=======
}
 
>>>>>>> 3b4a613d27fb378deabcefa388a1d5b92f57d45f
