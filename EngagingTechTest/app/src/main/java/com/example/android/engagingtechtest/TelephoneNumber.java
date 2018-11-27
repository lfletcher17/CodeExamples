package com.example.android.engagingtechtest;

public class TelephoneNumber {

    private String type;
    private String number;

    public TelephoneNumber(String type, String number) {
        this.type = type;
        this.number = formatNumber(number);
    }

    public String formatNumber(String original) {
        String initial = original.replace(" ", "").replace("\n", "");

        char[] array = initial.toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i==3) {
                str.append(" (");
            }
            if(i==6) {
                str.append(") ");
            }
            if(i==9) {
                str.append("-");
            }
            str.append(array[i]);
        }

        return str.toString();
    }

    @Override
    public String toString() {
        return this.type + " " + this.number;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

}
