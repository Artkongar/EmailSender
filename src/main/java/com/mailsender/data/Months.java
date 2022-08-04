package com.mailsender.data;

public enum Months {

    JANUARY(1, "Январь"),
    FEBRUARY(2, "Февраль"),
    MARCH(3, "Март"),
    APRIL(4, "Апрель"),
    MAY(5, "Май"),
    JUNE(6, "Июнь"),
    JULY(7, "Июль"),
    AUGUST(8, "Август"),
    SEPTEMBER(9, "Сентябрь"),
    OCTOBER(10, "Октябрь"),
    NOVEMBER(11, "Ноябрь"),
    DECEMBER(12, "Декабрь");

    private int monthNumber;
    private String ruName;

    Months(int monthNumber, String ruName) {
       this.monthNumber = monthNumber;
       this.ruName = ruName;
    }

    public String getRuName() {
        return ruName;
    }

    public static Months getByMonthNumber(int monthNumber) throws Exception {
        for (Months month : values()){
            if (month.monthNumber == monthNumber){
                return month;
            }
        }
        throw new Exception("No month with such number");
    }
}

