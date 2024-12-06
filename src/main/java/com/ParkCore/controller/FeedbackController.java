package com.ParkCore.controller;

import com.ParkCore.model.Feedback;
import com.ParkCore.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

	private final FeedbackService feedbackService;

	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@Operation(summary = "Provide feedback", description = "This functionality allows a user to provide feedback on an event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Feedback submitted successfully!",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Feedback.class)
					)
			),
			@ApiResponse(responseCode = "400", description = "Error while submitting feedback")
	})
	@PostMapping("/evaluate")
	public ResponseEntity<Feedback> provideFeedback(@RequestBody Feedback feedbackRequest) {
		return ResponseEntity.status(201).body(feedbackService.sendFeedback(feedbackRequest));
	}
}
