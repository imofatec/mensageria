package com.imo.producer_service.models.user;

public record NoPasswordUser(
    String id,
    String name,
    String email,
    String isConfirmed,
    String createdAt,
    String updatedAt
) {
}
