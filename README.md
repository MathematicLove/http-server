# HTTP Server on Java

## Overview

A configurable HTTP server implemented in Java that supports core HTTP methods and provides a flexible handler registration system. The server can be deployed with either traditional thread pools or virtual threads (on Java 19+).

## Features

- **HTTP Methods**: Supports GET, POST, PUT, PATCH, DELETE.
- **Request Parsing**: Parses the request line, headers (available as a `Map`), and request body.
- **Response Building**: Allows setting the response status, status phrase, headers, and body.
- **Configurable Server**: Creates a server on a specified host and port.
- **Handler Registration**: API enables adding handlers (via the `HttpHandler` interface) for specific URL paths and HTTP methods.
- **Concurrency**: The server uses an `ExecutorService` with a choice between standard threads or virtual threads (when using Java 19+).

## Project Structure

- **org.httpserver.http.handler.HttpHandler**  
  Interface for implementing custom HTTP request handlers.  
  Each handler receives a parsed request (`HttpReqParser`) and a response object (`HttpRes`).

- **org.httpserver.http.util.HttpReqParser**  
  Class for parsing incoming HTTP requests. Extracts the method, path, protocol version, headers, and request body.

- **org.httpserver.http.HttpRes**  
  Class for constructing and sending HTTP responses to clients. Allows setting the status code, status phrase, headers, and body. Automatically calculates the `Content-Length` header when necessary.

- **org.httpserver.http.HttpServer**  
  The main server class, which uses `ServerSocketChannel` to listen for connections. It handles incoming connections, parses requests, and passes them to registered handlers.

- **org.httpserver.RunServer**  
  The application entry point. Demonstrates creating a server instance, registering handlers for various HTTP methods, and starting the server.

## Server Operation

1. **Initialization and Configuration**  
   During server creation, the host, port, thread count, and the type of `ExecutorService` (standard or virtual threads) are configured. Handlers are registered via the public API method `addListener(path, method, handler)`.

2. **Handling Incoming Connections**  
   The server uses non-blocking `ServerSocketChannel` to accept new connections. Upon establishing a connection, it switches the channel to blocking mode and delegates processing to the thread pool.

3. **Request Parsing**  
   In the `handleClient` method, the incoming request is parsed using the `HttpReqParser` class, which extracts the HTTP method, URL path, headers, and request body.

4. **Handler Invocation**  
   Based on the HTTP method and URL path, the server searches for a registered handler. If found, the handler is invoked to generate a response. Otherwise, a 404 response is returned.

5. **Response Building and Sending**  
   The `HttpRes` class is used to set the status, headers, and body of the response. Before sending, the `Content-Length` header is automatically added (if required). The complete response is then sent to the client.

## Testing Examples

- **Testing General Endpoints**:  
  Use commands like `curl -X -d "data" http://localhost:8080/linkN`, where 1 <= N <= 4.
  Example: `curl -X POST -d "sample data" http://localhost:8080/link2`

- **Testing DELETE Method**:  
  `curl -X DELETE http://localhost:8080/delete`

- **Testing Binary Files (e.g., images)**:  
  `curl http://localhost:8080/pic --output test.png && open test.png`
