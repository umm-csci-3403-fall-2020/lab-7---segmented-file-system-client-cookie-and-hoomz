package segmentedfilesystem;

import java.util.Arrays;

public class DataPacket {
    byte[] packet;
    byte fileID;
    int packetNumber;
    byte[] info;
    boolean last;

    public DataPacket(byte fileID, int packetNumber, byte[] info, boolean last){
        this.fileID = fileID;
        this.packetNumber = packetNumber;
        this.info = info;
        this.last = last;
    }

    public byte getFileID(){ //returns the fileID of a data packet
        return this.fileID;
    }

    public boolean getLast(){
        byte status = packet[0];
        if (status % 4 == 3) {
            last = true;
        }
        return last;
    }

    // public int getPacketNumber(){
    //     return this.packetNumber;
    // }
    public int getPacketNumber(){
        int packetNumber;
        int valOne = Byte.toUnsignedInt(packet[2]); //conversion since java supports signed ints (-128 to 127)
        int valTwo = Byte.toUnsignedInt(packet[3]);
        packetNumber = 256*valOne+valTwo;
        return packetNumber;
    }

    public byte[] getDataInfo(){
        return this.info;
    }

    // public int getPacketNum(byte[] packet){
    //     int valOne = Byte.toUnsignedInt(packet[2]); //conversion since java supports signed ints (-128 to 127)
    //     int valTwo = Byte.toUnsignedInt(packet[3]);
    //     packetNumber = 256*valOne+valTwo;
    //     return packetNumber;
    // }

    // public byte getFileID(byte[] packet){ //returns the fileID of a header packet
    //     byte fileID = packet[1];
    //     return fileID;
    // }

    // public byte[] getInfo(byte[] packet){ //the info of each data packet
    //     byte[] info = Arrays.copyOfRange(packet, 4, packet.length);
    //     return info;
    // }
}
