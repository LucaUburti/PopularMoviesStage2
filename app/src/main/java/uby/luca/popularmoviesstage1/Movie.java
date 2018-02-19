package uby.luca.popularmoviesstage1;

/**
 * Created by uburti on 19/02/2018.
 */

public class Movie {

    private String title;
    private String poster;
    private String voteAverage;
    private String plot;

    public Movie(String title, String poster, String voteAverage, String plot) {
        this.title = title;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
}
