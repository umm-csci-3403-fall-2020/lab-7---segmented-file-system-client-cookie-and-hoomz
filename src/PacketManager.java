import java.util.HashMap; 

public class PacketManager implements Runnable {
    //stuff here to determine whether a packet is a header or data packet,
    //then constructs the packet and adds it to the correct ReceivedFile object
    public byte[] packet;
    public PacketManager(byte[] packet){
        this.packet = packet;
    }


    //create a hashmap for each new packet if there isn't one for it already

    @Override
    public void run(){
        HashMap<byte, byte[]> allPackets = new HashMap<>(); 
        byte idByte = packet[1]; //file ID of a packet, which packet it belongs to
        byte statusByte = packet[0]; //status byte of a packet
        
        if (statusByte % 2 ==0){ //if status byte is even, then it's a header packet

        }

        allPackets.put(idByte, packet);
    }

}
