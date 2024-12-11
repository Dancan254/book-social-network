package com.mongs.book_social_network.email;

public enum EmailTemplateName {

    ACTIVATEACCOUNT("activate-account");

    private final String name;
    EmailTemplateName(String templateName) {
        name = templateName;
    }

}
