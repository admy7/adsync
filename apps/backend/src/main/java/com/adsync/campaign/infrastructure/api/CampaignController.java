package com.adsync.campaign.infrastructure.api;

import an.awesome.pipelinr.Pipeline;
import com.adsync.campaign.application.usecases.CreateCampaignCommand;
import com.adsync.campaign.application.usecases.DeleteCampaignCommand;
import com.adsync.campaign.application.usecases.GetAllCampaignsQuery;
import com.adsync.campaign.application.usecases.GetCampaignByIdQuery;
import com.adsync.campaign.application.usecases.UpdateCampaignCommand;
import com.adsync.campaign.domain.viewmodels.CampaignViewModel;
import com.adsync.campaign.domain.viewmodels.IdResponse;
import com.adsync.campaign.infrastructure.api.dto.CreateCampaignDTO;
import com.adsync.campaign.infrastructure.api.dto.GetAllCampaignsDTO;
import com.adsync.campaign.infrastructure.api.dto.UpdateCampaignDTO;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {

    private final Pipeline pipeline;
    private final GetAllCampaignsQuery getAllCampaignsQuery;
    private final GetCampaignByIdQuery getCampaignByIdQuery;

    public CampaignController(
            Pipeline pipeline,
            GetAllCampaignsQuery getAllCampaignsQuery,
            GetCampaignByIdQuery getCampaignByIdQuery) {
        this.pipeline = pipeline;
        this.getAllCampaignsQuery = getAllCampaignsQuery;
        this.getCampaignByIdQuery = getCampaignByIdQuery;
    }

    @PostMapping
    public ResponseEntity<IdResponse> createCampaign(@Valid @RequestBody CreateCampaignDTO dto) {
        CreateCampaignCommand command =
                new CreateCampaignCommand(dto.name(), dto._channel(), dto.budget(), dto._start(), dto._end());

        var response = pipeline.send(command);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable String id) {
        DeleteCampaignCommand command = new DeleteCampaignCommand(id);

        pipeline.send(command);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Void> updateCampaign(
            @PathVariable String id, @Valid @RequestBody UpdateCampaignDTO dto) {
        UpdateCampaignCommand command =
                new UpdateCampaignCommand(
                        id, dto._name(), dto._channel(), dto._budget(), dto._start(), dto._end());

        pipeline.send(command);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<GetAllCampaignsDTO> getAllCampaigns() {
        List<CampaignViewModel> campaigns = getAllCampaignsQuery.handle();

        GetAllCampaignsDTO dto = new GetAllCampaignsDTO(campaigns, campaigns.size());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CampaignViewModel> getCampaignById(@PathVariable String id) {
        CampaignViewModel campaign = getCampaignByIdQuery.handle(id);

        return new ResponseEntity<>(campaign, HttpStatus.OK);
    }
}
