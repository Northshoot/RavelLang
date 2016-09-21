package ai.harmony.api.platforms.web.Google;

/**
 * Created by lauril on 9/21/16.
 *  * id     : ID,

 t      : StockSymbol,

 e      : Index,

 l      : LastTradePrice,

 l_cur  : LastTradeWithCurrency,

 ltt    : LastTradeTime,

 lt_dts : LastTradeDateTime,

 lt     : LastTradeDateTimeLong,

 div    : Dividend,

 yld    : Yield,

 s      : LastTradeSize,

 c      : Change,

 c      : ChangePercent,

 el     : ExtHrsLastTradePrice,

 el_cur : ExtHrsLastTradeWithCurrency,

 elt    : ExtHrsLastTradeDateTimeLong,

 ec     : ExtHrsChange,

 ecp    : ExtHrsChangePercent,

 pcls_fix: PreviousClosePrice
 { "id": "304466804484872" ,"t" : "GOOG" ,
 "e" : "NASDAQ" ,
 "l" : "774.92" ,"l_fix" : "774.92" ,"l_cur" : "774.92" ,
 "s": "0" ,
 "ltt":"3:27PM EDT" ,
 "lt" : "Sep 21, 3:27PM EDT" ,
 "lt_dts" : "2016-09-21T15:27:01Z" ,
 "c" : "+3.51" ,"c_fix" : "3.51" ,
 "cp" : "0.46" ,"cp_fix" : "0.46" ,
 "ccol" : "chg" ,
 "pcls_fix" : "771.41" }
 */
public class StockObject {
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

    public StockObject(String id, String t, String e, String l, String l_fix, String l_cur, String s, String ltt, String lt_dts, String lt, String c, String c_fix, String cp, String cp_fix, String ccol, String pcls_fix) {
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
        return "On market " + this.e +
                " " +
                " price " + this.l +
                " time "+ lt;
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
