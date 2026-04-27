package com.auth.domain.Users.enums;


public enum AuthProviderEnums {
    /**
     * LOCAL: Jab user hamare app ke registration form (Email/Password)
     * se account banata hai.
     */
    LOCAL,

    /**
     * GOOGLE: Jab user "Sign in with Google" use karta hai.
     */
    GOOGLE,

    /**
     * GITHUB: Jab user GitHub account se login karta hai.
     */
    GITHUB,

    /**
     * FACEBOOK: Future integration ke liye option.
     */
    FACEBOOK
}
