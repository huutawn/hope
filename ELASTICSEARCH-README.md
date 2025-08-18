# Elasticsearch Search Integration

This document describes the Elasticsearch search functionality integrated into the Hope platform.

## Overview

The Elasticsearch integration provides powerful search capabilities for:
- **Jobs**: Search by keyword, location, salary range, company, category, and disability suitability
- **Posts**: Search by keyword, type, user, title, and content
- **Volunteer Posts**: Search by keyword, location, status, fund range, and bank information

All search results are returned using the same response DTOs (`JobResponse`, `PostResponse`, `PostVolunteerResponse`) wrapped in `PageResponse` objects, maintaining consistency with the existing API.

## Prerequisites

1. **Elasticsearch Server**: Make sure Elasticsearch is running and accessible
2. **Environment Variables**: Set the following in your `.env` file:
   ```
   ELASTIC_PASSWORD=your_elasticsearch_password
   ```

## Configuration

The Elasticsearch configuration is already set up in `application.yaml`:

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
    username: elastic
    password: ${ELASTIC_PASSWORD}
```

## API Endpoints

### Search Jobs

| Endpoint | Method | Description | Parameters |
|----------|--------|-------------|------------|
| `/api/search/jobs` | GET | Search jobs by keyword | `keyword`, `page`, `size` |
| `/api/search/jobs/location` | GET | Search jobs by location | `location`, `page`, `size` |
| `/api/search/jobs/category/{categoryId}` | GET | Search jobs by category | `categoryId`, `page`, `size` |
| `/api/search/jobs/company/{companyId}` | GET | Search jobs by company | `companyId`, `page`, `size` |
| `/api/search/jobs/salary` | GET | Search jobs by salary range | `minSalary`, `maxSalary`, `page`, `size` |
| `/api/search/jobs/disability` | GET | Search jobs by disability type | `disabilityType`, `page`, `size` |
| `/api/search/jobs/type` | GET | Search jobs by job type | `jobType`, `page`, `size` |
| `/api/search/jobs/active` | GET | Get all active jobs | `page`, `size` |
| `/api/search/jobs/employer/{employerId}` | GET | Get jobs by employer | `employerId`, `page`, `size` |

### Search Posts

| Endpoint | Method | Description | Parameters |
|----------|--------|-------------|------------|
| `/api/search/posts` | GET | Search posts by keyword | `keyword`, `page`, `size` |
| `/api/search/posts/type` | GET | Search posts by type | `type`, `page`, `size` |
| `/api/search/posts/user/{userId}` | GET | Search posts by user | `userId`, `page`, `size` |
| `/api/search/posts/username` | GET | Search posts by username | `userName`, `page`, `size` |
| `/api/search/posts/active` | GET | Get all active posts | `page`, `size` |
| `/api/search/posts/pinned` | GET | Get pinned posts | `page`, `size` |
| `/api/search/posts/title` | GET | Search posts by title | `title`, `page`, `size` |
| `/api/search/posts/content` | GET | Search posts by content | `content`, `page`, `size` |

### Search Volunteer Posts

| Endpoint | Method | Description | Parameters |
|----------|--------|-------------|------------|
| `/api/search/volunteers` | GET | Search volunteer posts by keyword | `keyword`, `page`, `size` |
| `/api/search/volunteers/location` | GET | Search volunteer posts by location | `location`, `page`, `size` |
| `/api/search/volunteers/status` | GET | Search volunteer posts by status | `status`, `page`, `size` |
| `/api/search/volunteers/user/{userId}` | GET | Search volunteer posts by user | `userId`, `page`, `size` |
| `/api/search/volunteers/username` | GET | Search volunteer posts by username | `userName`, `page`, `size` |
| `/api/search/volunteers/active` | GET | Get all active volunteer posts | `page`, `size` |
| `/api/search/volunteers/fund` | GET | Search by fund range | `minFund`, `maxFund`, `page`, `size` |
| `/api/search/volunteers/amount` | GET | Search by total amount range | `minAmount`, `maxAmount`, `page`, `size` |
| `/api/search/volunteers/title` | GET | Search volunteer posts by title | `title`, `page`, `size` |
| `/api/search/volunteers/content` | GET | Search volunteer posts by content | `content`, `page`, `size` |
| `/api/search/volunteers/bank` | GET | Search volunteer posts by bank name | `bankName`, `page`, `size` |

### Admin Endpoints (Requires ADMIN role)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/admin/elasticsearch/index-all` | POST | Index all entities |
| `/api/admin/elasticsearch/index-jobs` | POST | Index all jobs |
| `/api/admin/elasticsearch/index-posts` | POST | Index all posts |
| `/api/admin/elasticsearch/index-volunteers` | POST | Index all volunteer posts |
| `/api/admin/elasticsearch/job/{jobId}` | DELETE | Delete job from index |
| `/api/admin/elasticsearch/post/{postId}` | DELETE | Delete post from index |
| `/api/admin/elasticsearch/volunteer/{postVolunteerId}` | DELETE | Delete volunteer post from index |

