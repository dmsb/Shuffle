package com.example.shuffle;

public enum AvailableGenres {

    ROCK("rock", "hard-rock", "j-rock", "psych-rock","punk-rock", "rock-n-roll", "rockabilly"),
    BRAZIL("mpb"),
    POP("cantopop", "indie-pop","j-pop","k-pop","mandopop","pop","pop-film", "power-pop", "synth-pop"),
    INDIE("indie","indie-pop");

    private String[] genres;

    AvailableGenres(String... genres) {
        this.genres = genres;
    }

    public String[] getGenres() {
        return genres;
    }

}
