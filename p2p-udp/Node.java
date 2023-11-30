import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Node
{
    // Variables
    public int port = 5000;
    public NetworkListener network;
    public KeyboardListener keyboard;
    public List<RemoteNode> nodes = new ArrayList<RemoteNode>();

    // Inner Classes
    public class RemoteNode {
        public String ip;
        public int port;
    }

    public class NetworkListener extends Thread {
        Node node = null;
        public NetworkListener(Node n) { node = n; }
        public void run() {
            System.out.println("Starting NetworkListner on port "+node.port);
            try {
                DatagramSocket socket = new DatagramSocket(node.port);
                DatagramPacket packet;
                while(true) {
                    byte[] receive = new byte[65535];
                    packet = new DatagramPacket(receive, receive.length);
                    socket.receive(packet);
                    System.out.println("Network Rx");
                    String data = ByteToString(receive);
                    String remoteip = packet.getAddress().toString().substring(1);
                    node.network(data, remoteip);
                }
            } catch(Exception e) { e.printStackTrace(); }
            
        }
    }

    public String ByteToString(byte[] a)
    {
        if (a==null) return null;
        StringBuilder ret = new StringBuilder();
        int i=0;
        while(a[i]!=0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }

    public class KeyboardListener extends Thread {
        Node node = null;
        public KeyboardListener(Node n) { node = n; }
        public void run() {
            Scanner sc = new Scanner(System.in);
            while(true)
            {
                String line = sc.nextLine();
                node.keyboard(line);
            }
        }
    }

    // Methods
    public static void main(String[] args)
    {
        Node n = new Node();
        if (args.length>0) n.port = Integer.parseInt(args[0]);
        System.out.println("Listening on port "+n.port);
        n.start();
    }

    public void start()
    {
        keyboard = new KeyboardListener(this);
        keyboard.start();
        network = new NetworkListener(this);
        network.start();
    }

    public void keyboard(String line)
    {
        System.out.println("K> "+line);
        String[] cmd = line.split(" ");
        if (cmd[0].equalsIgnoreCase("send"))
        {
            // send remoteip remoteport data
            if (cmd.length != 4) System.out.println("send remoteip remoteport data");
            else {
                String remoteip = cmd[1];
                int remoteport = Integer.parseInt(cmd[2]);
                String data = cmd[3];
                System.out.println("Sending <"+data+"> to "+remoteip+":"+remoteport);
                send(data, remoteip, remoteport);
            }
        }
        else if (cmd[0].equalsIgnoreCase("ping"))
        {
            // ping remoteip remoteport
            if (cmd.length != 3) System.out.println("ping remoteip remoteport");
            else {
                String remoteip = cmd[1];
                int remoteport = Integer.parseInt(cmd[2]);
                String data = "PING "+port;
                System.out.println("Sending <"+data+"> to "+remoteip+":"+remoteport);
                addNode(remoteip, remoteport);
                send(data, remoteip, remoteport);
            }
        }
        else if (cmd[0].equalsIgnoreCase("fetch"))
        {
            // ping remoteip remoteport
            if (cmd.length != 3) System.out.println("ping remoteip remoteport");
            else {
                String remoteip = cmd[1];
                int remoteport = Integer.parseInt(cmd[2]);
                String data = "FETCH "+port;
                System.out.println("Sending <"+data+"> to "+remoteip+":"+remoteport);
                addNode(remoteip, remoteport);
                send(data, remoteip, remoteport);
            }
        }
        else if (cmd[0].equalsIgnoreCase("add"))
        {
            // add remoteip remoteport
            if (cmd.length != 3) System.out.println("add remoteip remoteport");
            else {
                String remoteip = cmd[1];
                int remoteport = Integer.parseInt(cmd[2]);
                addNode(remoteip,remoteport);
            }
        }
        else if (cmd[0].equalsIgnoreCase("list"))
        {
            for(RemoteNode n: nodes)
            {
                System.out.println("REMOTE "+n.ip+":"+n.port);
            }
        }
        else if (cmd[0].equalsIgnoreCase("broadcast"))
        {
            for(RemoteNode n: nodes)
            {
                String data = "PING "+port;
                System.out.println("Sending <"+data+"> to "+n.ip+":"+n.port);
                send(data, n.ip, n.port);
            }
        }
        else
        {
            System.out.println("Unknown command");
        }
    }

    public void network(String data, String remoteip)
    {
        System.out.println("N> "+data+" from "+remoteip);
        String[] parts = data.split(" ");
        if (parts[0].equalsIgnoreCase("PING")) // we have been pinged!
        {
            int remoteport = Integer.parseInt(parts[1]);
            System.out.println("PING from "+remoteip+" respond on port "+remoteport);
            addNode(remoteip, remoteport);
            send("PONG",remoteip,remoteport);
        }
        else if (parts[0].equalsIgnoreCase("FETCH")) // we have been asked for our node list
        {
            int remoteport = Integer.parseInt(parts[1]);
            System.out.println("FETCH from "+remoteip+" respond on port "+remoteport);
            addNode(remoteip, remoteport);
            for(RemoteNode n: nodes)
            {
                String d = "NODE "+n.ip+" "+n.port;
                send(d,remoteip,remoteport);
            }
            //send("PONG",remoteip,remoteport);
        }
        else if (parts[0].equalsIgnoreCase("NODE")) // we have been given a node
        {
            String nodeip = parts[1];
            int nodeport = Integer.parseInt(parts[2]);
            System.out.println("NODE of "+nodeip+":"+nodeport);
            addNode(nodeip, nodeport);
        }
    }

    public void send(String data, String remoteip, int remoteport)
    {
        try {
            byte[] bytes = data.getBytes();
            InetAddress inet = InetAddress.getByName(remoteip);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, inet, remoteport);
            ds.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addNode(String remoteip, int remoteport)
    {
        for(RemoteNode n: nodes)
        {
            if (n.ip.equalsIgnoreCase(remoteip) && remoteport == n.port)
                return;
        }
        RemoteNode n = new RemoteNode();
        n.ip = remoteip;
        n.port = remoteport;
        nodes.add(n);
    }
}