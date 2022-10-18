package com.github.vcvitaly.tuitask.dto;

import lombok.Data;

import java.util.List;

/**
 * VcsInfoResponseDto.
 *
 * @author Vitalii Chura
 */
@Data
public class VcsInfoResponseDto {

    private String repoName;

    private String ownerLogin;

    private List<BranchDetailsDto> branchesDetails;
}
