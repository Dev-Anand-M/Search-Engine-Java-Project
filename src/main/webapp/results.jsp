<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results - Java Search Engine</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="results-page">
    <div class="header">
        <div class="search-bar">
            <a href="index.jsp" class="header-logo">Search</a>
            <form action="search" method="get" class="search-form">
                <input type="text" name="query" value="${query}" class="results-search-input">
                <button type="submit" class="results-search-button">Search</button>
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

    <!-- Pagination Controls (First, Prev, Numbered, Next, Last) -->
    <%
        int currentPage = request.getAttribute("currentPage") != null ? (Integer) request.getAttribute("currentPage") : 1;
        int pageSize = request.getAttribute("pageSize") != null ? (Integer) request.getAttribute("pageSize") : 10;
        int totalResults = request.getAttribute("totalResults") != null ? (Integer) request.getAttribute("totalResults") : 0;
        String queryStr = (String) request.getAttribute("query");
        int totalPages = (int) Math.ceil((double) totalResults / pageSize);

        int displayPages = 7; // How many numbered page links to show
        int startPage = Math.max(1, currentPage - displayPages / 2);
        int endPage = Math.min(totalPages, startPage + displayPages - 1);
        if(endPage - startPage < displayPages-1) {
            startPage = Math.max(1, endPage - displayPages + 1);
        }
    %>
    <div class="pagination">
        <% if (currentPage > 1) { %>
            <a href="search?query=<%= queryStr %>&page=1" title="First Page">&laquo; First</a>
            <a href="search?query=<%= queryStr %>&page=<%= currentPage - 1 %>">&lsaquo; Prev</a>
        <% } else { %>
            <span class="disabled">&laquo; First</span>
            <span class="disabled">&lsaquo; Prev</span>
        <% } %>

        <% if (startPage > 1) { %>
            <span>...</span>
        <% } %>

        <% for(int i = startPage; i <= endPage; i++) { %>
            <% if (i == currentPage) { %>
                <span class="current-page"><%= i %></span>
            <% } else { %>
                <a href="search?query=<%= queryStr %>&page=<%= i %>"><%= i %></a>
            <% } %>
        <% } %>

        <% if (endPage < totalPages) { %>
            <span>...</span>
        <% } %>

        <% if (currentPage < totalPages) { %>
            <a href="search?query=<%= queryStr %>&page=<%= currentPage + 1 %>">Next &rsaquo;</a>
            <a href="search?query=<%= queryStr %>&page=<%= totalPages %>" title="Last Page">Last &raquo;</a>
        <% } else { %>
            <span class="disabled">Next &rsaquo;</span>
            <span class="disabled">Last &raquo;</span>
        <% } %>
    </div>
</body>
</html>
