<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Java Search Engine</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #fff;
        }
        .container {
            max-width: 600px;
            margin: 200px auto;
            text-align: center;
        }
        .logo {
            font-size: 2.5rem;
            margin-bottom: 30px;
            color: #333;
        }
        .search-box {
            display: flex;
            border: 2px solid #ddd;
            border-radius: 25px;
            padding: 10px;
            margin-bottom: 30px;
        }
        .search-input {
            flex: 1;
            border: none;
            outline: none;
            padding: 10px 15px;
            font-size: 16px;
        }
        .search-button {
            background: #4285f4;
            color: white;
            border: none;
            padding: 10px 25px;
            border-radius: 20px;
            cursor: pointer;
            font-size: 16px;
        }
        .search-button:hover {
            background: #3367d6;
        }
        .links {
            margin-top: 20px;
        }
        .links a {
            color: #4285f4;
            text-decoration: none;
            margin: 0 10px;
        }
        .links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="logo">Java Search Engine</div>

        <form action="search" method="get">
            <div class="search-box">
                <input type="text" name="query" class="search-input" 
                       placeholder="Search JavaTpoint tutorials..." required>
                <button type="submit" class="search-button">Search</button>
            </div>
        </form>

        <div class="links">
            <a href="history">History</a>
        </div>
    </div>
</body>
</html>