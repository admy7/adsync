package com.objective_platform.campaign.infrastructure.api;

import com.objective_platform.campaign.application.usecases.*;
import com.objective_platform.campaign.domain.viewmodels.IdResponse;
import com.objective_platform.campaign.infrastructure.api.dto.CreateCampaignDTO;
import com.objective_platform.campaign.infrastructure.api.dto.DeleteCampaignDTO;
import com.objective_platform.campaign.infrastructure.api.dto.UpdateCampaignDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    private final CreateCampaignCommandHandler createCampaignCommandHandler;
    private final DeleteCampaignCommandHandler deleteCampaignCommandHandler;
    private final UpdateCampaignCommandHandler updateCampaignCommandHandler;

    public CampaignController(CreateCampaignCommandHandler createCampaignCommandHandler, DeleteCampaignCommandHandler deleteCampaignCommandHandler, UpdateCampaignCommandHandler updateCampaignCommandHandler) {
        this.createCampaignCommandHandler = createCampaignCommandHandler;
        this.deleteCampaignCommandHandler = deleteCampaignCommandHandler;
        this.updateCampaignCommandHandler = updateCampaignCommandHandler;
    }

    @PostMapping
    public ResponseEntity<IdResponse> createCampaign(@Valid @RequestBody CreateCampaignDTO dto) {
        CreateCampaignCommand command = new CreateCampaignCommand(
                dto.channel(),
                dto.budget(),
                dto.startDate(),
                dto.endDate());

        var response = createCampaignCommandHandler.handle(command);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCampaign(@Valid @RequestBody DeleteCampaignDTO dto) {
        DeleteCampaignCommand command = new DeleteCampaignCommand(dto.id());

        deleteCampaignCommandHandler.handle(command);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping
    public ResponseEntity<Void> updateCampaign(@Valid @RequestBody UpdateCampaignDTO dto) {
        UpdateCampaignCommand command = new UpdateCampaignCommand(
                dto.id(),
                dto._channel(),
                dto._budget(),
                dto._startDate(),
                dto._endDate());

        updateCampaignCommandHandler.handle(command);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
