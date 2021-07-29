package com.api.challenge.service;

@FunctionalInterface
public interface CheckedMethod<T,R> {
    R apply(T t) throws Exception;
}
