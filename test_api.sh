#!/bin/bash

echo "Testing User List API..."
echo "========================"

BASE_URL="http://localhost:8080/api/users"

echo "1. Testing health endpoint..."
curl -s "$BASE_URL/health" | head -1
echo ""

echo "2. Testing user count..."
curl -s "$BASE_URL/count"
echo ""

echo "3. Testing first page of users..."
curl -s "$BASE_URL?page=0&size=5" | head -10
echo ""

echo "4. Testing alphabet info..."
curl -s "$BASE_URL/alphabet" | head -10
echo ""

echo "5. Testing users by letter A..."
curl -s "$BASE_URL/letter/A?page=0&size=3" | head -10
echo ""

echo "6. Testing search for 'Aaron'..."
curl -s "$BASE_URL/search?q=Aaron&page=0&size=3" | head -10
echo ""

echo "API testing completed!"
