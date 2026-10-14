package com.example.LearningPlatformApp.exception;

public class BusinessRuleViolationException
        extends RuntimeException {

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}

//  لما البيانات تكون سليمة، لكن العملية نفسها مخالفة لقواعد المشروع؛ مثل محاولة أرشفة كورس له تسجيلات.
//409 Conflict