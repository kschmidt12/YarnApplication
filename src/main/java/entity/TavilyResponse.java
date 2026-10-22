package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//https://www.javacodegeeks.com/2018/01/ignore-unknown-properties-parsing-json-java-jackson-jsonignoreproperties-annotation-example.html#google_vignette
@JsonIgnoreProperties(ignoreUnknown = true)
public class TavilyResponse {
    private String query;
    private List<TavilyResult> results;

    public TavilyResponse() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<TavilyResult> getResults() {
        return results;
    }

    public void setResults(List<TavilyResult> results) {
        this.results = results;
    }
}
