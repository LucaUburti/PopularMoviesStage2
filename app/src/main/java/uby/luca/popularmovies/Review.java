package uby.luca.popularmovies;

/**
 * Created by uburti on 25/02/2018.
 */

class Review {
    String author;
    String content;
    String url;

    Review(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}
