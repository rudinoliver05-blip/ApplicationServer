# JTCP

JTCP is a custom TCP-based application server built from scratch in Java.

The project is designed to explore the internal architecture of application
servers and gain hands-on experience with networking, protocol design,
concurrency, state management, resilience, and performance engineering.

Instead of relying on HTTP or an existing server framework, JTCP implements
its own application-level protocol directly on top of TCP.

---

## Architecture

```text
                         Client
                           │
                           │ TCP
                           ▼
                    ┌──────────────┐
                    │    Server    │
                    └──────┬───────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │  Request Parser │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │Protocol Validator│
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │Command Dispatcher│
                  └────────┬────────┘
                           │
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
     LoginHandler     SignupHandler     GetHandler
          │                │                │
          └────────────────┼────────────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │   Storage Layer     │
                │                     │
                │ UserStore           │
                │ DataStore           │
                │ SessionStore        │
                └─────────────────────┘
