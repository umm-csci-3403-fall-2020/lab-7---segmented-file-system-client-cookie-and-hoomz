package segmentedfilesystem;

import java.util.Arrays;

public class HeaderPacket {
    byte[] packet;
    byte fileID = packet[1];
    byte[] fileName = Arrays.copyOfRange(packet, 2, packet.length);  

    public HeaderPacket(byte fileID, byte[] fileName){
        this.fileID = fileID;
        this.fileName = fileName;
    }

    public byte[] getFileName(){ //returns the fileName of a file given the header packet
        return this.fileName;
    }

    // public byte getFileID(byte[] packet){ //returns the fileID of a data packet
    //     byte fileID = packet[1];
    //     return fileID;
    // }
    public byte getFileID(){ //returns the fileID of a data packet
        //byte fileID = packet[1];
        return this.fileID;
    }


}
