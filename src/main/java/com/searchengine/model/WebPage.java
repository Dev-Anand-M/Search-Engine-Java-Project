package com.searchengine.model;

public class WebPage {
    private int id;
    private String url;
    private String title;
    private String content;
    private int relevanceScore;

    public WebPage() {}

    public WebPage(String url, String title, String content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(int relevanceScore) { this.relevanceScore = relevanceScore; }

    public String generateSnippet(String query) {
        if (content == null || content.isEmpty()) return "";

        String lowerContent = content.toLowerCase();
        String lowerQuery = query.toLowerCase();

        int index = lowerContent.indexOf(lowerQuery);
        if (index == -1) {
            return content.length() > 200 ? content.substring(0, 200) + "..." : content;
        }

        int start = Math.max(0, index - 100);
        int end = Math.min(content.length(), index + 150);

        String snippet = content.substring(start, end);
        if (start > 0) snippet = "..." + snippet;
        if (end < content.length()) snippet = snippet + "...";

        return snippet;
    }
}