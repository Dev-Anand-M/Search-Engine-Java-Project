<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Results - Java Search Engine</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #fff;
        }
        .header {
            padding: 20px;
            border-bottom: 1px solid #ebebeb;
        }
        .search-bar {
            display: flex;
            align-items: center;
            max-width: 600px;
        }
        .logo {
            color: #4285f4;
            font-size: 1.5rem;
            margin-right: 30px;
            text-decoration: none;
        }
        .search-input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 25px;
            font-size: 16px;
            margin-right: 10px;
        }
        .search-button {
            background: #4285f4;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }
        .results-info {
            padding: 20px;
            color: #666;
            font-size: 14px;
        }
        .results {
            max-width: 600px;
            padding: 0 20px;
        }
        .result-item {
            margin-bottom: 30px;
        }
        .result-title {
            font-size: 18px;
            margin-bottom: 5px;
        }
        .result-title a {
            color: #1a0dab;
            text-decoration: none;
        }
        .result-title a:hover {
            text-decoration: underline;
        }
        .result-url {
            color: #006621;
            font-size: 14px;
            margin-bottom: 5px;
        }
        .result-snippet {
            color: #545454;
            font-size: 14px;
            line-height: 1.4;
        }
        .no-results {
            padding: 20px;
            text-align: center;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="search-bar">
            <a href="index.jsp" class="logo">Java Search Engine</a>
            <form action="search" method="get" style="display: flex; flex: 1;">
                <input type="text" name="query" value="${query}" class="search-input">
                <button type="submit" class="search-button">Search</button>
            </form>
        </div>
    </div>

    <div class="results-info">
        <c:choose>
            <c:when test="${hasResults}">
                About ${totalResults} results for "<strong>${query}</strong>"
            </c:when>
            <c:otherwise>
                No results found for "<strong>${query}</strong>"
            </c:otherwise>
        </c:choose>
    </div>

    <div class="results">
        <c:choose>
            <c:when test="${hasResults}">
                <c:forEach var="result" items="${results}">
                    <div class="result-item">
                        <div class="result-title">
                            <a href="${result.url}" target="_blank">${result.title}</a>
                        </div>
                        <div class="result-url">${result.url}</div>
                        <div class="result-snippet">${result.generateSnippet(query)}</div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-results">
                    <p>Try different keywords or check your spelling.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>