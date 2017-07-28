package com.coloredpanda.utils;


public enum Emoji {

    WAVING_HAND_SIGN('\uD83D', '\uDC4B'),
    FRAMED_PICTURE('\uD83D', '\uDDBC'),
    MAN('\uD83D', '\uDC68');

    Character firstChar;
    Character secondChar;

    Emoji(Character firstChar, Character secondChar) {
        this.firstChar = firstChar;
        this.secondChar = secondChar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.firstChar != null) {
            sb.append(this.firstChar);
        }
        if (this.secondChar != null) {
            sb.append(this.secondChar);
        }

        return sb.toString();
    }

}
