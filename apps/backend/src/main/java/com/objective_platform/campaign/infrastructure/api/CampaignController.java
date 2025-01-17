package com.objective_platform.campaign.infrastructure.api;

import com.objective_platform.campaign.application.usecases.CreateCampaignCommand;
import com.objective_platform.campaign.application.usecases.CreateCampaignCommandHandler;
import com.objective_platform.campaign.domain.viewmodels.IdResponse;
import com.objective_platform.campaign.infrastructure.api.dto.CreateCampaignDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    private final CreateCampaignCommandHandler handler;

    public CampaignController(CreateCampaignCommandHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<IdResponse> createCampaign(@Valid @RequestBody CreateCampaignDTO dto) {
        CreateCampaignCommand command = new CreateCampaignCommand(
                dto.channel(),
                dto.budget(),
                dto.startDate(),
                dto.endDate());

        var response = handler.handle(command);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
