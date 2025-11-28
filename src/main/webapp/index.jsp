<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Java Search Engine</title>
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
    
    <div class="index-container">
        <div class="logo">JAVA SEARCH ENGINE</div>
        <p class="tagline">Discover the web with style</p>

        <form action="search" method="get">
            <div class="search-box">
                <input type="text" name="query" class="search-input" 
                       placeholder="What are you looking for?" required>
                <button type="submit" class="search-button">Search</button>
            </div>
        </form>

        <div class="links">
            <a href="history">📜 Search History</a>
        </div>
    </div>
</body>
</html>