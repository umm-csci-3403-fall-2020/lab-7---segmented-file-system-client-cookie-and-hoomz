import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.HashMap;

import segmentedfilesystem.DataPacket;
import segmentedfilesystem.HeaderPacket;

public class PacketManager {
    
    DatagramPacket packet;
    
    public PacketManager(){
        //create list here and hash maps
    }

    public void createPack(byte[] aPacket){
        if (isHeader(aPacket[0])== true){
            newHeader(aPacket);
        }
        if (isHeader(aPacket[0]) != true){
            newData(aPacket);
        }
    }

    public HeaderPacket newHeader(byte[] pack){ //creating a header packet
        String fileName = new String(pack,2,pack.length);
        HeaderPacket header = new HeaderPacket(pack[1],fileName);
        return header;
    }

    public DataPacket newData(byte[] pack){ //creating a data packet
        int packNum = getPacketNum(pack);
        boolean last = lastPack(pack);
        byte[] infoStuff = Arrays.copyOfRange(pack, 4, pack.length);
        DataPacket data = new DataPacket(pack[1], packNum, infoStuff, last);
        return data;
    }
     

    public int getPacketNum(byte[] packet){ //gets the packet number for a data packet
        int packetNumber;
        int valOne = Byte.toUnsignedInt(packet[2]); //conversion since java supports signed ints (-128 to 127)
        int valTwo = Byte.toUnsignedInt(packet[3]);
        packetNumber = 256*valOne+valTwo;
        return packetNumber;
    }

    public boolean lastPack(byte[] packet){ //determines if a datapacket is the last one for a file
        boolean last = false;
        byte status = packet[0];
        if (status%4==3){
            last = true;
        }
        return last;
    }

    public boolean isHeader(byte status){ //determines if a packet is a header packet or not
        boolean header;
        if (status%2 == 0){
            header = true;
        }
        else {
            header = false;
        }
        return header;
    }
}
