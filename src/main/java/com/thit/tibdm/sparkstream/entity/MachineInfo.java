//package com.thit.tibdm.sparkstream.entity;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.Serializable;
//
///**
// * 机器采集信息
// *
// * @author wanghaoqiang
// * @version 1.0
// * @create 2017-06-03 14:31
// **/
//public class MachineInfo implements Serializable{
//    public final static Logger LOGGER = LoggerFactory.getLogger(MachineInfo.class);
//    /**
//     *
//     */
//    private static final long serialVersionUID = 6681372116317508248L;
//
//    /**
//     * ZT159 : 6
//     * ZT158 : 6
//     * ZT157 : 7
//     * ZT156 : 4
//     * ZT151 : 0
//     * ZT150 : 6
//     * ZT155 : 4
//     * ZT154 : 7
//     * ZT153 : 2
//     * ZT152 : 2
//     * ZT169 : 3
//     * ZT168 : 3
//     * ZT167 : 4
//     * ZT162 : 2
//     * ZT161 : 3
//     * ZT160 : 2
//     * ZT166 : 1
//     * ZT165 : 3
//     * ZT164 : 1
//     * ZT163 : 2
//     * ZT1 : 324
//     * ZT3 : 6
//     * ZT2 : 2017.00
//     * COLLECT_TIME : 1496471423012
//     * ZT5 : 7
//     * ZT4 : 20
//     * ZT7 : 40
//     * ZT6 : 34
//     * ZT9 : 0
//     * ZT8 : 2
//     * ZT179 : 38.40
//     * ZT178 : 14.10
//     * ZT173 : 1
//     * ZT172 : 5
//     * ZT171 : 4
//     * ZT170 : 1
//     * ZT177 : -5.50
//     * ZT176 : 27.80
//     * ZT175 : 10.50
//     * ZT174 : 15.40
//     * ZT189 : 17.00
//     * CH : 324
//     * ZT184 : -1.20
//     * ZT183 : 26.10
//     * ZT182 : -2.30
//     * ZT181 : 10.00
//     * ZT188 : 36.10
//     * ZT187 : -1.70
//     * ZT186 : 9.60
//     * ZT185 : -2.60
//     * GZ11 : 0
//     * GZ10 : 0
//     * ZT180 : 15.00
//     * GZ13 : 0
//     * GZ12 : 0
//     * GZ15 : 0
//     * GZ14 : 0
//     * GZ16 : 0
//     * ZT195 : 14634
//     * ZT194 : 42943
//     * ZT193 : 8744
//     * ZT192 : 6634
//     * ZT199 : 0.70
//     * ZT198 : 99
//     * ZT197 : 13834
//     * ZT196 : 2617
//     * ZT191 : 3.80
//     * ZT190 : 14.80
//     * ZT19 : 50.59
//     * ZT17 : 2188
//     * ZT18 : 731
//     * ZT22 : 18
//     * ZT23 : 24
//     * ZT20 : 4
//     * ZT21 : 14
//     * ZT26 : 0
//     * ZT27 : 0
//     * ZT24 : 2.70
//     * ZT25 : 53
//     * ZT11 : 0
//     * ZT12 : 0
//     * ZT10 : 0
//     * ZT15 : 0
//     * ZT16 : 1
//     * ZT13 : 0
//     * ZT14 : 0
//     * ZT39 : 2
//     * ZT44 : 0
//     * ZT45 : 0
//     * ZT42 : 0
//     * ZT43 : 0
//     * ZT48 : 2
//     * ZT49 : 3
//     * ZT46 : 0
//     * ZT47 : 1
//     * MACHINE_ID : 324
//     * ZT40 : 0
//     * ZT41 : 1
//     * ZT203 : 190703885
//     * ZT202 : 329391467
//     * ZT201 : 382082209
//     * ZT200 : 73834998
//     * ZT207 : 43839991860.00
//     * ZT206 : 50273325180.00
//     * ZT205 : 309234390
//     * ZT204 : 493637645
//     * ZT28 : 0
//     * RECEIVE_TIME : 1459423125123
//     * ZT29 : 0
//     * ZT33 : 3
//     * ZT34 : 0
//     * ZT31 : 1
//     * ZT32 : 0
//     * ZT37 : 3
//     * ZT38 : 2
//     * ZT35 : 2
//     * ZT36 : 3
//     * ZT30 : 2
//     * ZT210 : 878601600.00
//     * ZT66 : 796
//     * ZT67 : 1515
//     * ZT64 : 440
//     * ZT65 : 1848
//     * ZT68 : -0.70
//     * ZT69 : 5.70
//     * ZT209 : 2659262400.00
//     * ZT208 : 1041120000.00
//     * ZT62 : 552
//     * ZT63 : 876
//     * ZT60 : 1006
//     * ZT61 : 923
//     * ZT104 : 1161.30
//     * ZT103 : 962.20
//     * ZT102 : 3835.10
//     * ZT101 : 1339.40
//     * ZT108 : 3
//     * ZT107 : 4555.40
//     * ZT106 : 2428.50
//     * ZT105 : 2385.10
//     * ZT100 : 4451.10
//     * ZT55 : 1
//     * ZT56 : 257
//     * ZT53 : 2
//     * ZT54 : 3
//     * ZT59 : 1553
//     * ZT57 : 203
//     * ZT58 : 602
//     * ZT51 : 3
//     * ZT52 : 3
//     * ZT50 : 2
//     * ZT115 : 5
//     * ZT114 : 0
//     * ZT113 : 1
//     * ZT112 : 2
//     * ZT119 : 4
//     * ZT118 : 0
//     * ZT117 : 7
//     * ZT116 : 3
//     * ZT111 : 7
//     * ZT110 : 6
//     * ZT88 : 1
//     * ZT89 : 2
//     * ZT86 : 2
//     * ZT87 : 1
//     * COLLECT_TYPE : 240
//     * ZT80 : 2
//     * ZT81 : 0
//     * ZT109 : 7
//     * ZT84 : 3
//     * ZT85 : 2
//     * ZT82 : 1
//     * GZ2 : 0
//     * ZT83 : 2
//     * GZ1 : 0
//     * ZT126 : 5
//     * GZ4 : 0
//     * ZT125 : 5
//     * GZ3 : 0
//     * ZT124 : 1
//     * GZ6 : 0
//     * ZT123 : 3
//     * GZ5 : 0
//     * GZ8 : 0
//     * ZT129 : 0
//     * GZ7 : 0
//     * ZT128 : 1
//     * ZT127 : 5
//     * GZ9 : 0
//     * ZT122 : 3
//     * ZT121 : 7
//     * ZT120 : 4
//     * ZT77 : 0
//     * ZT78 : 1
//     * ZT75 : 2
//     * ZT76 : 2
//     * ZT79 : 0
//     * ZT70 : 20.60
//     * ZT73 : 3
//     * ZT74 : 3
//     * ZT71 : 25.40
//     * ZT72 : 1
//     * ZT137 : 3
//     * ZT136 : 4
//     * ZT135 : 5
//     * ZT134 : 3
//     * ZT139 : 3
//     * ZT138 : 2
//     * ZT133 : 0
//     * ZT132 : 5
//     * ZT131 : 0
//     * ZT130 : 0
//     * ZT148 : 1
//     * ZT147 : 0
//     * ZT146 : 6
//     * ZT145 : 1
//     * ZT149 : 0
//     * ZT140 : 2
//     * ZT144 : 2
//     * ZT143 : 0
//     * ZT142 : 4
//     * ZT141 : 0
//     * ZT99 : 3906.70
//     * ZT97 : 768.30
//     * ZT98 : 2967.90
//     * ZT91 : 4148.20
//     * ZT92 : 59.00
//     * ZT90 : 2009.20
//     * ZT95 : 1959.00
//     * ZT96 : 4709.80
//     * ZT93 : 300.70
//     * ZT94 : 4029.60
//     */
//
//    private String ZT159;
//    private String ZT158;
//    private String ZT157;
//    private String ZT156;
//    private String ZT151;
//    private String ZT150;
//    private String ZT155;
//    private String ZT154;
//    private String ZT153;
//    private String ZT152;
//    private String ZT169;
//    private String ZT168;
//    private String ZT167;
//    private String ZT162;
//    private String ZT161;
//    private String ZT160;
//    private String ZT166;
//    private String ZT165;
//    private String ZT164;
//    private String ZT163;
//    private String ZT1;
//    private String ZT3;
//    private String ZT2;
//    private String COLLECT_TIME;
//    private String ZT5;
//    private String ZT4;
//    private String ZT7;
//    private String ZT6;
//    private String ZT9;
//    private String ZT8;
//    private String ZT179;
//    private String ZT178;
//    private String ZT173;
//    private String ZT172;
//    private String ZT171;
//    private String ZT170;
//    private String ZT177;
//    private String ZT176;
//    private String ZT175;
//    private String ZT174;
//    private String ZT189;
//    private String CH;
//    private String ZT184;
//    private String ZT183;
//    private String ZT182;
//    private String ZT181;
//    private String ZT188;
//    private String ZT187;
//    private String ZT186;
//    private String ZT185;
//    private String GZ11;
//    private String GZ10;
//    private String ZT180;
//    private String GZ13;
//    private String GZ12;
//    private String GZ15;
//    private String GZ14;
//    private String GZ16;
//    private String ZT195;
//    private String ZT194;
//    private String ZT193;
//    private String ZT192;
//    private String ZT199;
//    private String ZT198;
//    private String ZT197;
//    private String ZT196;
//    private String ZT191;
//    private String ZT190;
//    private String ZT19;
//    private String ZT17;
//    private String ZT18;
//    private String ZT22;
//    private String ZT23;
//    private String ZT20;
//    private String ZT21;
//    private String ZT26;
//    private String ZT27;
//    private String ZT24;
//    private String ZT25;
//    private String ZT11;
//    private String ZT12;
//    private String ZT10;
//    private String ZT15;
//    private String ZT16;
//    private String ZT13;
//    private String ZT14;
//    private String ZT39;
//    private String ZT44;
//    private String ZT45;
//    private String ZT42;
//    private String ZT43;
//    private String ZT48;
//    private String ZT49;
//    private String ZT46;
//    private String ZT47;
//    private String MACHINE_ID;
//    private String ZT40;
//    private String ZT41;
//    private String ZT203;
//    private String ZT202;
//    private String ZT201;
//    private String ZT200;
//    private String ZT207;
//    private String ZT206;
//    private String ZT205;
//    private String ZT204;
//    private String ZT28;
//    private String RECEIVE_TIME;
//    private String ZT29;
//    private String ZT33;
//    private String ZT34;
//    private String ZT31;
//    private String ZT32;
//    private String ZT37;
//    private String ZT38;
//    private String ZT35;
//    private String ZT36;
//    private String ZT30;
//    private String ZT210;
//    private String ZT66;
//    private String ZT67;
//    private String ZT64;
//    private String ZT65;
//    private String ZT68;
//    private String ZT69;
//    private String ZT209;
//    private String ZT208;
//    private String ZT62;
//    private String ZT63;
//    private String ZT60;
//    private String ZT61;
//    private String ZT104;
//    private String ZT103;
//    private String ZT102;
//    private String ZT101;
//    private String ZT108;
//    private String ZT107;
//    private String ZT106;
//    private String ZT105;
//    private String ZT100;
//    private String ZT55;
//    private String ZT56;
//    private String ZT53;
//    private String ZT54;
//    private String ZT59;
//    private String ZT57;
//    private String ZT58;
//    private String ZT51;
//    private String ZT52;
//    private String ZT50;
//    private String ZT115;
//    private String ZT114;
//    private String ZT113;
//    private String ZT112;
//    private String ZT119;
//    private String ZT118;
//    private String ZT117;
//    private String ZT116;
//    private String ZT111;
//    private String ZT110;
//    private String ZT88;
//    private String ZT89;
//    private String ZT86;
//    private String ZT87;
//    private String COLLECT_TYPE;
//    private String ZT80;
//    private String ZT81;
//    private String ZT109;
//    private String ZT84;
//    private String ZT85;
//    private String ZT82;
//    private String GZ2;
//    private String ZT83;
//    private String GZ1;
//    private String ZT126;
//    private String GZ4;
//    private String ZT125;
//    private String GZ3;
//    private String ZT124;
//    private String GZ6;
//    private String ZT123;
//    private String GZ5;
//    private String GZ8;
//    private String ZT129;
//    private String GZ7;
//    private String ZT128;
//    private String ZT127;
//    private String GZ9;
//    private String ZT122;
//    private String ZT121;
//    private String ZT120;
//    private String ZT77;
//    private String ZT78;
//    private String ZT75;
//    private String ZT76;
//    private String ZT79;
//    private String ZT70;
//    private String ZT73;
//    private String ZT74;
//    private String ZT71;
//    private String ZT72;
//    private String ZT137;
//    private String ZT136;
//    private String ZT135;
//    private String ZT134;
//    private String ZT139;
//    private String ZT138;
//    private String ZT133;
//    private String ZT132;
//    private String ZT131;
//    private String ZT130;
//    private String ZT148;
//    private String ZT147;
//    private String ZT146;
//    private String ZT145;
//    private String ZT149;
//    private String ZT140;
//    private String ZT144;
//    private String ZT143;
//    private String ZT142;
//    private String ZT141;
//    private String ZT99;
//    private String ZT97;
//    private String ZT98;
//    private String ZT91;
//    private String ZT92;
//    private String ZT90;
//    private String ZT95;
//    private String ZT96;
//    private String ZT93;
//    private String ZT94;
//
//    public String getZT159() {
//        return ZT159;
//    }
//
//    public void setZT159(String ZT159) {
//        this.ZT159 = ZT159;
//    }
//
//    public String getZT158() {
//        return ZT158;
//    }
//
//    public void setZT158(String ZT158) {
//        this.ZT158 = ZT158;
//    }
//
//    public String getZT157() {
//        return ZT157;
//    }
//
//    public void setZT157(String ZT157) {
//        this.ZT157 = ZT157;
//    }
//
//    public String getZT156() {
//        return ZT156;
//    }
//
//    public void setZT156(String ZT156) {
//        this.ZT156 = ZT156;
//    }
//
//    public String getZT151() {
//        return ZT151;
//    }
//
//    public void setZT151(String ZT151) {
//        this.ZT151 = ZT151;
//    }
//
//    public String getZT150() {
//        return ZT150;
//    }
//
//    public void setZT150(String ZT150) {
//        this.ZT150 = ZT150;
//    }
//
//    public String getZT155() {
//        return ZT155;
//    }
//
//    public void setZT155(String ZT155) {
//        this.ZT155 = ZT155;
//    }
//
//    public String getZT154() {
//        return ZT154;
//    }
//
//    public void setZT154(String ZT154) {
//        this.ZT154 = ZT154;
//    }
//
//    public String getZT153() {
//        return ZT153;
//    }
//
//    public void setZT153(String ZT153) {
//        this.ZT153 = ZT153;
//    }
//
//    public String getZT152() {
//        return ZT152;
//    }
//
//    public void setZT152(String ZT152) {
//        this.ZT152 = ZT152;
//    }
//
//    public String getZT169() {
//        return ZT169;
//    }
//
//    public void setZT169(String ZT169) {
//        this.ZT169 = ZT169;
//    }
//
//    public String getZT168() {
//        return ZT168;
//    }
//
//    public void setZT168(String ZT168) {
//        this.ZT168 = ZT168;
//    }
//
//    public String getZT167() {
//        return ZT167;
//    }
//
//    public void setZT167(String ZT167) {
//        this.ZT167 = ZT167;
//    }
//
//    public String getZT162() {
//        return ZT162;
//    }
//
//    public void setZT162(String ZT162) {
//        this.ZT162 = ZT162;
//    }
//
//    public String getZT161() {
//        return ZT161;
//    }
//
//    public void setZT161(String ZT161) {
//        this.ZT161 = ZT161;
//    }
//
//    public String getZT160() {
//        return ZT160;
//    }
//
//    public void setZT160(String ZT160) {
//        this.ZT160 = ZT160;
//    }
//
//    public String getZT166() {
//        return ZT166;
//    }
//
//    public void setZT166(String ZT166) {
//        this.ZT166 = ZT166;
//    }
//
//    public String getZT165() {
//        return ZT165;
//    }
//
//    public void setZT165(String ZT165) {
//        this.ZT165 = ZT165;
//    }
//
//    public String getZT164() {
//        return ZT164;
//    }
//
//    public void setZT164(String ZT164) {
//        this.ZT164 = ZT164;
//    }
//
//    public String getZT163() {
//        return ZT163;
//    }
//
//    public void setZT163(String ZT163) {
//        this.ZT163 = ZT163;
//    }
//
//    public String getZT1() {
//        return ZT1;
//    }
//
//    public void setZT1(String ZT1) {
//        this.ZT1 = ZT1;
//    }
//
//    public String getZT3() {
//        return ZT3;
//    }
//
//    public void setZT3(String ZT3) {
//        this.ZT3 = ZT3;
//    }
//
//    public String getZT2() {
//        return ZT2;
//    }
//
//    public void setZT2(String ZT2) {
//        this.ZT2 = ZT2;
//    }
//
//    public String getCOLLECT_TIME() {
//        return COLLECT_TIME;
//    }
//
//    public void setCOLLECT_TIME(String COLLECT_TIME) {
//        this.COLLECT_TIME = COLLECT_TIME;
//    }
//
//    public String getZT5() {
//        return ZT5;
//    }
//
//    public void setZT5(String ZT5) {
//        this.ZT5 = ZT5;
//    }
//
//    public String getZT4() {
//        return ZT4;
//    }
//
//    public void setZT4(String ZT4) {
//        this.ZT4 = ZT4;
//    }
//
//    public String getZT7() {
//        return ZT7;
//    }
//
//    public void setZT7(String ZT7) {
//        this.ZT7 = ZT7;
//    }
//
//    public String getZT6() {
//        return ZT6;
//    }
//
//    public void setZT6(String ZT6) {
//        this.ZT6 = ZT6;
//    }
//
//    public String getZT9() {
//        return ZT9;
//    }
//
//    public void setZT9(String ZT9) {
//        this.ZT9 = ZT9;
//    }
//
//    public String getZT8() {
//        return ZT8;
//    }
//
//    public void setZT8(String ZT8) {
//        this.ZT8 = ZT8;
//    }
//
//    public String getZT179() {
//        return ZT179;
//    }
//
//    public void setZT179(String ZT179) {
//        this.ZT179 = ZT179;
//    }
//
//    public String getZT178() {
//        return ZT178;
//    }
//
//    public void setZT178(String ZT178) {
//        this.ZT178 = ZT178;
//    }
//
//    public String getZT173() {
//        return ZT173;
//    }
//
//    public void setZT173(String ZT173) {
//        this.ZT173 = ZT173;
//    }
//
//    public String getZT172() {
//        return ZT172;
//    }
//
//    public void setZT172(String ZT172) {
//        this.ZT172 = ZT172;
//    }
//
//    public String getZT171() {
//        return ZT171;
//    }
//
//    public void setZT171(String ZT171) {
//        this.ZT171 = ZT171;
//    }
//
//    public String getZT170() {
//        return ZT170;
//    }
//
//    public void setZT170(String ZT170) {
//        this.ZT170 = ZT170;
//    }
//
//    public String getZT177() {
//        return ZT177;
//    }
//
//    public void setZT177(String ZT177) {
//        this.ZT177 = ZT177;
//    }
//
//    public String getZT176() {
//        return ZT176;
//    }
//
//    public void setZT176(String ZT176) {
//        this.ZT176 = ZT176;
//    }
//
//    public String getZT175() {
//        return ZT175;
//    }
//
//    public void setZT175(String ZT175) {
//        this.ZT175 = ZT175;
//    }
//
//    public String getZT174() {
//        return ZT174;
//    }
//
//    public void setZT174(String ZT174) {
//        this.ZT174 = ZT174;
//    }
//
//    public String getZT189() {
//        return ZT189;
//    }
//
//    public void setZT189(String ZT189) {
//        this.ZT189 = ZT189;
//    }
//
//    public String getCH() {
//        return CH;
//    }
//
//    public void setCH(String CH) {
//        this.CH = CH;
//    }
//
//    public String getZT184() {
//        return ZT184;
//    }
//
//    public void setZT184(String ZT184) {
//        this.ZT184 = ZT184;
//    }
//
//    public String getZT183() {
//        return ZT183;
//    }
//
//    public void setZT183(String ZT183) {
//        this.ZT183 = ZT183;
//    }
//
//    public String getZT182() {
//        return ZT182;
//    }
//
//    public void setZT182(String ZT182) {
//        this.ZT182 = ZT182;
//    }
//
//    public String getZT181() {
//        return ZT181;
//    }
//
//    public void setZT181(String ZT181) {
//        this.ZT181 = ZT181;
//    }
//
//    public String getZT188() {
//        return ZT188;
//    }
//
//    public void setZT188(String ZT188) {
//        this.ZT188 = ZT188;
//    }
//
//    public String getZT187() {
//        return ZT187;
//    }
//
//    public void setZT187(String ZT187) {
//        this.ZT187 = ZT187;
//    }
//
//    public String getZT186() {
//        return ZT186;
//    }
//
//    public void setZT186(String ZT186) {
//        this.ZT186 = ZT186;
//    }
//
//    public String getZT185() {
//        return ZT185;
//    }
//
//    public void setZT185(String ZT185) {
//        this.ZT185 = ZT185;
//    }
//
//    public String getGZ11() {
//        return GZ11;
//    }
//
//    public void setGZ11(String GZ11) {
//        this.GZ11 = GZ11;
//    }
//
//    public String getGZ10() {
//        return GZ10;
//    }
//
//    public void setGZ10(String GZ10) {
//        this.GZ10 = GZ10;
//    }
//
//    public String getZT180() {
//        return ZT180;
//    }
//
//    public void setZT180(String ZT180) {
//        this.ZT180 = ZT180;
//    }
//
//    public String getGZ13() {
//        return GZ13;
//    }
//
//    public void setGZ13(String GZ13) {
//        this.GZ13 = GZ13;
//    }
//
//    public String getGZ12() {
//        return GZ12;
//    }
//
//    public void setGZ12(String GZ12) {
//        this.GZ12 = GZ12;
//    }
//
//    public String getGZ15() {
//        return GZ15;
//    }
//
//    public void setGZ15(String GZ15) {
//        this.GZ15 = GZ15;
//    }
//
//    public String getGZ14() {
//        return GZ14;
//    }
//
//    public void setGZ14(String GZ14) {
//        this.GZ14 = GZ14;
//    }
//
//    public String getGZ16() {
//        return GZ16;
//    }
//
//    public void setGZ16(String GZ16) {
//        this.GZ16 = GZ16;
//    }
//
//    public String getZT195() {
//        return ZT195;
//    }
//
//    public void setZT195(String ZT195) {
//        this.ZT195 = ZT195;
//    }
//
//    public String getZT194() {
//        return ZT194;
//    }
//
//    public void setZT194(String ZT194) {
//        this.ZT194 = ZT194;
//    }
//
//    public String getZT193() {
//        return ZT193;
//    }
//
//    public void setZT193(String ZT193) {
//        this.ZT193 = ZT193;
//    }
//
//    public String getZT192() {
//        return ZT192;
//    }
//
//    public void setZT192(String ZT192) {
//        this.ZT192 = ZT192;
//    }
//
//    public String getZT199() {
//        return ZT199;
//    }
//
//    public void setZT199(String ZT199) {
//        this.ZT199 = ZT199;
//    }
//
//    public String getZT198() {
//        return ZT198;
//    }
//
//    public void setZT198(String ZT198) {
//        this.ZT198 = ZT198;
//    }
//
//    public String getZT197() {
//        return ZT197;
//    }
//
//    public void setZT197(String ZT197) {
//        this.ZT197 = ZT197;
//    }
//
//    public String getZT196() {
//        return ZT196;
//    }
//
//    public void setZT196(String ZT196) {
//        this.ZT196 = ZT196;
//    }
//
//    public String getZT191() {
//        return ZT191;
//    }
//
//    public void setZT191(String ZT191) {
//        this.ZT191 = ZT191;
//    }
//
//    public String getZT190() {
//        return ZT190;
//    }
//
//    public void setZT190(String ZT190) {
//        this.ZT190 = ZT190;
//    }
//
//    public String getZT19() {
//        return ZT19;
//    }
//
//    public void setZT19(String ZT19) {
//        this.ZT19 = ZT19;
//    }
//
//    public String getZT17() {
//        return ZT17;
//    }
//
//    public void setZT17(String ZT17) {
//        this.ZT17 = ZT17;
//    }
//
//    public String getZT18() {
//        return ZT18;
//    }
//
//    public void setZT18(String ZT18) {
//        this.ZT18 = ZT18;
//    }
//
//    public String getZT22() {
//        return ZT22;
//    }
//
//    public void setZT22(String ZT22) {
//        this.ZT22 = ZT22;
//    }
//
//    public String getZT23() {
//        return ZT23;
//    }
//
//    public void setZT23(String ZT23) {
//        this.ZT23 = ZT23;
//    }
//
//    public String getZT20() {
//        return ZT20;
//    }
//
//    public void setZT20(String ZT20) {
//        this.ZT20 = ZT20;
//    }
//
//    public String getZT21() {
//        return ZT21;
//    }
//
//    public void setZT21(String ZT21) {
//        this.ZT21 = ZT21;
//    }
//
//    public String getZT26() {
//        return ZT26;
//    }
//
//    public void setZT26(String ZT26) {
//        this.ZT26 = ZT26;
//    }
//
//    public String getZT27() {
//        return ZT27;
//    }
//
//    public void setZT27(String ZT27) {
//        this.ZT27 = ZT27;
//    }
//
//    public String getZT24() {
//        return ZT24;
//    }
//
//    public void setZT24(String ZT24) {
//        this.ZT24 = ZT24;
//    }
//
//    public String getZT25() {
//        return ZT25;
//    }
//
//    public void setZT25(String ZT25) {
//        this.ZT25 = ZT25;
//    }
//
//    public String getZT11() {
//        return ZT11;
//    }
//
//    public void setZT11(String ZT11) {
//        this.ZT11 = ZT11;
//    }
//
//    public String getZT12() {
//        return ZT12;
//    }
//
//    public void setZT12(String ZT12) {
//        this.ZT12 = ZT12;
//    }
//
//    public String getZT10() {
//        return ZT10;
//    }
//
//    public void setZT10(String ZT10) {
//        this.ZT10 = ZT10;
//    }
//
//    public String getZT15() {
//        return ZT15;
//    }
//
//    public void setZT15(String ZT15) {
//        this.ZT15 = ZT15;
//    }
//
//    public String getZT16() {
//        return ZT16;
//    }
//
//    public void setZT16(String ZT16) {
//        this.ZT16 = ZT16;
//    }
//
//    public String getZT13() {
//        return ZT13;
//    }
//
//    public void setZT13(String ZT13) {
//        this.ZT13 = ZT13;
//    }
//
//    public String getZT14() {
//        return ZT14;
//    }
//
//    public void setZT14(String ZT14) {
//        this.ZT14 = ZT14;
//    }
//
//    public String getZT39() {
//        return ZT39;
//    }
//
//    public void setZT39(String ZT39) {
//        this.ZT39 = ZT39;
//    }
//
//    public String getZT44() {
//        return ZT44;
//    }
//
//    public void setZT44(String ZT44) {
//        this.ZT44 = ZT44;
//    }
//
//    public String getZT45() {
//        return ZT45;
//    }
//
//    public void setZT45(String ZT45) {
//        this.ZT45 = ZT45;
//    }
//
//    public String getZT42() {
//        return ZT42;
//    }
//
//    public void setZT42(String ZT42) {
//        this.ZT42 = ZT42;
//    }
//
//    public String getZT43() {
//        return ZT43;
//    }
//
//    public void setZT43(String ZT43) {
//        this.ZT43 = ZT43;
//    }
//
//    public String getZT48() {
//        return ZT48;
//    }
//
//    public void setZT48(String ZT48) {
//        this.ZT48 = ZT48;
//    }
//
//    public String getZT49() {
//        return ZT49;
//    }
//
//    public void setZT49(String ZT49) {
//        this.ZT49 = ZT49;
//    }
//
//    public String getZT46() {
//        return ZT46;
//    }
//
//    public void setZT46(String ZT46) {
//        this.ZT46 = ZT46;
//    }
//
//    public String getZT47() {
//        return ZT47;
//    }
//
//    public void setZT47(String ZT47) {
//        this.ZT47 = ZT47;
//    }
//
//    public String getMACHINE_ID() {
//        return MACHINE_ID;
//    }
//
//    public void setMACHINE_ID(String MACHINE_ID) {
//        this.MACHINE_ID = MACHINE_ID;
//    }
//
//    public String getZT40() {
//        return ZT40;
//    }
//
//    public void setZT40(String ZT40) {
//        this.ZT40 = ZT40;
//    }
//
//    public String getZT41() {
//        return ZT41;
//    }
//
//    public void setZT41(String ZT41) {
//        this.ZT41 = ZT41;
//    }
//
//    public String getZT203() {
//        return ZT203;
//    }
//
//    public void setZT203(String ZT203) {
//        this.ZT203 = ZT203;
//    }
//
//    public String getZT202() {
//        return ZT202;
//    }
//
//    public void setZT202(String ZT202) {
//        this.ZT202 = ZT202;
//    }
//
//    public String getZT201() {
//        return ZT201;
//    }
//
//    public void setZT201(String ZT201) {
//        this.ZT201 = ZT201;
//    }
//
//    public String getZT200() {
//        return ZT200;
//    }
//
//    public void setZT200(String ZT200) {
//        this.ZT200 = ZT200;
//    }
//
//    public String getZT207() {
//        return ZT207;
//    }
//
//    public void setZT207(String ZT207) {
//        this.ZT207 = ZT207;
//    }
//
//    public String getZT206() {
//        return ZT206;
//    }
//
//    public void setZT206(String ZT206) {
//        this.ZT206 = ZT206;
//    }
//
//    public String getZT205() {
//        return ZT205;
//    }
//
//    public void setZT205(String ZT205) {
//        this.ZT205 = ZT205;
//    }
//
//    public String getZT204() {
//        return ZT204;
//    }
//
//    public void setZT204(String ZT204) {
//        this.ZT204 = ZT204;
//    }
//
//    public String getZT28() {
//        return ZT28;
//    }
//
//    public void setZT28(String ZT28) {
//        this.ZT28 = ZT28;
//    }
//
//    public String getRECEIVE_TIME() {
//        return RECEIVE_TIME;
//    }
//
//    public void setRECEIVE_TIME(String RECEIVE_TIME) {
//        this.RECEIVE_TIME = RECEIVE_TIME;
//    }
//
//    public String getZT29() {
//        return ZT29;
//    }
//
//    public void setZT29(String ZT29) {
//        this.ZT29 = ZT29;
//    }
//
//    public String getZT33() {
//        return ZT33;
//    }
//
//    public void setZT33(String ZT33) {
//        this.ZT33 = ZT33;
//    }
//
//    public String getZT34() {
//        return ZT34;
//    }
//
//    public void setZT34(String ZT34) {
//        this.ZT34 = ZT34;
//    }
//
//    public String getZT31() {
//        return ZT31;
//    }
//
//    public void setZT31(String ZT31) {
//        this.ZT31 = ZT31;
//    }
//
//    public String getZT32() {
//        return ZT32;
//    }
//
//    public void setZT32(String ZT32) {
//        this.ZT32 = ZT32;
//    }
//
//    public String getZT37() {
//        return ZT37;
//    }
//
//    public void setZT37(String ZT37) {
//        this.ZT37 = ZT37;
//    }
//
//    public String getZT38() {
//        return ZT38;
//    }
//
//    public void setZT38(String ZT38) {
//        this.ZT38 = ZT38;
//    }
//
//    public String getZT35() {
//        return ZT35;
//    }
//
//    public void setZT35(String ZT35) {
//        this.ZT35 = ZT35;
//    }
//
//    public String getZT36() {
//        return ZT36;
//    }
//
//    public void setZT36(String ZT36) {
//        this.ZT36 = ZT36;
//    }
//
//    public String getZT30() {
//        return ZT30;
//    }
//
//    public void setZT30(String ZT30) {
//        this.ZT30 = ZT30;
//    }
//
//    public String getZT210() {
//        return ZT210;
//    }
//
//    public void setZT210(String ZT210) {
//        this.ZT210 = ZT210;
//    }
//
//    public String getZT66() {
//        return ZT66;
//    }
//
//    public void setZT66(String ZT66) {
//        this.ZT66 = ZT66;
//    }
//
//    public String getZT67() {
//        return ZT67;
//    }
//
//    public void setZT67(String ZT67) {
//        this.ZT67 = ZT67;
//    }
//
//    public String getZT64() {
//        return ZT64;
//    }
//
//    public void setZT64(String ZT64) {
//        this.ZT64 = ZT64;
//    }
//
//    public String getZT65() {
//        return ZT65;
//    }
//
//    public void setZT65(String ZT65) {
//        this.ZT65 = ZT65;
//    }
//
//    public String getZT68() {
//        return ZT68;
//    }
//
//    public void setZT68(String ZT68) {
//        this.ZT68 = ZT68;
//    }
//
//    public String getZT69() {
//        return ZT69;
//    }
//
//    public void setZT69(String ZT69) {
//        this.ZT69 = ZT69;
//    }
//
//    public String getZT209() {
//        return ZT209;
//    }
//
//    public void setZT209(String ZT209) {
//        this.ZT209 = ZT209;
//    }
//
//    public String getZT208() {
//        return ZT208;
//    }
//
//    public void setZT208(String ZT208) {
//        this.ZT208 = ZT208;
//    }
//
//    public String getZT62() {
//        return ZT62;
//    }
//
//    public void setZT62(String ZT62) {
//        this.ZT62 = ZT62;
//    }
//
//    public String getZT63() {
//        return ZT63;
//    }
//
//    public void setZT63(String ZT63) {
//        this.ZT63 = ZT63;
//    }
//
//    public String getZT60() {
//        return ZT60;
//    }
//
//    public void setZT60(String ZT60) {
//        this.ZT60 = ZT60;
//    }
//
//    public String getZT61() {
//        return ZT61;
//    }
//
//    public void setZT61(String ZT61) {
//        this.ZT61 = ZT61;
//    }
//
//    public String getZT104() {
//        return ZT104;
//    }
//
//    public void setZT104(String ZT104) {
//        this.ZT104 = ZT104;
//    }
//
//    public String getZT103() {
//        return ZT103;
//    }
//
//    public void setZT103(String ZT103) {
//        this.ZT103 = ZT103;
//    }
//
//    public String getZT102() {
//        return ZT102;
//    }
//
//    public void setZT102(String ZT102) {
//        this.ZT102 = ZT102;
//    }
//
//    public String getZT101() {
//        return ZT101;
//    }
//
//    public void setZT101(String ZT101) {
//        this.ZT101 = ZT101;
//    }
//
//    public String getZT108() {
//        return ZT108;
//    }
//
//    public void setZT108(String ZT108) {
//        this.ZT108 = ZT108;
//    }
//
//    public String getZT107() {
//        return ZT107;
//    }
//
//    public void setZT107(String ZT107) {
//        this.ZT107 = ZT107;
//    }
//
//    public String getZT106() {
//        return ZT106;
//    }
//
//    public void setZT106(String ZT106) {
//        this.ZT106 = ZT106;
//    }
//
//    public String getZT105() {
//        return ZT105;
//    }
//
//    public void setZT105(String ZT105) {
//        this.ZT105 = ZT105;
//    }
//
//    public String getZT100() {
//        return ZT100;
//    }
//
//    public void setZT100(String ZT100) {
//        this.ZT100 = ZT100;
//    }
//
//    public String getZT55() {
//        return ZT55;
//    }
//
//    public void setZT55(String ZT55) {
//        this.ZT55 = ZT55;
//    }
//
//    public String getZT56() {
//        return ZT56;
//    }
//
//    public void setZT56(String ZT56) {
//        this.ZT56 = ZT56;
//    }
//
//    public String getZT53() {
//        return ZT53;
//    }
//
//    public void setZT53(String ZT53) {
//        this.ZT53 = ZT53;
//    }
//
//    public String getZT54() {
//        return ZT54;
//    }
//
//    public void setZT54(String ZT54) {
//        this.ZT54 = ZT54;
//    }
//
//    public String getZT59() {
//        return ZT59;
//    }
//
//    public void setZT59(String ZT59) {
//        this.ZT59 = ZT59;
//    }
//
//    public String getZT57() {
//        return ZT57;
//    }
//
//    public void setZT57(String ZT57) {
//        this.ZT57 = ZT57;
//    }
//
//    public String getZT58() {
//        return ZT58;
//    }
//
//    public void setZT58(String ZT58) {
//        this.ZT58 = ZT58;
//    }
//
//    public String getZT51() {
//        return ZT51;
//    }
//
//    public void setZT51(String ZT51) {
//        this.ZT51 = ZT51;
//    }
//
//    public String getZT52() {
//        return ZT52;
//    }
//
//    public void setZT52(String ZT52) {
//        this.ZT52 = ZT52;
//    }
//
//    public String getZT50() {
//        return ZT50;
//    }
//
//    public void setZT50(String ZT50) {
//        this.ZT50 = ZT50;
//    }
//
//    public String getZT115() {
//        return ZT115;
//    }
//
//    public void setZT115(String ZT115) {
//        this.ZT115 = ZT115;
//    }
//
//    public String getZT114() {
//        return ZT114;
//    }
//
//    public void setZT114(String ZT114) {
//        this.ZT114 = ZT114;
//    }
//
//    public String getZT113() {
//        return ZT113;
//    }
//
//    public void setZT113(String ZT113) {
//        this.ZT113 = ZT113;
//    }
//
//    public String getZT112() {
//        return ZT112;
//    }
//
//    public void setZT112(String ZT112) {
//        this.ZT112 = ZT112;
//    }
//
//    public String getZT119() {
//        return ZT119;
//    }
//
//    public void setZT119(String ZT119) {
//        this.ZT119 = ZT119;
//    }
//
//    public String getZT118() {
//        return ZT118;
//    }
//
//    public void setZT118(String ZT118) {
//        this.ZT118 = ZT118;
//    }
//
//    public String getZT117() {
//        return ZT117;
//    }
//
//    public void setZT117(String ZT117) {
//        this.ZT117 = ZT117;
//    }
//
//    public String getZT116() {
//        return ZT116;
//    }
//
//    public void setZT116(String ZT116) {
//        this.ZT116 = ZT116;
//    }
//
//    public String getZT111() {
//        return ZT111;
//    }
//
//    public void setZT111(String ZT111) {
//        this.ZT111 = ZT111;
//    }
//
//    public String getZT110() {
//        return ZT110;
//    }
//
//    public void setZT110(String ZT110) {
//        this.ZT110 = ZT110;
//    }
//
//    public String getZT88() {
//        return ZT88;
//    }
//
//    public void setZT88(String ZT88) {
//        this.ZT88 = ZT88;
//    }
//
//    public String getZT89() {
//        return ZT89;
//    }
//
//    public void setZT89(String ZT89) {
//        this.ZT89 = ZT89;
//    }
//
//    public String getZT86() {
//        return ZT86;
//    }
//
//    public void setZT86(String ZT86) {
//        this.ZT86 = ZT86;
//    }
//
//    public String getZT87() {
//        return ZT87;
//    }
//
//    public void setZT87(String ZT87) {
//        this.ZT87 = ZT87;
//    }
//
//    public String getCOLLECT_TYPE() {
//        return COLLECT_TYPE;
//    }
//
//    public void setCOLLECT_TYPE(String COLLECT_TYPE) {
//        this.COLLECT_TYPE = COLLECT_TYPE;
//    }
//
//    public String getZT80() {
//        return ZT80;
//    }
//
//    public void setZT80(String ZT80) {
//        this.ZT80 = ZT80;
//    }
//
//    public String getZT81() {
//        return ZT81;
//    }
//
//    public void setZT81(String ZT81) {
//        this.ZT81 = ZT81;
//    }
//
//    public String getZT109() {
//        return ZT109;
//    }
//
//    public void setZT109(String ZT109) {
//        this.ZT109 = ZT109;
//    }
//
//    public String getZT84() {
//        return ZT84;
//    }
//
//    public void setZT84(String ZT84) {
//        this.ZT84 = ZT84;
//    }
//
//    public String getZT85() {
//        return ZT85;
//    }
//
//    public void setZT85(String ZT85) {
//        this.ZT85 = ZT85;
//    }
//
//    public String getZT82() {
//        return ZT82;
//    }
//
//    public void setZT82(String ZT82) {
//        this.ZT82 = ZT82;
//    }
//
//    public String getGZ2() {
//        return GZ2;
//    }
//
//    public void setGZ2(String GZ2) {
//        this.GZ2 = GZ2;
//    }
//
//    public String getZT83() {
//        return ZT83;
//    }
//
//    public void setZT83(String ZT83) {
//        this.ZT83 = ZT83;
//    }
//
//    public String getGZ1() {
//        return GZ1;
//    }
//
//    public void setGZ1(String GZ1) {
//        this.GZ1 = GZ1;
//    }
//
//    public String getZT126() {
//        return ZT126;
//    }
//
//    public void setZT126(String ZT126) {
//        this.ZT126 = ZT126;
//    }
//
//    public String getGZ4() {
//        return GZ4;
//    }
//
//    public void setGZ4(String GZ4) {
//        this.GZ4 = GZ4;
//    }
//
//    public String getZT125() {
//        return ZT125;
//    }
//
//    public void setZT125(String ZT125) {
//        this.ZT125 = ZT125;
//    }
//
//    public String getGZ3() {
//        return GZ3;
//    }
//
//    public void setGZ3(String GZ3) {
//        this.GZ3 = GZ3;
//    }
//
//    public String getZT124() {
//        return ZT124;
//    }
//
//    public void setZT124(String ZT124) {
//        this.ZT124 = ZT124;
//    }
//
//    public String getGZ6() {
//        return GZ6;
//    }
//
//    public void setGZ6(String GZ6) {
//        this.GZ6 = GZ6;
//    }
//
//    public String getZT123() {
//        return ZT123;
//    }
//
//    public void setZT123(String ZT123) {
//        this.ZT123 = ZT123;
//    }
//
//    public String getGZ5() {
//        return GZ5;
//    }
//
//    public void setGZ5(String GZ5) {
//        this.GZ5 = GZ5;
//    }
//
//    public String getGZ8() {
//        return GZ8;
//    }
//
//    public void setGZ8(String GZ8) {
//        this.GZ8 = GZ8;
//    }
//
//    public String getZT129() {
//        return ZT129;
//    }
//
//    public void setZT129(String ZT129) {
//        this.ZT129 = ZT129;
//    }
//
//    public String getGZ7() {
//        return GZ7;
//    }
//
//    public void setGZ7(String GZ7) {
//        this.GZ7 = GZ7;
//    }
//
//    public String getZT128() {
//        return ZT128;
//    }
//
//    public void setZT128(String ZT128) {
//        this.ZT128 = ZT128;
//    }
//
//    public String getZT127() {
//        return ZT127;
//    }
//
//    public void setZT127(String ZT127) {
//        this.ZT127 = ZT127;
//    }
//
//    public String getGZ9() {
//        return GZ9;
//    }
//
//    public void setGZ9(String GZ9) {
//        this.GZ9 = GZ9;
//    }
//
//    public String getZT122() {
//        return ZT122;
//    }
//
//    public void setZT122(String ZT122) {
//        this.ZT122 = ZT122;
//    }
//
//    public String getZT121() {
//        return ZT121;
//    }
//
//    public void setZT121(String ZT121) {
//        this.ZT121 = ZT121;
//    }
//
//    public String getZT120() {
//        return ZT120;
//    }
//
//    public void setZT120(String ZT120) {
//        this.ZT120 = ZT120;
//    }
//
//    public String getZT77() {
//        return ZT77;
//    }
//
//    public void setZT77(String ZT77) {
//        this.ZT77 = ZT77;
//    }
//
//    public String getZT78() {
//        return ZT78;
//    }
//
//    public void setZT78(String ZT78) {
//        this.ZT78 = ZT78;
//    }
//
//    public String getZT75() {
//        return ZT75;
//    }
//
//    public void setZT75(String ZT75) {
//        this.ZT75 = ZT75;
//    }
//
//    public String getZT76() {
//        return ZT76;
//    }
//
//    public void setZT76(String ZT76) {
//        this.ZT76 = ZT76;
//    }
//
//    public String getZT79() {
//        return ZT79;
//    }
//
//    public void setZT79(String ZT79) {
//        this.ZT79 = ZT79;
//    }
//
//    public String getZT70() {
//        return ZT70;
//    }
//
//    public void setZT70(String ZT70) {
//        this.ZT70 = ZT70;
//    }
//
//    public String getZT73() {
//        return ZT73;
//    }
//
//    public void setZT73(String ZT73) {
//        this.ZT73 = ZT73;
//    }
//
//    public String getZT74() {
//        return ZT74;
//    }
//
//    public void setZT74(String ZT74) {
//        this.ZT74 = ZT74;
//    }
//
//    public String getZT71() {
//        return ZT71;
//    }
//
//    public void setZT71(String ZT71) {
//        this.ZT71 = ZT71;
//    }
//
//    public String getZT72() {
//        return ZT72;
//    }
//
//    public void setZT72(String ZT72) {
//        this.ZT72 = ZT72;
//    }
//
//    public String getZT137() {
//        return ZT137;
//    }
//
//    public void setZT137(String ZT137) {
//        this.ZT137 = ZT137;
//    }
//
//    public String getZT136() {
//        return ZT136;
//    }
//
//    public void setZT136(String ZT136) {
//        this.ZT136 = ZT136;
//    }
//
//    public String getZT135() {
//        return ZT135;
//    }
//
//    public void setZT135(String ZT135) {
//        this.ZT135 = ZT135;
//    }
//
//    public String getZT134() {
//        return ZT134;
//    }
//
//    public void setZT134(String ZT134) {
//        this.ZT134 = ZT134;
//    }
//
//    public String getZT139() {
//        return ZT139;
//    }
//
//    public void setZT139(String ZT139) {
//        this.ZT139 = ZT139;
//    }
//
//    public String getZT138() {
//        return ZT138;
//    }
//
//    public void setZT138(String ZT138) {
//        this.ZT138 = ZT138;
//    }
//
//    public String getZT133() {
//        return ZT133;
//    }
//
//    public void setZT133(String ZT133) {
//        this.ZT133 = ZT133;
//    }
//
//    public String getZT132() {
//        return ZT132;
//    }
//
//    public void setZT132(String ZT132) {
//        this.ZT132 = ZT132;
//    }
//
//    public String getZT131() {
//        return ZT131;
//    }
//
//    public void setZT131(String ZT131) {
//        this.ZT131 = ZT131;
//    }
//
//    public String getZT130() {
//        return ZT130;
//    }
//
//    public void setZT130(String ZT130) {
//        this.ZT130 = ZT130;
//    }
//
//    public String getZT148() {
//        return ZT148;
//    }
//
//    public void setZT148(String ZT148) {
//        this.ZT148 = ZT148;
//    }
//
//    public String getZT147() {
//        return ZT147;
//    }
//
//    public void setZT147(String ZT147) {
//        this.ZT147 = ZT147;
//    }
//
//    public String getZT146() {
//        return ZT146;
//    }
//
//    public void setZT146(String ZT146) {
//        this.ZT146 = ZT146;
//    }
//
//    public String getZT145() {
//        return ZT145;
//    }
//
//    public void setZT145(String ZT145) {
//        this.ZT145 = ZT145;
//    }
//
//    public String getZT149() {
//        return ZT149;
//    }
//
//    public void setZT149(String ZT149) {
//        this.ZT149 = ZT149;
//    }
//
//    public String getZT140() {
//        return ZT140;
//    }
//
//    public void setZT140(String ZT140) {
//        this.ZT140 = ZT140;
//    }
//
//    public String getZT144() {
//        return ZT144;
//    }
//
//    public void setZT144(String ZT144) {
//        this.ZT144 = ZT144;
//    }
//
//    public String getZT143() {
//        return ZT143;
//    }
//
//    public void setZT143(String ZT143) {
//        this.ZT143 = ZT143;
//    }
//
//    public String getZT142() {
//        return ZT142;
//    }
//
//    public void setZT142(String ZT142) {
//        this.ZT142 = ZT142;
//    }
//
//    public String getZT141() {
//        return ZT141;
//    }
//
//    public void setZT141(String ZT141) {
//        this.ZT141 = ZT141;
//    }
//
//    public String getZT99() {
//        return ZT99;
//    }
//
//    public void setZT99(String ZT99) {
//        this.ZT99 = ZT99;
//    }
//
//    public String getZT97() {
//        return ZT97;
//    }
//
//    public void setZT97(String ZT97) {
//        this.ZT97 = ZT97;
//    }
//
//    public String getZT98() {
//        return ZT98;
//    }
//
//    public void setZT98(String ZT98) {
//        this.ZT98 = ZT98;
//    }
//
//    public String getZT91() {
//        return ZT91;
//    }
//
//    public void setZT91(String ZT91) {
//        this.ZT91 = ZT91;
//    }
//
//    public String getZT92() {
//        return ZT92;
//    }
//
//    public void setZT92(String ZT92) {
//        this.ZT92 = ZT92;
//    }
//
//    public String getZT90() {
//        return ZT90;
//    }
//
//    public void setZT90(String ZT90) {
//        this.ZT90 = ZT90;
//    }
//
//    public String getZT95() {
//        return ZT95;
//    }
//
//    public void setZT95(String ZT95) {
//        this.ZT95 = ZT95;
//    }
//
//    public String getZT96() {
//        return ZT96;
//    }
//
//    public void setZT96(String ZT96) {
//        this.ZT96 = ZT96;
//    }
//
//    public String getZT93() {
//        return ZT93;
//    }
//
//    public void setZT93(String ZT93) {
//        this.ZT93 = ZT93;
//    }
//
//    public String getZT94() {
//        return ZT94;
//    }
//
//    public void setZT94(String ZT94) {
//        this.ZT94 = ZT94;
//    }
//
//    public MachineInfo() {
//    }
//
//    @Override
//    public String toString() {
//        return "MachineInfo{" +
//                "ZT159='" + ZT159 + '\'' +
//                ", ZT158='" + ZT158 + '\'' +
//                ", ZT156='" + ZT156 + '\'' +
//                ", ZT150='" + ZT150 + '\'' +
//                ", ZT155='" + ZT155 + '\'' +
//                ", ZT153='" + ZT153 + '\'' +
//                ", ZT152='" + ZT152 + '\'' +
//                ", ZT167='" + ZT167 + '\'' +
//                ", ZT162='" + ZT162 + '\'' +
//                ", COLLECT_TIME='" + COLLECT_TIME + '\'' +
//                ", CH='" + CH + '\'' +
//                ", MACHINE_ID='" + MACHINE_ID + '\'' +
//                '}';
//    }
//}
