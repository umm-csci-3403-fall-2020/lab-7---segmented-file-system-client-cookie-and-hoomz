package segmentedfilesystem;

public class HeaderPacket {
    byte[] packet;
    byte fileID = packet[1];
    String fileName = new String(packet, 2, packet.length);    

    public HeaderPacket(byte fileID, String fileName){
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


}
