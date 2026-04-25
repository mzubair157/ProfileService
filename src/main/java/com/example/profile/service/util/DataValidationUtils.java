package com.example.profile.service.util;

import com.example.profile.service.exception.InvalidRequestException;
import java.util.Set;

public final class DataValidationUtils {

    private static final Set<String> SUPPORTED_REGIONS = Set.of(AppConstants.REGION_US, AppConstants.REGION_EU);

    private DataValidationUtils() {
    }

    public static void requirePositiveId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new InvalidRequestException(fieldName + " must be a positive number");
        }
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException(fieldName + " must not be blank");
        }
    }

    public static void requireValidRegion(String region) {
        if (region == null || !SUPPORTED_REGIONS.contains(region)) {
            throw new InvalidRequestException("Unsupported region: " + region);
        }
    }

    public static int normalizePageSize(Integer pageSize) {
        if (pageSize == null) {
            return AppConstants.MAX_PAGE_SIZE;
        }
        if (pageSize <= 0) {
            throw new InvalidRequestException("pageSize must be greater than zero");
        }
        return Math.min(pageSize, AppConstants.MAX_PAGE_SIZE);
    }
}
