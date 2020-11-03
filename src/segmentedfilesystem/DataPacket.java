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

    public byte getFileID(DataPacket data){ //returns the fileID of a data packet
        return data.fileID;
    }

    public boolean getLast(DataPacket data){
        return data.last;
    }

    public int getPacketNumber(DataPacket data){
        return data.packetNumber;
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
