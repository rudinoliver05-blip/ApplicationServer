# JTCP

JTCP is a custom application server built from scratch in Java to explore
networking, protocol design, server architecture, concurrency, and
high-performance systems.

Rather than relying on HTTP or an existing web framework, JTCP implements
its own application-level protocol on top of TCP.

## Current Architecture

Client
   │
   │ Custom TCP Protocol
   ▼
Server
   │
   ▼
Request Parser
   │
   ▼
Protocol Validation
   │
   ▼
Command Dispatcher
   │
   ├── SignupHandler
   ├── LoginHandler
   ├── LogoutHandler
   ├── WhoAmIHandler
   └── ...
   │
   ▼
In-Memory Storage / Session Store

## Current Features

- Custom TCP client/server communication
- Custom application-level protocol
- Protocol versioning
- Request parsing and validation
- Command-based request dispatching
- Separate command handlers
- In-memory user storage
- User signup and login
- Session creation using UUIDs
- Session-based logout
- Session storage and removal
- Protocol and application-level error handling

## Goals

JTCP is being developed incrementally rather than as a framework-driven
application. The goal is to understand what happens inside an application
server and eventually build a system capable of handling significant
concurrency and load.

Planned areas of development include:

- Concurrent client handling
- Thread-per-connection implementation
- Thread-pool based execution
- Request queues
- Java NIO
- Event-driven I/O
- Backpressure
- Connection limits
- Request timeouts
- Graceful shutdown
- Load shedding
- Rate limiting
- Metrics and observability
- Performance benchmarking
- High-concurrency load testing

The project will evolve through these stages:

Correctness → Concurrency → Resilience → Measurement → Performance

## Why This Project?

JTCP is primarily a systems-learning project. It is intended to provide
hands-on experience with the problems that exist underneath higher-level
application frameworks:

- How network connections are managed
- How protocols are designed and parsed
- How requests are dispatched
- How state is maintained
- How concurrent requests are handled
- How servers behave under load
- How architectural decisions affect performance

The long-term goal is to turn JTCP into a small, high-performance
application server while using benchmarks and experiments to evaluate
different architectural approaches.
