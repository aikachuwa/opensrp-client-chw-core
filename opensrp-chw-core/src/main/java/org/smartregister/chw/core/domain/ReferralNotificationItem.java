package org.smartregister.chw.core.domain;

import java.util.List;

public class ReferralNotificationItem {
    private String title;
    private List<String> details;

    public ReferralNotificationItem(String title, List<String> details) {
        this.title = title;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDetails() {
        return details;
    }
}