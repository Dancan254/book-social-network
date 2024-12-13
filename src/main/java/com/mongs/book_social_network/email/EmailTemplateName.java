package com.mongs.book_social_network.email;

public enum EmailTemplateName {

    ACTIVATEACCOUNT("activate-account");

    private final String templateName;
    EmailTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

}
