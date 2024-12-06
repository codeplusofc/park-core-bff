package com.ParkCore.validation;

import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.model.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackValidator {

    public static void validateFeedback(Feedback feedback) {
        if (feedback.getClassification() == null) {
            throw new BadRequestException("You must send classification");
        }
        if (feedback.getComment() == null || feedback.getComment().isEmpty() || feedback.getComment().length() > 500) {
            throw new BadRequestException("Comment is not empty");
        }
    }
}
