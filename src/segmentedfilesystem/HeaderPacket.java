package segmentedfilesystem;

import java.util.Arrays;

public class HeaderPacket {
    byte[] packet;
    // byte fileID = packet[1];
    byte fileID;
    byte[] fileName;  

    public HeaderPacket(byte fileID, byte[] fileName, byte[] packet){
        this.fileID = packet[1];
        this.fileName = Arrays.copyOfRange(packet, 2, packet.length);
        this.packet = packet;
    }

    public byte[] getFileName(){ //returns the fileName of a file given the header packet
        // byte[] file = Arrays.copyOfRange(packet, 2, packet.length);
        // return file;
        return this.fileName;
    }

    public byte getFileID(){ //returns the fileID of a data packet
        return this.fileID;
    }


}