## Usage Examples

### 1. Search Jobs by Keyword
```bash
curl -X GET "http://localhost:8080/api/search/jobs?keyword=developer&page=0&size=10"
```

### 2. Search Jobs by Location and Salary Range
```bash
curl -X GET "http://localhost:8080/api/search/jobs/location?location=Ho Chi Minh&page=0&size=10"
curl -X GET "http://localhost:8080/api/search/jobs/salary?minSalary=1000&maxSalary=5000&page=0&size=10"
```

### 3. Search Posts by Keyword
```bash
curl -X GET "http://localhost:8080/api/search/posts?keyword=charity&page=0&size=10"
```

### 4. Search Volunteer Posts by Location
```bash
curl -X GET "http://localhost:8080/api/search/volunteers/location?location=Hanoi&page=0&size=10"
```

### 5. Index All Entities (Admin only)
```bash
curl -X POST "http://localhost:8080/api/admin/elasticsearch/index-all" \
  -H "Authorization: Bearer {your_admin_token}"
```

## Response Format

All search endpoints return responses in the following format:

```json
{
  "message": "Jobs searched successfully",
  "result": {
    "currentPage": 0,
    "totalPages": 5,
    "pageSize": 10,
    "totalElements": 50,
    "data": [
      {
        "id": 1,
        "title": "Software Developer",
        "description": "Looking for a skilled developer...",
        // ... other fields based on the entity type
      }
    ]
  }
}
```

## Search Features

### 1. Multi-field Search
- Jobs: Searches across title, description, company name, location, requirements, responsibilities, and benefits
- Posts: Searches across title, content, and username
- Volunteer Posts: Searches across title, content, location, username, and bank name

### 2. Field Boosting
Search results are weighted by importance:
- Title fields have the highest boost (^3)
- Description/content fields have medium boost (^2)
- Other fields have normal weight

### 3. Filtering
All searches automatically filter for active records and apply additional filters based on entity type.

## Indexing Strategy

### Automatic Indexing
The system automatically indexes entities when they are:
- Created (new entities are automatically indexed)
- Updated (existing entities are re-indexed)
- Deleted (entities are removed from the index)

### Manual Indexing
Use the admin endpoints to manually re-index all data or specific entity types when needed.

## Architecture

### Components
1. **Document Entities**: `JobDocument`, `PostDocument`, `PostVolunteerDocument`
2. **Repositories**: Elasticsearch repositories for each document type
3. **Services**: 
   - Individual services for each entity type
   - `UnifiedSearchService` for converting to standard DTOs
   - `ElasticsearchIndexingService` for automatic indexing
4. **Controllers**: 
   - `SearchController` for public search endpoints
   - `ElasticsearchAdminController` for admin operations

### Data Flow
1. Search request → `SearchController`
2. Controller → `UnifiedSearchService`
3. Service → Entity-specific Elasticsearch service
4. Elasticsearch service → Elasticsearch repository
5. Repository queries Elasticsearch
6. Results converted back to standard DTOs using existing mappers
7. Response returned as `PageResponse<EntityResponse>`

## Performance Considerations

1. **Async Indexing**: All indexing operations are performed asynchronously to avoid blocking main application threads
2. **Efficient Queries**: Uses Elasticsearch's native query capabilities for fast search
3. **Pagination**: All results are paginated to handle large datasets efficiently
4. **Caching**: Consider adding Redis caching for frequently accessed search results

## Troubleshooting

### Common Issues

1. **Elasticsearch Connection Error**
   - Verify Elasticsearch is running on the configured URL
   - Check credentials in environment variables

2. **Index Not Found Error**
   - Run the index-all admin endpoint to create initial indices

3. **Search Returns No Results**
   - Verify data has been indexed using admin endpoints
   - Check if entities are marked as active

4. **Slow Search Performance**
   - Consider adding more Elasticsearch nodes
   - Optimize query structure
   - Add caching layer

## Future Enhancements

1. **Advanced Filters**: Add more sophisticated filtering options
2. **Faceted Search**: Implement faceted search for better user experience
3. **Auto-suggest**: Add search suggestions and auto-completion
4. **Analytics**: Track search patterns and popular queries
5. **Synonyms**: Add synonym support for better search matching
6. **Geo-location**: Add location-based proximity search

## Monitoring

Consider adding monitoring for:
- Search response times
- Index sizes
- Query patterns
- Error rates
- Elasticsearch cluster health
