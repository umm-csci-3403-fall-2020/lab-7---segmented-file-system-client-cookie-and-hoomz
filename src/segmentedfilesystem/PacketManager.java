package segmentedfilesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import segmentedfilesystem.DataPacket;
import segmentedfilesystem.HeaderPacket;

public class PacketManager {

    byte[] packet;
    List<HeaderPacket> headerList;
    List<DataPacket> dataList;

    public PacketManager(){
        this.headerList = new ArrayList<HeaderPacket>(); //list for the header packets
        this.dataList = new ArrayList<DataPacket>(); //list for all the data packets
    }

    public boolean isHeader(byte status) { // determines if a packet is a header packet or not
        boolean header = false;
        if (status % 2 == 0) {
            header = true;
        } 
        return header;
    }

    public void createPack(byte[] aPacket, int length) throws IOException { // creates the header and data packets
        if (isHeader(aPacket[0]) == true) {  //adds each created packet to their respective list
            newHeader(aPacket, length);
        }
        if (isHeader(aPacket[0]) != true) {
            newData(aPacket);
        }
        if (theEnd() == true){
            List<DataPacket> sortedList = sortDataList(dataList);
            List<DataPacket> data1 = populate(sortedList);
            List<DataPacket> data2 = populate(sortedList);
            List<DataPacket> data3 = populate(sortedList);
            sortDataPackNum(data1);
            sortDataPackNum(data2);
            sortDataPackNum(data3);
            writeToFile(headerList, data1);
            writeToFile(headerList, data2);
            writeToFile(headerList, data3);
        }
    }

    public void newHeader(byte[] pack, int length) { // creating a header packet
        byte[] fileName = Arrays.copyOfRange(pack, 2, length);
        HeaderPacket header = new HeaderPacket(pack[1], fileName, pack);
        headerList.add(header); // putting all the header packets into one list
    }

    public void newData(byte[] pack) { // creating a data packet
        int packNum = getPacketNum(pack);
        boolean last = lastPack(pack);
        if (last == true){ //determining if a data packet is the last packet for its file
            System.out.println(last);
        }
        byte[] infoStuff = Arrays.copyOfRange(pack, 4, pack.length);
        DataPacket data = new DataPacket(pack[1], packNum, infoStuff, last, pack);
        dataList.add(data); // putting all the data packets into one list
    }

    public int getPacketNum(byte[] packet) { // gets the packet number for a data packet dealing with the conversion
        int packetNumber;
        int valOne = Byte.toUnsignedInt(packet[2]); // conversion since java supports signed ints (-128 to 127)
        int valTwo = Byte.toUnsignedInt(packet[3]);
        packetNumber = 256 * valOne + valTwo;
        return packetNumber;
    }

    public boolean lastPack(byte[] packet) { // determines if a datapacket is the last one for a file
        boolean last = false;
        byte status = packet[0];
        if (status % 4 == 3) {
            last = true;
        }
        return last;
    }

    public List<DataPacket> sortDataList(List<DataPacket> dList) { // sorts the list of all data packets by their file numbers
        byte id;
        List<Byte> newList = new ArrayList<Byte>(); // creating a list of the fileID's of each datapacket
        for (int i = 0; i < dList.size(); i++) {
            DataPacket dummy = dList.get(i);
            id = dummy.getFileID();
            newList.add(id); //adding each fileID to newList
        }
        Collections.sort(newList); // sorting the list of fileID's
        List<DataPacket> sortedID = new ArrayList<DataPacket>(); //a new list which sorted the original list by fileID's
        for (Byte id2 : newList) { // for each byte in the sortedID list
            sortedID.add(dList.get(id2)); // adding the corresponding data packet to the new list
        }
        return sortedID; // now we have a list of the data packets which is sorted by their file numbers
                         // (ex: 111122333333)
    }

    public boolean isDataFinished(){ //determines if the dataPacket list has all the dataPackets it needs
        boolean finished = false;
        int packetTotal = 0;
        int size = dataList.size();
        for (int i = 0; i < dataList.size(); i++){
            DataPacket pack = dataList.get(i);
            boolean last = pack.getLast();
            if (last == true){
                packetTotal = packetTotal + pack.getPacketNumber();
            }
            if (size == packetTotal+1){
                finished = true;
            }
        }
        return finished;
    }

    public boolean isHeaderFinished(){ //determines if the header list has 3 headerPackets
        boolean finished = false;
        if (headerList.size() == 3){
            finished = true;
        }
        return finished;
    }

    public boolean theEnd(){ //determines if we have all header and data packets for the files
        boolean done = false;
        if (isDataFinished() == true && isHeaderFinished() == true){
            done = true;
        }
        return done;
    }

    /*
    The original dataPacket list we had at the beginnnng is sorted by fileID, looking something like 1111122223333333
    This method puts the dataPackets into 3 different lists based on their fileID
    */
    public List<DataPacket> populate(List<DataPacket> sortedID) { 
        List<DataPacket> fileList = new ArrayList<DataPacket>();
        for (int i = 0; i < sortedID.size() - 1; i++) {
            DataPacket dummy = sortedID.get(i);
            DataPacket dummy2 = sortedID.get(i + 1);
            // boolean last = dummy2.getLast(dummy2);
            fileList.add(dataList.get(i));
            sortedID.remove(i);
            if (dummy.getFileID() != dummy2.getFileID()) {
                return fileList;
            }
        }
        fileList.add(sortedID.get(sortedID.size() - 1));
        return fileList;
    }

    /*
    This method takes a list that has all the same file ID's and puts each dataPacket into a TreeMap
    TreeMaps are magic and sort its items as you enter them, so after we put each dataPacket into a TreeMap,
    we put them back into a list but now the array of bytes that have the information are in order.
    (ex: packNum1, packNum2,packNum3...)
    */
    public void sortDataPackNum(List<DataPacket> dataList) { 
        TreeMap<Integer, DataPacket> map = new TreeMap<Integer, DataPacket>();
        for (int i = 0; i < dataList.size(); i++) {
            DataPacket dummy = dataList.get(i);
            int packNum = dummy.getPacketNumber();
            // byte[] info = dummy.getDataInfo();
            map.put(packNum, dummy);
        }
        List<DataPacket> newDataList = new ArrayList<DataPacket>();
        for (Map.Entry<Integer, DataPacket> entry : map.entrySet()) {
            DataPacket pack = entry.getValue();
            newDataList.add(pack);
        }
    }

    /*
    Iterates through the list of headerPackets and sees if it's fileID matches the ID's of the dataPackets
    If they do match, create a file with the fileName of the header and the info is the corresponding dataPackets
    */
    public void writeToFile(List<HeaderPacket> headerList, List<DataPacket> dataList) throws IOException, FileNotFoundException {
        for (int i = 0; i < headerList.size(); i++){
            HeaderPacket head = headerList.get(i);
            byte fileID = head.getFileID();
            String fileName = new String(head.getFileName());
            File file = new File(fileName);
            FileOutputStream output = new FileOutputStream(file);

            for (int k = 0; k < dataList.size(); k++){
                DataPacket data2 = dataList.get(k);
                byte ID = data2.getFileID();
                if (fileID == ID){
                    output.write(data2.getDataInfo());
                }
            }
            output.flush();
            output.close();
        }
    }
}
