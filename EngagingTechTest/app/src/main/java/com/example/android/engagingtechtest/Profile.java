package com.example.android.engagingtechtest;

import java.util.ArrayList;

public class Profile {

    private String uuID;
    private String name;
    private String profileURL;
    private String userNumber;
    private ArrayList<TelephoneNumber> numbers;

    public Profile (String uuID, String name, String profileURL, String userNumber, ArrayList<TelephoneNumber> numbers) {
        this.uuID = uuID;
        this.name = formatName(name);
        this.profileURL = profileURL;
        this.userNumber = userNumber;
        this.numbers = numbers;
    }

    public String formatName(String original) {
        String initial = original.replace(" ", "").replace("\n", "");

        char[] array = initial.toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0 && Character.isUpperCase(array[i])) {
                str.append(" ");
            }
            str.append(array[i]);
        }

        return str.toString();
    }

    public String getProfileURL() {
        return profileURL;
    }

    public String getUuID() {
        return uuID;
    }

    public String getName() {
        return name;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public ArrayList<TelephoneNumber> getNumbers() {
        return numbers;
    }
}
