package com.github.vcvitaly.tuitask.service;

import com.github.vcvitaly.tuitask.dto.VcsInfoResponseDto;
import com.github.vcvitaly.tuitask.enumeration.VcsProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * VcsApiManagementService.
 *
 * @author Vitalii Chura
 */
@Service
@Slf4j
public class VcsApiManagementService {

    private Map<VcsProviderType, VcsApiService> vcsApiServiceMap = new EnumMap<>(VcsProviderType.class);

    public VcsApiManagementService(List<VcsApiService> vcsApiServices) {
        vcsApiServices.forEach(service -> vcsApiServiceMap.put(service.getVcsProviderType(), service));
    }

    public List<VcsInfoResponseDto> getVcsDetails(String userName, VcsProviderType vcsProviderType) {
        return getVcsApiService(vcsProviderType).getVcsDetails(userName);
    }

    private VcsApiService getVcsApiService(VcsProviderType vcsProviderType) {
        if (!vcsApiServiceMap.containsKey(vcsProviderType)) {
            log.error("There is no service for provider type {}", vcsProviderType);
            throw new IllegalArgumentException("There is no service for provider type " + vcsProviderType);
        }

        return vcsApiServiceMap.get(vcsProviderType);
    }
}
