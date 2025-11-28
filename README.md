# Java Search Engine 🔍

A modern, full-featured web search engine built with Java, featuring a beautiful animated UI and intelligent web crawling.

## Features

- 🎨 **Modern UI** with animated geometric backgrounds
- 🕷️ **Smart Web Crawler** - crawls 1000+ pages with rate limiting
- 🔍 **Full-text Search** with MySQL FULLTEXT indexing
- 📜 **Search History** tracking
- 📄 **Pagination** for search results
- 🎯 **Snippet Generation** with keyword highlighting

## Tech Stack

- **Backend**: Java 11, Servlets, JSP
- **Database**: MySQL with FULLTEXT search
- **Crawler**: JSoup for HTML parsing
- **Server**: Embedded Tomcat
- **Frontend**: CSS3 animations, responsive design

## Deployment on Railway

### Quick Deploy

1. Fork this repository
2. Create a new project on [Railway](https://railway.app)
3. Add MySQL database
4. Deploy from GitHub
5. Set environment variable: `ENABLE_CRAWLER=true`

### Environment Variables

- `ENABLE_CRAWLER` - Set to `true` to enable automatic crawling every 24 hours
- Railway auto-provides: `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`

## Local Development

### Prerequisites

- Java 11+
- Maven
- MySQL

### Setup

1. Clone the repository
2. Create MySQL database: `searchengine_db`
3. Update credentials in `DatabaseConnection.java` if needed
4. Run crawler to populate data:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.searchengine.crawler.WebCrawler"
   ```
5. Start the server:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.searchengine.Main"
   ```
6. Visit `http://localhost:8080`

## Crawler Configuration

The crawler is configured to:
- Crawl up to **1000 pages** per session
- Wait **2 seconds** between requests (polite crawling)
- Maximum depth of **3 levels**
- Crawl from quality sources: Wikipedia, Britannica, BBC, Reuters, Nature, etc.

## License

MIT License
