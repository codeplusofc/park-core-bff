package com.ParkCore.service;

import com.ParkCore.model.Feedback;
import com.ParkCore.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import static com.ParkCore.validation.FeedbackValidator.validateFeedback;

@Service
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;

	public FeedbackService(FeedbackRepository feedbackRepository) {
		this.feedbackRepository = feedbackRepository;
	}

	public Feedback sendFeedback(Feedback feedback) {
		validateFeedback(feedback);

		return feedbackRepository.save(feedback);
	}

}
