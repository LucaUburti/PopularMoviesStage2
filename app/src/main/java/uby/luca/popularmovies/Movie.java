package uby.luca.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by uburti on 19/02/2018.
 */


public class Movie implements Parcelable{

    private String title;
    private String poster;
    private String voteAverage;
    private String plot;
    private String releaseDate;


    public Movie(String title, String poster, String voteAverage, String plot, String releaseDate) {
        this.title = title;
        this.poster = poster;
        this.voteAverage = voteAverage;
        this.plot = plot;
        this.releaseDate=releaseDate;
    }


    protected Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        voteAverage = in.readString();
        plot = in.readString();
        releaseDate=in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(voteAverage);
        dest.writeString(plot);
        dest.writeString(releaseDate);
    }
}
