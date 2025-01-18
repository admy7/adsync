package com.objective_platform.campaign.infrastructure.api;

import an.awesome.pipelinr.Pipeline;
import com.objective_platform.campaign.application.usecases.CreateCampaignCommand;
import com.objective_platform.campaign.application.usecases.DeleteCampaignCommand;
import com.objective_platform.campaign.application.usecases.UpdateCampaignCommand;
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

    private final Pipeline pipeline;

    public CampaignController(Pipeline pipeline, Pipeline pipeline1) {
        this.pipeline = pipeline1;
    }

    @PostMapping
    public ResponseEntity<IdResponse> createCampaign(@Valid @RequestBody CreateCampaignDTO dto) {
        CreateCampaignCommand command = new CreateCampaignCommand(
                dto.channel(),
                dto.budget(),
                dto.startDate(),
                dto.endDate());

        var response = pipeline.send(command);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCampaign(@Valid @RequestBody DeleteCampaignDTO dto) {
        DeleteCampaignCommand command = new DeleteCampaignCommand(dto.id());

        pipeline.send(command);

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

        pipeline.send(command);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
