//package com.thit.tibdm.bom.entity;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
///**
// * 实体类
// *
// * @author wanghaoqiang
// * @version 1.0
// * @create 2017-05-25 13:14
// **/
//public class JsonFormat {
//    public final static Logger logger = LoggerFactory.getLogger(JsonFormat.class);
//
//    /**
//     * ProtocolName : TestProtocol--state
//     * MessageType : state
//     * Length : 926
//     * Variable : [{"SerialNumber":"1","UniqueCode":"ZT1","Name":"列车编号","ByteOffset":"0","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"329","Realmax":"336"},{"SerialNumber":"2","UniqueCode":"ZT2","Name":"当前站代码","ByteOffset":"2","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"100"},{"SerialNumber":"3","UniqueCode":"ZT3","Name":"终点站代码","ByteOffset":"3","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"100"},{"SerialNumber":"4","UniqueCode":"ZT4","Name":"下一站代码","ByteOffset":"4","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"100"},{"SerialNumber":"5","UniqueCode":"ZT5","Name":"列车速度","ByteOffset":"5","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"12000"},{"SerialNumber":"6","UniqueCode":"ZT6","Name":"控制模式","ByteOffset":"7","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"7"},{"SerialNumber":"7","UniqueCode":"ZT7","Name":"开门模式","ByteOffset":"8","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"4"},{"SerialNumber":"8","UniqueCode":"ZT8","Name":"报站模式","ByteOffset":"9","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"4"},{"SerialNumber":"9","UniqueCode":"ZT9","Name":"司控器级位","ByteOffset":"10","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"100"},{"SerialNumber":"10","UniqueCode":"ZT10","Name":"主风压力","ByteOffset":"12","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"0","Realmax":"120"},{"SerialNumber":"11","UniqueCode":"ZT11","Name":"线网电压","ByteOffset":"13","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"2500"},{"SerialNumber":"12","UniqueCode":"ZT12","Name":"蓄电池电压","ByteOffset":"15","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"130"},{"SerialNumber":"13","UniqueCode":"ZT13","Name":"TC1车载重量","ByteOffset":"17","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50000"},{"SerialNumber":"14","UniqueCode":"ZT14","Name":"TC2车载重量","ByteOffset":"19","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50000"},{"SerialNumber":"15","UniqueCode":"ZT15","Name":"MP1车载重量","ByteOffset":"21","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50000"},{"SerialNumber":"16","UniqueCode":"ZT16","Name":"MP2车载重量","ByteOffset":"23","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50000"},{"SerialNumber":"17","UniqueCode":"ZT17","Name":"M1车载重量","ByteOffset":"25","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50000"},{"SerialNumber":"18","UniqueCode":"ZT18","Name":"M2车载重量","ByteOffset":"27","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50000"},{"SerialNumber":"19","UniqueCode":"ZT19","Name":"TC1车客室温度值","ByteOffset":"29","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50"},{"SerialNumber":"20","UniqueCode":"ZT20","Name":"MP1车客室温度值","ByteOffset":"31","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50"},{"SerialNumber":"21","UniqueCode":"ZT21","Name":"M1车客室温度值","ByteOffset":"33","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50"},{"SerialNumber":"22","UniqueCode":"ZT22","Name":"M2车客室温度值","ByteOffset":"35","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50"},{"SerialNumber":"23","UniqueCode":"ZT23","Name":"MP2车客室温度值","ByteOffset":"37","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50"},{"SerialNumber":"24","UniqueCode":"ZT24","Name":"TC2车客室温度值","ByteOffset":"39","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"50"},{"SerialNumber":"25","UniqueCode":"ZT25","Name":"MP1车受电弓状态","ByteOffset":"41","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"0","Realmax":"3"},{"SerialNumber":"26","UniqueCode":"ZT26","Name":"MP2车受电弓状态","ByteOffset":"42","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"0","Realmax":"3"},{"SerialNumber":"27","UniqueCode":"ZT27","Name":"MP1车HSCB状态","ByteOffset":"43","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"0","Realmax":"4"},{"SerialNumber":"28","UniqueCode":"ZT28","Name":"M1车HSCB状态","ByteOffset":"44","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"0","Realmax":"4"},{"SerialNumber":"29","UniqueCode":"ZT29","Name":"M2车HSCB状态","ByteOffset":"45","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"0","Realmax":"4"},{"SerialNumber":"30","UniqueCode":"ZT30","Name":"MP2车HSCB状态","ByteOffset":"46","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"0","Realmax":"4"},{"SerialNumber":"31","UniqueCode":"ZT31","Name":"MP1车牵引系统状态","ByteOffset":"47","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"6"},{"SerialNumber":"32","UniqueCode":"ZT32","Name":"M1车牵引系统状态","ByteOffset":"48","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"6"},{"SerialNumber":"33","UniqueCode":"ZT33","Name":"M2车牵引系统状态","ByteOffset":"49","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"6"},{"SerialNumber":"34","UniqueCode":"ZT34","Name":"MP2车牵引系统状态","ByteOffset":"50","ByteLength":"1","BitOffset":"0","BitLength":"8","Realmin":"1","Realmax":"6"},{"SerialNumber":"35","UniqueCode":"ZT35","Name":"累计运行里程","ByteOffset":"51","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"36","UniqueCode":"ZT36","Name":"辅助能耗","ByteOffset":"55","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"37","UniqueCode":"ZT37","Name":"制动电阻能耗","ByteOffset":"59","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"38","UniqueCode":"ZT38","Name":"牵引能耗","ByteOffset":"63","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"39","UniqueCode":"ZT39","Name":"再生能耗","ByteOffset":"67","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"40","UniqueCode":"ZT40","Name":"TCMS运行时间","ByteOffset":"71","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"41","UniqueCode":"ZT41","Name":"空压机1工作时间","ByteOffset":"75","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"42","UniqueCode":"ZT42","Name":"空压机2工作时间","ByteOffset":"79","ByteLength":"4","BitOffset":"0","BitLength":"32","Realmin":"0","Realmax":"1294967295"},{"SerialNumber":"43","UniqueCode":"ZT43","Name":"Tc1车CVS1运行时间","ByteOffset":"83","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"65535"},{"SerialNumber":"44","UniqueCode":"ZT44","Name":"M1车CVS2运行时间","ByteOffset":"85","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"65535"},{"SerialNumber":"45","UniqueCode":"ZT45","Name":"Tc2车CVS3运行时间","ByteOffset":"87","ByteLength":"2","BitOffset":"0","BitLength":"16","Realmin":"0","Realmax":"65535"},{"SerialNumber":"46","UniqueCode":"GZ1","Name":"逆变器模块永久故障","ByteOffset":"89","ByteLength":"1","BitOffset":"0","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"47","UniqueCode":"GZ2","Name":"AC输出模块相接地故障","ByteOffset":"89","ByteLength":"1","BitOffset":"1","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"48","UniqueCode":"GZ3","Name":"两个空压机都失效","ByteOffset":"89","ByteLength":"1","BitOffset":"2","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"49","UniqueCode":"GZ4","Name":"牵引系统少于1/2模式","ByteOffset":"89","ByteLength":"1","BitOffset":"3","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"50","UniqueCode":"GZ5","Name":"Tc1车1R传感器温度报警","ByteOffset":"89","ByteLength":"1","BitOffset":"4","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"51","UniqueCode":"GZ6","Name":"MPU通信中断","ByteOffset":"89","ByteLength":"1","BitOffset":"5","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"52","UniqueCode":"GZ7","Name":"两个司机室都被占用","ByteOffset":"89","ByteLength":"1","BitOffset":"6","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"53","UniqueCode":"GZ8","Name":"Tc2车门2R开门障碍物探测","ByteOffset":"89","ByteLength":"1","BitOffset":"7","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"54","UniqueCode":"GZ9","Name":"Mp2车门2R开门超时","ByteOffset":"90","ByteLength":"1","BitOffset":"0","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"55","UniqueCode":"GZ10","Name":"Tc1车门5L蜂鸣器供电故障","ByteOffset":"90","ByteLength":"1","BitOffset":"1","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"56","UniqueCode":"GZ11","Name":"Mp2车门8R外部指示灯供电故障","ByteOffset":"90","ByteLength":"1","BitOffset":"2","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"57","UniqueCode":"GZ12","Name":"M1车门1L-3L间4852路通信中断","ByteOffset":"90","ByteLength":"1","BitOffset":"3","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"58","UniqueCode":"GZ13","Name":"M2车2路摄像头故障","ByteOffset":"90","ByteLength":"1","BitOffset":"4","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"59","UniqueCode":"GZ14","Name":"探测器10离线","ByteOffset":"90","ByteLength":"1","BitOffset":"5","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"60","UniqueCode":"GZ15","Name":"Tc2车4R传感器故障","ByteOffset":"90","ByteLength":"1","BitOffset":"6","BitLength":"1","Realmin":"0","Realmax":"1"},{"SerialNumber":"61","UniqueCode":"GZ16","Name":"转向架1常用制动不一致故障","ByteOffset":"90","ByteLength":"1","BitOffset":"7","BitLength":"1","Realmin":"0","Realmax":"1"}]
//     * WarningProtocol : [{"SerialNumber":9012,"Name":"所有车厢客室温度都大于30度","Timespan":3600,"Frequency":5,"TimeDuration":3,"Param":["ZT19","ZT20","ZT21","ZT22","ZT23","ZT24"],"Jexl":"ZT19*0.1>30&&ZT20*0.1>30&&ZT21*0.1>30&&ZT22*0.1>30&&ZT23*0.1>30&&ZT24*0.1>30"}]
//     */
//
//    private String ProtocolName;
//    private String MessageType;
//    private int Length;
//    private List<VariableBean> Variable;
//    private List<WarningProtocolBean> WarningProtocol;
//
//    public String getProtocolName() {
//        return ProtocolName;
//    }
//
//    public void setProtocolName(String ProtocolName) {
//        this.ProtocolName = ProtocolName;
//    }
//
//    public String getMessageType() {
//        return MessageType;
//    }
//
//    public void setMessageType(String MessageType) {
//        this.MessageType = MessageType;
//    }
//
//    public int getLength() {
//        return Length;
//    }
//
//    public void setLength(int Length) {
//        this.Length = Length;
//    }
//
//    public List<VariableBean> getVariable() {
//        return Variable;
//    }
//
//    public void setVariable(List<VariableBean> Variable) {
//        this.Variable = Variable;
//    }
//
//    public List<WarningProtocolBean> getWarningProtocol() {
//        return WarningProtocol;
//    }
//
//    public void setWarningProtocol(List<WarningProtocolBean> WarningProtocol) {
//        this.WarningProtocol = WarningProtocol;
//    }
//
//    public static class VariableBean {
//        /**
//         * SerialNumber : 1
//         * UniqueCode : ZT1
//         * Name : 列车编号
//         * ByteOffset : 0
//         * ByteLength : 2
//         * BitOffset : 0
//         * BitLength : 16
//         * Realmin : 329
//         * Realmax : 336
//         */
//
//        private String SerialNumber;
//        private String UniqueCode;
//        private String Name;
//        private String ByteOffset;
//        private String ByteLength;
//        private String BitOffset;
//        private String BitLength;
//        private String Realmin;
//        private String Realmax;
//
//        public String getSerialNumber() {
//            return SerialNumber;
//        }
//
//        public void setSerialNumber(String SerialNumber) {
//            this.SerialNumber = SerialNumber;
//        }
//
//        public String getUniqueCode() {
//            return UniqueCode;
//        }
//
//        public void setUniqueCode(String UniqueCode) {
//            this.UniqueCode = UniqueCode;
//        }
//
//        public String getName() {
//            return Name;
//        }
//
//        public void setName(String Name) {
//            this.Name = Name;
//        }
//
//        public String getByteOffset() {
//            return ByteOffset;
//        }
//
//        public void setByteOffset(String ByteOffset) {
//            this.ByteOffset = ByteOffset;
//        }
//
//        public String getByteLength() {
//            return ByteLength;
//        }
//
//        public void setByteLength(String ByteLength) {
//            this.ByteLength = ByteLength;
//        }
//
//        public String getBitOffset() {
//            return BitOffset;
//        }
//
//        public void setBitOffset(String BitOffset) {
//            this.BitOffset = BitOffset;
//        }
//
//        public String getBitLength() {
//            return BitLength;
//        }
//
//        public void setBitLength(String BitLength) {
//            this.BitLength = BitLength;
//        }
//
//        public String getRealmin() {
//            return Realmin;
//        }
//
//        public void setRealmin(String Realmin) {
//            this.Realmin = Realmin;
//        }
//
//        public String getRealmax() {
//            return Realmax;
//        }
//
//        public void setRealmax(String Realmax) {
//            this.Realmax = Realmax;
//        }
//    }
//
//    public static class WarningProtocolBean {
//        /**
//         * SerialNumber : 9012
//         * Name : 所有车厢客室温度都大于30度
//         * Timespan : 3600
//         * Frequency : 5
//         * TimeDuration : 3
//         * Param : ["ZT19","ZT20","ZT21","ZT22","ZT23","ZT24"]
//         * Jexl : ZT19*0.1>30&&ZT20*0.1>30&&ZT21*0.1>30&&ZT22*0.1>30&&ZT23*0.1>30&&ZT24*0.1>30
//         */
//
//        private int SerialNumber;
//        private String Name;
//        private int Timespan;
//        private int Frequency;
//        private int TimeDuration;
//        private String Jexl;
//        private List<String> Param;
//
//        public int getSerialNumber() {
//            return SerialNumber;
//        }
//
//        public void setSerialNumber(int SerialNumber) {
//            this.SerialNumber = SerialNumber;
//        }
//
//        public String getName() {
//            return Name;
//        }
//
//        public void setName(String Name) {
//            this.Name = Name;
//        }
//
//        public int getTimespan() {
//            return Timespan;
//        }
//
//        public void setTimespan(int Timespan) {
//            this.Timespan = Timespan;
//        }
//
//        public int getFrequency() {
//            return Frequency;
//        }
//
//        public void setFrequency(int Frequency) {
//            this.Frequency = Frequency;
//        }
//
//        public int getTimeDuration() {
//            return TimeDuration;
//        }
//
//        public void setTimeDuration(int TimeDuration) {
//            this.TimeDuration = TimeDuration;
//        }
//
//        public String getJexl() {
//            return Jexl;
//        }
//
//        public void setJexl(String Jexl) {
//            this.Jexl = Jexl;
//        }
//
//        public List<String> getParam() {
//            return Param;
//        }
//
//        public void setParam(List<String> Param) {
//            this.Param = Param;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "JsonFormat{" +
//               "ProtocolName='" + ProtocolName + '\'' +
//               ", MessageType='" + MessageType + '\'' +
//               ", Length=" + Length +
//               ", Variable=" + Variable.toString() +
//               ", WarningProtocol=" + WarningProtocol.toString() +
//               '}';
//    }
//}
