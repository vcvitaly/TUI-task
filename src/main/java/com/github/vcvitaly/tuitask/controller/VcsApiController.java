package com.github.vcvitaly.tuitask.controller;

import com.github.vcvitaly.tuitask.dto.VcsInfoResponseDto;
import com.github.vcvitaly.tuitask.enumeration.VcsProviderType;
import com.github.vcvitaly.tuitask.service.VcsApiManagementService;
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

    @GetMapping("/{userName}/repos/github")
    public List<VcsInfoResponseDto> getGithubRepoDetails(@PathVariable("userName") String userName) {
        return vcsApiManagementService.getVcsDetails(userName, VcsProviderType.GITHUB);
    }
}
