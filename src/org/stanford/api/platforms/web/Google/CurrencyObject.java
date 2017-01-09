package org.stanford.api.platforms.web.Google;

/**
 * Created by lauril on 9/21/16.
 * // [ { "id": "-2001" ,"t" : "GBPUSD" ,"e" : "CURRENCY" ,
 * "l" : "1.2982" ,"l_fix" : "" ,"l_cur" : "" ,"s": "0" ,
 * "ltt":"" ,"lt" : "Sep 21, 2:53AM GMT" ,"lt_dts" : "2016-09-21T02:53:10Z" ,
 * "c" : "-0.00013" ,"c_fix" : "" ,"cp" : "-0.010" ,"cp_fix" : "" ,"ccol" : "chr" ,"pcls_fix" : "" } ]
 */
 public class CurrencyObject {
    public String id;
    public String t;
    public String e;
    public String l;
    public String l_fix;
    public String l_cur;
    public String s;
    public String ltt;
    public String lt_dts;
    public String lt;
    public String c;
    public String c_fix;
    public String cp;
    public String cp_fix;
    public String ccol;
    public String pcls_fix;

    public CurrencyObject(String id, String t, String e, String l, String l_fix, String l_cur, String s, String ltt, String lt_dts, String lt, String c, String c_fix, String cp, String cp_fix, String ccol, String pcls_fix) {
        this.id = id;
        this.t = t;
        this.e = e;
        this.l = l;
        this.l_fix = l_fix;
        this.l_cur = l_cur;
        this.s = s;
        this.ltt = ltt;
        this.lt_dts = lt_dts;
        this.lt = lt;
        this.c = c;
        this.c_fix = c_fix;
        this.cp = cp;
        this.cp_fix = cp_fix;
        this.ccol = ccol;
        this.pcls_fix = pcls_fix;
    }

    @Override
    public String toString(){
        return "Exchange From To "+ this.t +
                " value " + this.l +
                " on "+ lt;
    }

    public String arrayPrint(){
        return "{" +
                "id:"+ this.id + ","+
                "t:"+ this.t +"," +
                "e:"+ e + ","+
                "l:"+ l + ","+
                "l_fix:"+ l_fix+ ","+
                "l_cur:"+l_cur + ","+
                "s:"+ s+ ","+
                "ltt:"+ ltt + ","+
                "lt_dts:"+ lt_dts + ","+
                "lt:"+ lt + ","+
                "c:"+ c + ","+
                "c_fix:"+ c_fix + ","+
                "cp:"+ cp + ","+
                "cp_fix:"+cp_fix + ","+
                "ccol:"+ ccol+ ","+
                "pcls_fix:"+ pcls_fix +
                "}";
    }
}
