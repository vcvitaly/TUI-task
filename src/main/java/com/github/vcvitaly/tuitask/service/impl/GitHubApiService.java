package com.github.vcvitaly.tuitask.service.impl;

import com.github.vcvitaly.tuitask.dto.BranchDetailsDto;
import com.github.vcvitaly.tuitask.dto.VcsInfoResponseDto;
import com.github.vcvitaly.tuitask.enumeration.VcsProviderType;
import com.github.vcvitaly.tuitask.exception.VcsApiCommunicationIOException;
import com.github.vcvitaly.tuitask.service.VcsApiService;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GitHubApiService.
 *
 * @author Vitalii Chura
 */
@Service
@Slf4j
public class GitHubApiService implements VcsApiService {

    @Value("${vcs.github.token}")
    private String token;

    @Override
    public List<VcsInfoResponseDto> getVcsDetails(String userName) {
        log.debug("Getting repo details for user {} from GitHub", userName);
        try {
            GitHub github = new GitHubBuilder().withOAuthToken(token).build();
            var repositoriesMap = github.getUser(userName).getRepositories();
            return repositoriesMap.values().stream()
                    .map(this::toVcsInfoResponseDto)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw getApiCommunicationIOException(e);
        }
    }

    private VcsApiCommunicationIOException getApiCommunicationIOException(IOException e) {
        return new VcsApiCommunicationIOException("An exception happened during communication with GItHub API", e);
    }

    @Override
    public VcsProviderType getVcsProviderType() {
        return VcsProviderType.GITHUB;
    }

    private VcsInfoResponseDto toVcsInfoResponseDto(GHRepository repo) {
        var responseDto = new VcsInfoResponseDto();
        responseDto.setRepoName(repo.getName());
        responseDto.setOwnerLogin(repo.getOwnerName());
        Map<String, GHBranch> branchesMap;
        try {
            branchesMap = repo.getBranches();
        } catch (IOException e) {
            throw getApiCommunicationIOException(e);
        }
        var branchDetailsDtos = branchesMap.values().stream()
                .map(this::toBranchDetailsDto)
                .toList();
        responseDto.setBranchesDetails(branchDetailsDtos);
        return responseDto;
    }

    private BranchDetailsDto toBranchDetailsDto(GHBranch branch) {
        var branchDetailsDto = new BranchDetailsDto();
        branchDetailsDto.setName(branch.getName());
        branchDetailsDto.setLastCommitSha(branch.getSHA1());
        return branchDetailsDto;
    }
}
