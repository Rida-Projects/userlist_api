# Architecture and Design Patterns Diagram

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐            │
│  │   React.js  │  │   Mobile    │  │   Web App   │            │
│  │  Frontend   │  │     App     │  │             │            │
│  └─────────────┘  └─────────────┘  └─────────────┘            │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ HTTP/REST API
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              UserController                             │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐      │   │
│  │  │   GET       │ │   GET       │ │   GET       │      │   │
│  │  │  /users     │ │ /letter/{A} │ │ /search     │      │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘      │   │
│  │                                                       │   │
│  │  Pattern: Facade Pattern                              │   │
│  │  Purpose: Simplified interface to complex operations │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ DTOs
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      DTO LAYER                                 │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐              │
│  │UserRequest  │ │UserResponse │ │SearchRequest│              │
│  │    DTO      │ │    DTO      │ │    DTO      │              │
│  └─────────────┘ └─────────────┘ └─────────────┘              │
│                                                               │
│  Pattern: Data Transfer Object (DTO)                         │
│  Purpose: Encapsulate data transfer between layers           │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ Service Calls
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                              │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                UserService                              │   │
│  │                                                       │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐      │   │
│  │  │   getUsers  │ │getUsersBy   │ │ searchUsers │      │   │
│  │  │             │ │   Letter    │ │             │      │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘      │   │
│  │                                                       │   │
│  │  Pattern: Service Layer Pattern                       │   │
│  │  Purpose: Encapsulate business logic                  │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ Data Access
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     DATA LAYER                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              In-Memory Data Store                      │   │
│  │                                                       │   │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐      │   │
│  │  │   List      │ │    Map      │ │   Index     │      │   │
│  │  │allUsernames │ │alphabetIndex│ │  Building   │      │   │
│  │  │ (630K items)│ │ (O(1) lookup│ │  Algorithm  │      │   │
│  │  └─────────────┘ └─────────────┘ └─────────────┘      │   │
│  │                                                       │   │
│  │  Pattern: Repository Pattern (Implicit)               │   │
│  │  Purpose: Abstract data access logic                  │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ File I/O
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    PERSISTENCE LAYER                           │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                usernames.txt                            │   │
│  │                                                       │   │
│  │  • 630,643 usernames                                  │   │
│  │  • Sorted alphabetically                              │   │
│  │  • Loaded once at startup                             │   │
│  │                                                       │   │
│  │  Pattern: File-based Repository                       │   │
│  │  Purpose: Data persistence and initial loading        │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Design Patterns Implementation

### 1. **Data Transfer Object (DTO) Pattern**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controller    │───▶│      DTO        │───▶│    Service      │
│                 │    │                 │    │                 │
│ • HTTP params   │    │ • Validation    │    │ • Business      │
│ • Request data  │    │ • Type safety   │    │   logic         │
│                 │    │ • Encapsulation │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 2. **Service Layer Pattern**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controller    │───▶│    Service      │───▶│   Repository    │
│                 │    │                 │    │                 │
│ • HTTP handling │    │ • Business      │    │ • Data access   │
│ • Request/Resp  │    │   logic         │    │ • Caching       │
│ • Validation    │    │ • Orchestration │    │ • Indexing      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 3. **Singleton Pattern (Spring Managed)**
```
┌─────────────────────────────────────────────────────────────┐
│                Spring Application Context                   │
│                                                             │
│  ┌─────────────────┐                                        │
│  │   UserService   │ ◄─── Single Instance                  │
│  │                 │                                        │
│  │ • Shared state  │                                        │
│  │ • Thread-safe   │                                        │
│  │ • Memory cache  │                                        │
│  └─────────────────┘                                        │
└─────────────────────────────────────────────────────────────┘
```

### 4. **Strategy Pattern**
```
┌─────────────────┐
│   UserService   │
│                 │
│ ┌─────────────┐ │    ┌─────────────────┐
│ │ getUsers()  │ │───▶│ Full List       │
│ └─────────────┘ │    │ Strategy        │
│                 │    └─────────────────┘
│ ┌─────────────┐ │
│ │getUsersBy   │ │───▶┌─────────────────┐
│ │Letter()     │ │    │ Alphabet-based  │
│ └─────────────┘ │    │ Strategy        │
│                 │    └─────────────────┘
│ ┌─────────────┐ │
│ │searchUsers()│ │───▶┌─────────────────┐
│ └─────────────┘ │    │ Search-based    │
│                 │    │ Strategy        │
└─────────────────┘    └─────────────────┘
```

## Performance Optimization Layers

### 1. **Memory Caching Strategy**
```
┌─────────────────────────────────────────────────────────────┐
│                    Memory Optimization                      │
│                                                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Startup   │  │   Runtime   │  │   Cleanup   │        │
│  │             │  │             │  │             │        │
│  │ • Load file │  │ • Serve from│  │ • Auto GC   │        │
│  │ • Build idx │  │   memory    │  │ • Resource  │        │
│  │ • Cache all │  │ • O(1) lookup│  │   cleanup   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

### 2. **Indexing Strategy**
```
┌─────────────────────────────────────────────────────────────┐
│                    Indexing Optimization                    │
│                                                             │
│  File: usernames.txt (630K records)                        │
│  │                                                         │
│  ▼                                                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Alphabet Index                         │   │
│  │                                                     │   │
│  │  A: [0, 36579]     - 36,580 users                  │   │
│  │  B: [36580, 62263] - 25,684 users                  │   │
│  │  C: [62264, 100363] - 38,100 users                 │   │
│  │  ...                                               │   │
│  │  Z: [630136, 630642] - 507 users                   │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  Benefits: O(1) alphabet navigation                        │
└─────────────────────────────────────────────────────────────┘
```

### 3. **Pagination Strategy**
```
┌─────────────────────────────────────────────────────────────┐
│                   Pagination Optimization                   │
│                                                             │
│  Request: page=5, size=50                                   │
│  │                                                         │
│  ▼                                                         │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Pagination Logic                       │   │
│  │                                                     │   │
│  │  startIndex = 5 * 50 = 250                          │   │
│  │  endIndex = min(250 + 50, 630643) = 300             │   │
│  │                                                     │   │
│  │  Only process records 250-300                       │   │
│  │  (Not all 630K records)                             │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  Benefits: Constant memory usage per request               │
└─────────────────────────────────────────────────────────────┘
```

## Performance Metrics

### **Response Time Analysis**
```
┌─────────────────────────────────────────────────────────────┐
│                    Response Times                           │
│                                                             │
│  Alphabet Navigation: < 1ms    (O(1) lookup)              │
│  Pagination (50 items): < 5ms  (O(pageSize))              │
│  Search (full scan): 100-500ms (O(n))                     │
│  Health Check: < 1ms          (Simple response)           │
└─────────────────────────────────────────────────────────────┘
```

### **Memory Usage Analysis**
```
┌─────────────────────────────────────────────────────────────┐
│                    Memory Usage                             │
│                                                             │
│  Usernames List: ~50MB     (630K strings)                  │
│  Alphabet Index: ~1MB      (26 entries)                    │
│  JVM Overhead: ~20-30MB    (Spring Boot)                   │
│  Total: ~80-100MB          (Peak usage)                    │
└─────────────────────────────────────────────────────────────┘
```

### **Scalability Analysis**
```
┌─────────────────────────────────────────────────────────────┐
│                    Scalability Limits                      │
│                                                             │
│  Current: 630K users        ✅ Optimized                   │
│  1M users: ~150MB memory    ✅ Feasible                    │
│  5M users: ~750MB memory    ⚠️  Requires more RAM          │
│  10M users: ~1.5GB memory   ❌ Consider database           │
└─────────────────────────────────────────────────────────────┘
```

This architecture demonstrates how proper design patterns and performance optimizations work together to create a scalable, maintainable, and efficient system for handling large datasets.
