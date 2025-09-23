<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search History - Java Search Engine</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #fff;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
        }
        .header {
            margin-bottom: 30px;
        }
        .logo {
            color: #4285f4;
            font-size: 1.8rem;
            text-decoration: none;
        }
        .history-title {
            font-size: 1.5rem;
            margin: 20px 0;
        }
        .history-item {
            padding: 10px 0;
            border-bottom: 1px solid #ebebeb;
        }
        .history-item a {
            color: #1a0dab;
            text-decoration: none;
            font-size: 16px;
        }
        .history-item a:hover {
            text-decoration: underline;
        }
        .no-history {
            text-align: center;
            color: #666;
            padding: 50px;
        }
        .back-link {
            margin-top: 30px;
        }
        .back-link a {
            color: #4285f4;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <a href="index.jsp" class="logo">Java Search Engine</a>
            <h2 class="history-title">Search History</h2>
        </div>

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

        <div class="back-link">
            <a href="index.jsp">← Back to Search</a>
        </div>
    </div>
</body>
</html>