package com.zeylin.runoncoffee.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Record Not Found")
public class NotFoundException extends RuntimeException {
}
