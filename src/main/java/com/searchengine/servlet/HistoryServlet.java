package com.searchengine.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        List<String> history = (List<String>) session.getAttribute("searchHistory");

        if (history == null) {
            history = new ArrayList<>();
        }

        request.setAttribute("searchHistory", history);
        request.getRequestDispatcher("/history.jsp").forward(request, response);
    }
}