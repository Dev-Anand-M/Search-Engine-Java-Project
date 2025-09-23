package com.searchengine.servlet;

import com.searchengine.model.WebPage;
import com.searchengine.search.SearchEngine;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private SearchEngine searchEngine = new SearchEngine();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String query = request.getParameter("query");
        if (query == null || query.trim().isEmpty()) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // Save to session history
        HttpSession session = request.getSession();
        List<String> history = (List<String>) session.getAttribute("searchHistory");
        if (history == null) {
            history = new java.util.ArrayList<>();
        }
        if (!history.contains(query.trim())) {
            history.add(0, query.trim()); // Add at beginning
            if (history.size() > 20) {
                history = history.subList(0, 20); // Keep only last 20
            }
        }
        session.setAttribute("searchHistory", history);

        // Perform search
        List<WebPage> results = searchEngine.search(query.trim(), 30);

        // Set attributes
        request.setAttribute("query", query);
        request.setAttribute("results", results);
        request.setAttribute("totalResults", results.size());
        request.setAttribute("hasResults", !results.isEmpty());

        request.getRequestDispatcher("/results.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}