# React.js Frontend Example

This example shows how to integrate the User List API with a React.js frontend to display large lists efficiently.

## Key Features Demonstrated

1. **Infinite Scroll**: Load more users as the user scrolls
2. **Alphabet Navigation**: Quick jump to users starting with specific letters
3. **Search Functionality**: Real-time search through the user list
4. **Pagination**: Efficient loading of data in chunks

## React Component Example

```jsx
import React, { useState, useEffect, useCallback } from 'react';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [alphabetInfo, setAlphabetInfo] = useState([]);
  const [selectedLetter, setSelectedLetter] = useState(null);

  const API_BASE = 'http://localhost:8080/api/users';

  // Load alphabet navigation data
  useEffect(() => {
    fetch(`${API_BASE}/alphabet`)
      .then(res => res.json())
      .then(data => setAlphabetInfo(data))
      .catch(console.error);
  }, []);

  // Load users based on current state
  const loadUsers = useCallback(async (pageNum = 0, reset = false) => {
    if (loading) return;
    
    setLoading(true);
    try {
      let url;
      if (selectedLetter) {
        url = `${API_BASE}/letter/${selectedLetter}?page=${pageNum}&size=50`;
      } else if (searchQuery) {
        url = `${API_BASE}/search?q=${encodeURIComponent(searchQuery)}&page=${pageNum}&size=50`;
      } else {
        url = `${API_BASE}?page=${pageNum}&size=50`;
      }

      const response = await fetch(url);
      const data = await response.json();
      
      if (reset) {
        setUsers(data.users);
      } else {
        setUsers(prev => [...prev, ...data.users]);
      }
      
      setHasMore(data.hasNext);
      setPage(pageNum);
    } catch (error) {
      console.error('Error loading users:', error);
    } finally {
      setLoading(false);
    }
  }, [loading, selectedLetter, searchQuery]);

  // Initial load
  useEffect(() => {
    loadUsers(0, true);
  }, [selectedLetter, searchQuery]);

  // Infinite scroll handler
  const handleScroll = useCallback(() => {
    if (window.innerHeight + document.documentElement.scrollTop 
        >= document.documentElement.offsetHeight - 1000) {
      if (hasMore && !loading) {
        loadUsers(page + 1, false);
      }
    }
  }, [hasMore, loading, page, loadUsers]);

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [handleScroll]);

  // Handle alphabet navigation
  const handleLetterClick = (letter) => {
    setSelectedLetter(letter);
    setSearchQuery('');
    setPage(0);
  };

  // Handle search
  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
    setSelectedLetter(null);
    setPage(0);
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <h1>User List (630K+ Users)</h1>
      
      {/* Search Bar */}
      <div style={{ marginBottom: '20px' }}>
        <input
          type="text"
          placeholder="Search users..."
          value={searchQuery}
          onChange={handleSearch}
          style={{
            padding: '10px',
            fontSize: '16px',
            width: '300px',
            border: '1px solid #ccc',
            borderRadius: '4px'
          }}
        />
      </div>

      {/* Alphabet Navigation */}
      <div style={{ marginBottom: '20px' }}>
        <h3>Quick Navigation:</h3>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '5px' }}>
          {alphabetInfo.map(info => (
            <button
              key={info.letter}
              onClick={() => handleLetterClick(info.letter)}
              style={{
                padding: '8px 12px',
                border: '1px solid #007bff',
                backgroundColor: selectedLetter === info.letter ? '#007bff' : 'white',
                color: selectedLetter === info.letter ? 'white' : '#007bff',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '14px'
              }}
            >
              {info.letter} ({info.count})
            </button>
          ))}
        </div>
      </div>

      {/* User List */}
      <div>
        <h3>
          {selectedLetter ? `Users starting with '${selectedLetter}'` : 
           searchQuery ? `Search results for '${searchQuery}'` : 
           'All Users'}
        </h3>
        
        <div style={{ maxHeight: '600px', overflowY: 'auto', border: '1px solid #ccc' }}>
          {users.map((user, index) => (
            <div
              key={`${user.index}-${index}`}
              style={{
                padding: '10px',
                borderBottom: '1px solid #eee',
                backgroundColor: index % 2 === 0 ? '#f9f9f9' : 'white'
              }}
            >
              <strong>#{user.index + 1}</strong> {user.name}
            </div>
          ))}
          
          {loading && (
            <div style={{ padding: '20px', textAlign: 'center' }}>
              Loading more users...
            </div>
          )}
          
          {!hasMore && users.length > 0 && (
            <div style={{ padding: '20px', textAlign: 'center', color: '#666' }}>
              No more users to load
            </div>
          )}
        </div>
      </div>

      {/* Stats */}
      <div style={{ marginTop: '20px', fontSize: '14px', color: '#666' }}>
        Loaded: {users.length} users | 
        {selectedLetter && ` Letter: ${selectedLetter}`}
        {searchQuery && ` Search: "${searchQuery}"`}
      </div>
    </div>
  );
};

export default UserList;
```

## Key Implementation Details

### 1. Infinite Scroll
- Uses `useEffect` with scroll event listener
- Loads more data when user scrolls near bottom
- Prevents duplicate requests with loading state

### 2. Alphabet Navigation
- Fetches alphabet info from `/api/users/alphabet`
- Shows count for each letter
- Allows quick navigation to specific letter sections

### 3. Search Functionality
- Real-time search with debouncing (can be added)
- Clears alphabet selection when searching
- Uses the search endpoint for efficient filtering

### 4. Performance Optimizations
- Only loads 50 users at a time
- Uses `useCallback` to prevent unnecessary re-renders
- Efficient state management to avoid memory leaks

### 5. Error Handling
- Try-catch blocks for API calls
- Loading states for better UX
- Graceful handling of empty results

## Usage Instructions

1. Start the Spring Boot API: `./mvnw spring-boot:run`
2. Create a React app: `npx create-react-app userlist-frontend`
3. Replace the default App.js with the UserList component above
4. Install any additional dependencies if needed
5. Run the React app: `npm start`

## Additional Features You Can Add

1. **Debounced Search**: Add a delay to search requests
2. **Virtual Scrolling**: For even better performance with very large lists
3. **Caching**: Cache API responses to reduce server load
4. **Loading Skeletons**: Better loading indicators
5. **Error Boundaries**: Handle component errors gracefully
6. **Responsive Design**: Mobile-friendly layout
7. **Keyboard Navigation**: Arrow keys for navigation
8. **URL State**: Sync search/letter selection with URL

This implementation efficiently handles the 630K+ user dataset without freezing the browser, providing a smooth user experience with infinite scroll and alphabet navigation.
