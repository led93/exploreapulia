package com.example.ep.domain;

public enum Region {
    Salento("Salento"),
    Gargano("Gargano"),
    Murgia("Murgia");
    private final String label;

    Region(String label) {
        this.label = label;
    }

    public static Region findByLabel(String label) {
        for(Region r : Region.values()) {
            if(r.label.equalsIgnoreCase(label)) {
                return r;
            }
        }
        return null;
    }
}
