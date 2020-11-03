package segmentedfilesystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import segmentedfilesystem.DataPacket;
import segmentedfilesystem.HeaderPacket;

public class PacketManager {
    
    byte[] packet;
    List<HeaderPacket> headerList;
    List<DataPacket> dataList;

    public PacketManager(){
        this.headerList = new ArrayList<HeaderPacket>();
        this.dataList = new ArrayList<DataPacket>();
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

    public void createPack(byte[] aPacket){ //creates the header and data packets
        if (isHeader(aPacket[0])== true){
            newHeader(aPacket);
        }
        if (isHeader(aPacket[0]) != true){
            newData(aPacket);
        }
    }

    public void newHeader(byte[] pack){ //creating a header packet
        byte[] fileName = Arrays.copyOfRange(packet, 2, packet.length); 
        HeaderPacket header = new HeaderPacket(pack[1],fileName);
        headerList.add(header); //putting all the header packets into one list
        //return header;
    }

    public void newData(byte[] pack){ //creating a data packet
        int packNum = getPacketNum(pack);
        boolean last = lastPack(pack);
        byte[] infoStuff = Arrays.copyOfRange(pack, 4, pack.length);
        DataPacket data = new DataPacket(pack[1], packNum, infoStuff, last);
        dataList.add(data); //putting all the data packets into one list
        //return data;
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

    // public int partNum(byte[] packet){
    //     int numOfParts = 0;
    //     if (lastPack(packet) == true){
    //         numOfParts = getPacketNum(packet);
    //     }
    //     return numOfParts;
    // }

    // public int totalPackets(List<DataPacket> dataList){ //determines number of total data packets there are
    //     int totalNum = 0;
    //     boolean last;
    //     for (int i = 0; i < dataList.size(); i++){
    //         DataPacket dummy = dataList.get(i);
    //         last = dummy.getLast();
    //         if (last == true){
    //             totalNum = totalNum + dummy.getPacketNumber();
    //         }
    //     }
    //     return totalNum;
    // }

    public List<DataPacket> sortDataList(List<DataPacket> dataList){ //sorts the list of data packets by file numbers
        byte id;
        List<Byte> newList = new ArrayList<Byte>(); //creating a list of the fileID's of each datapacket
        for (int i = 0; i < dataList.size(); i++){
            DataPacket dummy = dataList.get(i);
            id = dummy.getFileID();
            newList.add(id);
        }
        Collections.sort(newList); //sorting the list of fileID's
        List<DataPacket> sortedID = new ArrayList<DataPacket>();
        for (Byte id2 : newList){ //for each byte in the sortedID list 
            sortedID.add(dataList.get(id2)); //adding the corresponding data packet to the new list 
        }
        return sortedID; //now we have a list of the data packets which is sorted by their file numbers (ex: 111122333333)
    }

    //List<DataPacket> file1Prep = populate(dataList);
    public List<DataPacket> populate(List<DataPacket> dataList) {
        List<DataPacket> fileList = new ArrayList<DataPacket>();
        for (int i = 0; i < dataList.size()-1; i++){
            DataPacket dummy = dataList.get(i);
            DataPacket dummy2 = dataList.get(i+1);
            //boolean last = dummy2.getLast(dummy2);
            fileList.add(dataList.get(i));
            dataList.remove(i);
            if (dummy.getFileID() != dummy2.getFileID()){
                return fileList;
            }
        }
        fileList.add(dataList.get(dataList.size()-1));
        return fileList;
    }

    public TreeMap<Integer, byte[]> makeMaps(List<DataPacket> dataList){ //treeMaps are magic and sort entries as they're entered
        TreeMap<Integer, byte[]> map = new TreeMap<Integer, byte[]>();
        for (int i = 0; i < dataList.size(); i++){
            DataPacket dummy = dataList.get(i);
            int packNum =  dummy.getPacketNumber();
            byte[] info = dummy.getDataInfo();
            map.put(packNum,info);
        }
        return map;
    }

    public void buildFile(List<HeaderPacket> headerList){
        for (int i = 0; i < headerList.size(); i++){
            HeaderPacket header = headerList.get(i);
            byte[] name = header.getFileName();
        }
    }
}
