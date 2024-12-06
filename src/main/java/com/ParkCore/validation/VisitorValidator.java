package com.ParkCore.validation;

import com.ParkCore.model.Visitor;
import com.ParkCore.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ParkCore.util.ValidationUtils.isPhoneNumberValid;
import static com.ParkCore.util.ValidationUtils.isSsnValid;

@Component
public class VisitorValidator {

    @Autowired
    private VisitorRepository visitorRepository;

    public static void validateVisitorCpf(Visitor visitor) {
        if (!isSsnValid(visitor.getCpf())) { // CPF is similar to SSN
            throw new RuntimeException("Invalid SSN! Please enter a valid SSN!");
        }
    }

    public static void validateVisitorPhone(Visitor visitor) {
        if (!isPhoneNumberValid(visitor.getPhone())) {
            throw new RuntimeException("The provided number is invalid!");
        }
    }
}
