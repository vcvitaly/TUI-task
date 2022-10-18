package com.github.vcvitaly.tuitask.dto;

import lombok.Data;

/**
 * BranchDetailsDto.
 *
 * @author Vitalii Chura
 */
@Data
public class BranchDetailsDto {

    private String name;

    private String lastCommitSha;
}
