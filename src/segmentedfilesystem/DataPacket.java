package segmentedfilesystem;

import java.util.Arrays;

public class DataPacket {
    byte[] packet;
    byte fileID;
    int packetNumber;
    byte[] info;
    boolean last;

    public DataPacket(byte fileID, int packetNumber, byte[] info, boolean last, byte[] packet){
        this.fileID = fileID;
        this.packetNumber = packetNumber;
        this.info = Arrays.copyOfRange(packet,4,packet.length);
        this.last = last;
        this.packet = packet;
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
}
