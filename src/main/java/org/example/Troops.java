package org.example;

import java.util.List;

import static java.lang.Integer.valueOf;

public class Troops {
    public Integer T1;
    public Integer T2;
    public Integer T3;
    public Integer T4;
    public Integer T5;
    public Integer T6;
    public Boolean sendTroops = false;

    public Troops() {
    }

    public Troops(Integer t1, Integer t2, Integer t3, Integer t4, Integer t5, Integer t6) {
        T1 = t1;
        T2 = t2;
        T3 = t3;
        T4 = t4;
        T5 = t5;
        T6 = t6;
    }

    public Troops(Integer t1, Integer t2, Integer t3, Integer t4, Integer t5, Integer t6, Boolean sendTroops) {
        this(t1, t2, t3, t4, t5, t6);
        this.sendTroops = sendTroops;
    }

    public void subAllTroops(org.example.Troops smaller) {
        T1 -= smaller.T1;
        T2 -= smaller.T2;
        T3 -= smaller.T3;
        T4 -= smaller.T4;
        T5 -= smaller.T5;
        T6 -= smaller.T6;
    }

    public String toUrl() {
        if (T1 > 0)
            return "&troop[t1]=" + T1;
        if (T2 > 0)
            return "&troop[t2]=" + T2;
        if (T3 > 0)
            return "&troop[t3]=" + T3;
        if (T4 > 0)
            return "&troop[t4]=" + T4;
        if (T5 > 0)
            return "&troop[t5]=" + T5;
        if (T6 > 0)
            return "&troop[t6]=" + T6;
        return "";
    }

    public String toSaveToFileFormat() {
        return T1 + "/" + T2 + "/" + T3 + "/" + T4 + "/" + T5 + "/" + T6 + "/" + sendTroops;
    }

    public void fromSaveToFileFormat(String text) {
        String[] splitted = text.split("/");
        T1 = valueOf(splitted[0]);
        T2 = valueOf(splitted[1]);
        T3 = valueOf(splitted[2]);
        T4 = valueOf(splitted[3]);
        T5 = valueOf(splitted[4]);
        T6 = valueOf(splitted[5]);
        sendTroops = Boolean.parseBoolean(splitted[6]);
    }
}
