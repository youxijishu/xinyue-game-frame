package com.xinyue.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Retention(RetentionPolicy.RUNTIME)
@Service
@Scope(scopeName="prototype")
public @interface PrototypeService {

}
