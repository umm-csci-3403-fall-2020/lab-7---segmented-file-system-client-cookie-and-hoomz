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

    // public String getFileName(byte[]packet){ //returns the fileName of a file given the header packet
    //     String fileName = new String(packet, 2, packet.length);
    //     return fileName;
    // }

    // public byte getFileID(byte[] packet){ //returns the fileID of a data packet
    //     byte fileID = packet[1];
    //     return fileID;
    // }
    public byte getFileID(HeaderPacket header){ //returns the fileID of a data packet
        //byte fileID = packet[1];
        return header.fileID;
    }


}
