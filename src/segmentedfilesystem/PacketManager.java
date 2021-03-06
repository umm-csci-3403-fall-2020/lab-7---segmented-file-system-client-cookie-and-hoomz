package segmentedfilesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    /*
    This method will take the array of bytes and calculated length and create either a dataPacket or headerPacket. 
    It uses the isHeader method as a way to determine if the given array of bytes is either a dataPacket or headerPacket
    */
    public void createPack(byte[] aPacket, int length) throws IOException { 
        if (isHeader(aPacket[0]) == true) {  
            newHeader(aPacket, length);
        }
        if (isHeader(aPacket[0]) != true) {
            newData(aPacket, length);
        }
        if (theEnd() == true){
            System.out.println(dataList.size());
            Map<Byte, List <DataPacket>> sortedList = sortDataList(dataList);
            Byte[] uniqueFileIDs = sortedList.keySet().toArray(new Byte[0]);
            List<DataPacket> data1 = sortedList.get(uniqueFileIDs[0]);
            List<DataPacket> data2 = sortedList.get(uniqueFileIDs[1]);
            List<DataPacket> data3 = sortedList.get(uniqueFileIDs[2]);
            List<DataPacket> sorted1 = sortDataPackNum(data1);
            List<DataPacket> sorted2 = sortDataPackNum(data2);
            List<DataPacket> sorted3 = sortDataPackNum(data3);
            matchMaker(headerList, sorted1);
            matchMaker(headerList, sorted2);
            matchMaker(headerList, sorted3);
        }
    }

    public void newHeader(byte[] pack, int length) { // creating a header packet
        byte[] fileName = Arrays.copyOfRange(pack, 2, length);
        HeaderPacket header = new HeaderPacket(pack[1], fileName, pack);
        headerList.add(header); // putting all the header packets into one list
    }

    public void newData(byte[] pack, int length) { // creating a data packet
        int packNum = getPacketNum(pack);
        boolean last = lastPack(pack);
        if (last == true){ //determining if a data packet is the last packet for its file
            pack = trimPack(pack, length);
        }
        byte[] infoStuff = trimPack(pack, length); //trimming any unused space in the buffer
        DataPacket data = new DataPacket(pack[1], packNum, infoStuff, last, pack);
        dataList.add(data); // putting all the data packets into one list
    }

    /*
    This method is mainly for the last packets for each file. It's supposed to trim the empty space in the 
    array of bytes since the last packet won't fill the entire 1028 bit buffer it's given
    */
    public byte[] trimPack(byte[] packet, int length){
        packet = Arrays.copyOfRange(packet, 0, length); 
        return packet;
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

    /*
    Takes the list of all the data packets and puts them into a TreeMap based on their fileID, thus automatically sorting the
    file IDs so the data packets are all together with the other datapackets for a file. Then the data packets are put back
    into a list, but are now sorted by fileID
    */
    public Map<Byte, List<DataPacket>> sortDataList(List<DataPacket> dList){
        Map<Byte, List<DataPacket>> map = new HashMap<Byte, List<DataPacket>>();
        for (DataPacket p: dList){
            byte fileID = p.getFileID();
            List<DataPacket> packets = map.get(fileID);
            if (packets == null){
                packets = new ArrayList <DataPacket>();
                map.put(fileID, packets);
            }
            packets.add(p);
        }
        return map;
    }

    public boolean isDataFinished(){ //determines if the dataPacket list has all the dataPackets it needs
        boolean finished = false;
        int packetTotal = 3;
        int footerCounter = 0;
        int size = dataList.size();
        for (int i = 0; i < dataList.size(); i++){
            DataPacket pack = dataList.get(i);
            //boolean last = pack.getLast();
            if (pack.getLast()){
                packetTotal += pack.getPacketNumber();
                footerCounter++;
            }
            if (footerCounter == 3 && size == packetTotal){
                finished = true;
                return finished;
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
    This method takes a list that has all the same file ID's and puts each dataPacket into a TreeMap
    TreeMaps are magic and sort its items as you enter them, so after we put each dataPacket into a TreeMap,
    we put them back into a list but now the array of bytes that have the information are in order.
    (ex: packNum1, packNum2,packNum3...)
    */
    public List<DataPacket> sortDataPackNum(List<DataPacket> dataLists) { 
        TreeMap<Integer, DataPacket> map = new TreeMap<Integer, DataPacket>();
        for (int i = 0; i < dataLists.size(); i++) {
            DataPacket dummy = dataLists.get(i);
            int packNum = dummy.getPacketNumber();
            map.put(packNum, dummy);
        }
        List<DataPacket> newDataList = new ArrayList<DataPacket>();
        for (Map.Entry<Integer, DataPacket> entry : map.entrySet()) {
            DataPacket pack = entry.getValue();
            newDataList.add(pack);
        }
        return newDataList;
    }

    /*
    Matches a header with the corresponding dataPackets then calls writeToFile to write them out
    */
    public void matchMaker(List<HeaderPacket> headList, List<DataPacket> infoList)
            throws FileNotFoundException, IOException {
        for (int i = 0; i < headList.size(); i++){
            HeaderPacket head = headList.get(i);
            byte id = head.getFileID();
            DataPacket data = infoList.get(0);
            byte dataID = data.getFileID();
            if (id == dataID){
                System.out.println("Match made!");
                System.out.println(infoList.size());
                writeToFile(head, infoList);
            }
        }
    }

    /*
    Writes out a file with the fileName coming from the headerPacket and the info coming from the list of dataPackets.
    Utilizes trim to get rid of leading and trailing spaces which affects the fileName
    */
    public void writeToFile(HeaderPacket header, List<DataPacket> dataList) throws IOException, FileNotFoundException {
        String fileName = new String(header.getFileName());
        fileName = fileName.trim(); //getting rid of any subtle spaces
        File file = new File(fileName);
        FileOutputStream output = new FileOutputStream(file);

        for (int k = 0; k < dataList.size(); k++){
            DataPacket data2 = dataList.get(k);
            output.write(data2.getDataInfo());
        }
        System.out.println("This file was written!!");
        output.flush();
        output.close();
    }
}
