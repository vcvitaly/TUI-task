package com.github.vcvitaly.tuitask.service;

import com.github.vcvitaly.tuitask.dto.VcsInfoResponseDto;
import com.github.vcvitaly.tuitask.enumeration.VcsProviderType;

import java.util.List;

/**
 * VcsApiService.
 *
 * @author Vitalii Chura
 */
public interface VcsApiService {

    List<VcsInfoResponseDto> getVcsDetails(String userName);

    VcsProviderType getVcsProviderType();
}
