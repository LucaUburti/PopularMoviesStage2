package uby.luca.popularmovies;

/**
 * Created by uburti on 25/02/2018.
 */

class Trailer {
    String key;
    String name;

    Trailer(String key, String name) {
        this.key = key;
        this.name = name;
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}