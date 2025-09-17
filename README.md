## User List API – Technical Challenge Summary

### Problem
Efficiently expose a very large, alphabetically sorted username list (630K+, target 10M) via a web API and render it in a frontend without freezing the browser.

### Approach
- Load and index the data once at application startup.
- Serve requests from memory with O(1) alphabet navigation and paginated endpoints.
- Enforce client-friendly page-size limits to protect browser UX and backend stability.

## Journey and Key Decisions

### 1) Baseline “get all” to validate feasibility
- Implemented a “get all users” endpoint first to measure backend response time and verify data integrity.
- Observed that backend can return large payloads quickly, but this is impractical for browsers (memory/DOM constraints).
- Decision: Keep backend fast, move UX protection to client-facing pagination and caps.

### 2) Data loading and in-memory model
- At startup (@PostConstruct), read `usernames.txt` and store in a `List<String>` for fast sequential access.
- Build an `alphabetIndex` (`Map<Character, AlphabetInfo>`) that stores, for each letter, the start and end indices and count.
- Result:
  - Initial one-time load + O(n) index build at boot.
  - Subsequent requests are O(1) for letter range lookup and O(pageSize) for slicing.

### 3) API design focused on frontend performance
- Main endpoints are strictly paginated to prevent browser freezes:
  - Pagination enforced client-side and backed by a server-side limit (max 1000 per request).
  - Alphabet navigation supported via precomputed index for instant jumps.
- A “get all” response DTO is provided for non-browser use (e.g., internal tools), but not recommended for UI.

## Main API Endpoints (Production-Ready)

### 1. Get All Users (Paginated)
- Method: GET
- Path: `/api/users`
- Query:
  - `page` (default: 0)
  - `size` (default: 50, max: 1000)
- Example:
```bash
curl "http://localhost:8080/api/users?page=0&size=10"
```
- Notes:
  - Returns a DTO with users and pagination metadata (totalCount, totalPages, hasNext, etc.).
  - Server enforces `size <= 1000` to protect client and server resources.

### 2. Get Users by Letter (Paginated)
- Method: GET
- Path: `/api/users/letter/{letter}`
- Query:
  - `page` (default: 0)
  - `size` (default: 50, max: 1000)
- Example:
```bash
curl "http://localhost:8080/api/users/letter/A?page=0&size=10"
```
- Notes:
  - Uses the precomputed alphabet index to find the exact slice for the letter instantly.

### Optional/Utility Endpoints
- `GET /api/users/alphabet`: Returns alphabet navigation info (counts, start/end indices) with totalCount.
- `GET /api/users/count`: Returns total user count.
- `GET /api/users/all`: Returns all users with total count (DTO). Intended for server-to-server use; not recommended for browsers.

## Why Pagination and Max Page Size?
- Browsers have limited memory and DOM-rendering capacity. Rendering thousands of elements can freeze the UI.
- The backend also needs predictable memory/CPU/network usage under concurrency.
- Enforcing `size <= 1000` gives a good balance between throughput and UX, and it integrates well with infinite scroll on the frontend.

## Performance Highlights
- Single read of `usernames.txt` at startup; no file I/O per request.
- O(1) alphabet navigation via precomputed `alphabetIndex`.
- O(pageSize) list slicing to build responses.
- In-memory caching yields sub-millisecond lookups and fast responses.

## Architecture Overview
- `config/`: Cross-cutting config (e.g., CORS).
- `controllers/`: REST controllers (request mapping and DTO wiring).
- `dto/`: Request/response DTOs with basic validation and structure.
- `models/`: Domain models (`User`, `AlphabetInfo`).
- `services/`: Business logic (loading file, indexing, pagination, search).
- `resources/`: `usernames.txt` and `application.yml`.

## CORS and Frontend Integration
- Global CORS config added to serve different frontend origins and support OPTIONS preflight.
- If the frontend is served via HTTPS, the API should also be HTTPS (or proxied via same origin) to avoid mixed-content blocking.

## Limits and Trade-offs
- Max page size is capped to 1000 by design to protect both client and server.
- “Get all” exists for completeness but is not intended for browser consumption.
- For 10M+ users, the same design works with enough RAM; otherwise migrate storage/indexing to a database or search engine and keep the same API semantics.


## How to Run
- Start API:
```bash
./mvnw spring-boot:run
```
- Example calls:
```bash
curl "http://localhost:8080/api/users?page=0&size=50"
curl "http://localhost:8080/api/users/letter/M?page=0&size=50"
curl "http://localhost:8080/api/users/alphabet"
```

## Summary
- Data is loaded and indexed once at startup.
- All API responses are served from memory with fast, predictable performance.
- Pagination is first-class to ensure smooth frontend rendering and robust backend throughput.
- The structure follows conventional Spring practices, keeping concerns cleanly separated and easy to extend.


