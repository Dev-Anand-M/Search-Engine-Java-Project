# Java Search Engine - Setup Instructions

## Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- IntelliJ IDEA
- Apache Tomcat 9.0

## Step 1: Setup MySQL Database

1. Install MySQL and start the service
2. Connect to MySQL:
```bash
mysql -u root -p
```

3. Create database:
```sql
CREATE DATABASE searchengine_db;
USE searchengine_db;
```

4. Update credentials in `DatabaseConnection.java`:
```java
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password_here";
```

## Step 2: IntelliJ IDEA Setup

1. Open IntelliJ IDEA
2. File → Open → Select the project folder
3. Wait for Maven to download dependencies
4. Install Smart Tomcat plugin:
   - File → Settings → Plugins → Search "Smart Tomcat" → Install

## Step 3: Configure Tomcat

1. Download Apache Tomcat 9.0 from https://tomcat.apache.org/
2. Extract it to a folder (e.g., C:\apache-tomcat-9.0.xx)
3. In IntelliJ: Run → Edit Configurations → Add → Smart Tomcat
4. Configure:
   - Name: "Search Engine"
   - Tomcat Server: Browse to your Tomcat folder
   - Deployment Directory: Select `src/main/webapp`
   - Context Path: `/search-engine`

## Step 4: Build and Run

1. Build project:
```bash
mvn clean compile
```

2. Setup database tables:
   - Right-click on `DatabaseSetup.java` → Run
   - This creates the necessary tables

3. Start the server:
   - Click the green run button next to "Search Engine" configuration
   - Or: Run → Run 'Search Engine'

4. Open browser and go to:
```
http://localhost:8080/search-engine
```

## Step 5: Initialize with Data

### Option 1: Run Web Crawler
1. Right-click on `WebCrawler.java`
2. Add a main method:
```java
public static void main(String[] args) {
    WebCrawler crawler = new WebCrawler();
    crawler.startCrawling("https://www.javatpoint.com/java-tutorial");
}
```
3. Run the crawler to populate data

### Option 2: Manual Data Entry
Add sample pages directly to database:
```sql
INSERT INTO pages (url, title, content) VALUES 
('https://javatpoint.com/java-tutorial', 'Java Tutorial', 'Java is a programming language...'),
('https://javatpoint.com/servlet-tutorial', 'Servlet Tutorial', 'Servlet is used for web development...');
```

## Step 6: Test the Search Engine

1. Go to http://localhost:8080/search-engine
2. Search for terms like:
   - "java"
   - "servlet" 
   - "tutorial"
   - "programming"

3. Check search history by clicking "History"

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check username/password in DatabaseConnection.java
- Ensure database searchengine_db exists

### Tomcat Issues
- Verify Tomcat path is correct
- Check if port 8080 is available
- Look at IntelliJ console for errors

### Maven Issues
- Run: `mvn clean install`
- Check internet connection for dependencies

### No Search Results
- Run DatabaseSetup.java to create tables
- Add sample data or run the web crawler
- Check database has pages in the `pages` table

## Project Structure
```
src/
├── main/
│   ├── java/com/searchengine/
│   │   ├── database/DatabaseConnection.java
│   │   ├── model/WebPage.java
│   │   ├── crawler/WebCrawler.java
│   │   ├── search/SearchEngine.java
│   │   ├── servlet/SearchServlet.java
│   │   ├── servlet/HistoryServlet.java
│   │   └── util/DatabaseSetup.java
│   └── webapp/
│       ├── index.jsp
│       ├── results.jsp
│       ├── history.jsp
│       └── WEB-INF/web.xml
└── pom.xml
```

## Features
- ✅ Web crawling with JSoup
- ✅ MySQL database storage
- ✅ Full-text search
- ✅ Ranking by relevance
- ✅ Search history
- ✅ Clean web interface
- ✅ Top 30 results display

## Next Steps
- Add more sophisticated ranking (TF-IDF)
- Implement pagination
- Add search filters
- Improve UI design
- Add caching for performance
