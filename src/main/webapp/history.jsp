<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search History - Java Search Engine</title>
    <link rel="icon" type="image/png" href="favicon.png">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <!-- Animated particles -->
    <div class="particles">
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
        <div class="particle"></div>
    </div>
    
    <div class="history-container">
        <div class="history-header">
            <a href="index.jsp" class="history-logo">Search</a>
            <h2 class="history-title">📜 Search History</h2>
        </div>

        <div class="history-list">
            <c:choose>
                <c:when test="${not empty searchHistory}">
                    <c:forEach var="historyItem" items="${searchHistory}">
                        <div class="history-item">
                            <a href="search?query=${historyItem}">${historyItem}</a>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="no-history">
                        <p>No search history found.</p>
                        <p><a href="index.jsp">Start searching</a> to see your history here.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="back-link">
            <a href="index.jsp">← Back to Search</a>
        </div>
    </div>
</body>
</html>