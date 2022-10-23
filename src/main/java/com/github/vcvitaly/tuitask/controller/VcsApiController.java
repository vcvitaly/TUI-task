package com.github.vcvitaly.tuitask.controller;

import com.github.vcvitaly.tuitask.dto.VcsInfoResponseDto;
import com.github.vcvitaly.tuitask.enumeration.VcsProviderType;
import com.github.vcvitaly.tuitask.exception.handler.ErrorResponse;
import com.github.vcvitaly.tuitask.service.VcsApiManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * GIthubController.
 *
 * @author Vitalii Chura
 */
@RestController
@RequestMapping("/api/v1/vcs-api")
public class VcsApiController {

    private VcsApiManagementService vcsApiManagementService;

    public VcsApiController(VcsApiManagementService vcsApiManagementService) {
        this.vcsApiManagementService = vcsApiManagementService;
    }

    @Operation(summary = "Get information about users' repositories and branches by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When information is successfully fetched"),
            @ApiResponse(responseCode = "404", description = "When some resource is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "406", description = "When the requested media type is not supported",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "When a server error occurred",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{userName}/github/repos", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<VcsInfoResponseDto> getGithubRepoDetails(@PathVariable("userName") String userName) {
        return vcsApiManagementService.getVcsDetails(userName, VcsProviderType.GITHUB);
    }
}
