# User List API Documentation

This API provides efficient access to a large dataset of usernames (630,643+ users) with pagination, search, and alphabet navigation capabilities.

## Base URL
```
http://localhost:8080/api/users
```

## Endpoints

### 1. Get All Users (Paginated)
**GET** `/api/users`

**Parameters:**
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 50, max: 1000)

**Example:**
```bash
curl "http://localhost:8080/api/users?page=0&size=10"
```

**Response:**
```json
{
  "users": [
    {
      "name": "Aaron Abbott",
      "index": 0
    },
    {
      "name": "Aaron Abernathy", 
      "index": 1
    }
  ],
  "totalCount": 630643,
  "page": 0,
  "pageSize": 10,
  "totalPages": 63065,
  "hasNext": true,
  "hasPrevious": false
}
```

### 2. Get Users by Letter
**GET** `/api/users/letter/{letter}`

**Parameters:**
- `letter`: Alphabet letter (A-Z)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 50, max: 1000)

**Example:**
```bash
curl "http://localhost:8080/api/users/letter/A?page=0&size=10"
```

### 3. Search Users
**GET** `/api/users/search`

**Parameters:**
- `q`: Search query (required)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 50, max: 1000)

**Example:**
```bash
curl "http://localhost:8080/api/users/search?q=john&page=0&size=10"
```

### 4. Get Alphabet Navigation Info
**GET** `/api/users/alphabet`

**Example:**
```bash
curl "http://localhost:8080/api/users/alphabet"
```

**Response:**
```json
{
  "alphabetInfo": [
    {
      "letter": "A",
      "count": 36580,
      "startIndex": 0,
      "endIndex": 36579
    },
    {
      "letter": "B", 
      "count": 25684,
      "startIndex": 36580,
      "endIndex": 62263
    }
  ],
  "totalLetters": 23,
  "totalCount": 630643
}
```

### 5. Get Specific Letter Info
**GET** `/api/users/alphabet/{letter}`

**Example:**
```bash
curl "http://localhost:8080/api/users/alphabet/A"
```

### 6. Get Total User Count
**GET** `/api/users/count`

**Example:**
```bash
curl "http://localhost:8080/api/users/count"
```

**Response:**
```json
630643
```

### 7. Health Check
**GET** `/api/users/health`

**Example:**
```bash
curl "http://localhost:8080/api/users/health"
```

## Performance Features

1. **Efficient File Loading**: The usernames are loaded once at startup and kept in memory for fast access
2. **Alphabet Index**: Pre-built index for each letter to enable fast navigation
3. **Pagination**: Configurable page sizes with reasonable limits
4. **Memory Optimization**: Only loads the requested page of data, not the entire dataset

## Frontend Integration

This API is designed to work with React.js frontend applications that need to display large lists efficiently. The alphabet navigation endpoint provides the data needed to build a menu similar to the example provided.

## CORS Support

The API includes CORS headers to allow frontend integration from different origins.

## Error Handling

- Invalid page numbers default to 0
- Page sizes are capped at 1000 for performance
- Empty search queries return all users
- Non-existent letters return empty results

## Startup Time

The application may take a few moments to start as it loads and indexes the large usernames file. This is a one-time cost for optimal runtime performance.




