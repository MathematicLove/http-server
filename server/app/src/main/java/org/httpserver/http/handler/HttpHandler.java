package org.httpserver.http.handler;

import org.httpserver.http.HttpRes;
import org.httpserver.http.util.HttpReqParser;

import java.io.IOException;

public interface HttpHandler {
    void handle(HttpReqParser request, HttpRes response) throws IOException;
}
