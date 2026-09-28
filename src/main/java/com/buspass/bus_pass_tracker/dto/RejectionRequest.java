package com.buspass.bus_pass_tracker.dto;

import jakarta.validation.constraints.NotBlank;

public class RejectionRequest {

    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;

    private String adminRemark;

    public RejectionRequest() {
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }
}