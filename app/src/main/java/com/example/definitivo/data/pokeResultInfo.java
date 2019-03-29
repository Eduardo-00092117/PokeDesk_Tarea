package com.example.definitivo.data;

import java.util.ArrayList;

public class pokeResultInfo {
    private int count;
    private String next;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<pokemonResul> getResults() {
        return results;
    }

    public void setResults(ArrayList<pokemonResul> results) {
        this.results = results;
    }

    private String previous;
    private ArrayList<pokemonResul> results;


}
