package org.vaadin.example;

public class User {
    private final String _id;
    private final String mscode;
    private final String year;
    private final String estCode;
    private final String estimate;
    private final String se;
    private final String lowerCIB;
    private final String upperCIB;
    private final String flag;

    public User(String _id, String mscode, String year, String estCode, String estimate, String se, String lowerCIB, String upperCIB, String flag) {
        this._id = _id;
        this.mscode = mscode;
        this.year = year;
        this.estCode = estCode;
        this.estimate = estimate;
        this.se = se;
        this.lowerCIB = lowerCIB;
        this.upperCIB = upperCIB;
        this.flag = flag;
    }



    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", mscode='" + mscode + '\'' +
                ", year=" + year +
                ", estCode='" + estCode + '\'' +
                ", estimate=" + estimate +
                ", se=" + se +
                ", lowerCIB=" + lowerCIB +
                ", upperCIB=" + upperCIB +
                ", flag='" + flag + '\'' +
                '}';
    }

}