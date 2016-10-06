package com.ssmksh.closestack.ex;

import java.io.*;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import org.kohsuke.args4j.Option;
import com.ssmksh.closestack.util.Util;

public class Main {
   @Option(name = "--cmd", usage = "cmd", aliases = "-cm")
   public static String cmd = "none";   //default
   @Option(name = "--type", usage = "type", aliases = "-t")
   public static String type = "none";
   @Option(name = "--name", usage = "name", aliases = "-n")
   public static String name = "none";
   @Option(name = "--password", usage = "password", aliases = "-pw")
   public static String password = "none";
   @Option(name = "--cpu", usage = "cpu", aliases = "-c")
   public static String cpu = "none";
   @Option(name = "--ram", usage = "ram", aliases = "-r")
   public static String ram = "none";
   @Option(name = "--disk", usage = "disk", aliases = "-d")
   public static String disk = "none";
   @Option(name = "--webId", usage = "webId", aliases = "-webid")
   public static String webId = "";
   @Option(name = "--ip", usage = "ip")
   public static String ip = "211.189.20.80";
   public static String ksFilename = "CentOS.ks";
   
   public static void main(String[] args) {
      Util.parseArgs(args, new Main());
      name = webId+"_"+name;
      
      if (cmd.equals("list")) {
         list();
      } else if (cmd.equals("delete")) {
         stop();
         delete();
      } else if (cmd.equals("start")) {
         start();
      } else if (cmd.equals("stop")) {
         stop();
      } else if (cmd.equals("generate")) {
         generate();
      } 
   }

   public static void list() {
      System.out.println("virsh -c qemu:///system list");
      Util.exec("virsh -c qemu:///system list");
   }
   
   public static void delete() {
      System.out.println("virsh -c qemu:///system undefine "+name);
      Util.exec("virsh -c qemu:///system undefine "+name);
   }

   public static void start() {
      System.out.println("virsh -c qemu:///system start "+name);
      Util.exec("virsh -c qemu:///system start "+name);
   }

   public static void stop() {
      System.out.println("virsh -c qemu:///system destroy "+name);
      Util.exec("virsh -c qemu:///system destroy "+name);
   }
   
   public static boolean execute_term(String pid){
      Socket socket = null;
       BufferedOutputStream bos = null;
       
        try {
           System.out.println("EXECUTE TERM!!!!!!!!!!!!");
           socket = new Socket("211.189.20.168",1234);

           // server -> client 출력용 스트림
           bos = new BufferedOutputStream(socket.getOutputStream());
           byte[] b = new byte[1024];
           
           String ab;
           if(type.equals("centos")){
              ab = "a";
           }else{
              ab = "b";
           }
           String hi = ab+"/"+ip;
           
           b = hi.getBytes();
           bos.write(b);
           bos.flush();
           
           
           bos.close();
           socket.close();
           
        } catch (UnknownHostException e) {
            System.out.println("Unkonw exception " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("IOException caught " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
   }
   
   public static boolean execute_shell(String hi){
      Socket socket = null;
       BufferedOutputStream bos = null;
       BufferedInputStream bis = null;
       
        try {
           socket = new Socket("211.189.20.168",8197);

           // server -> client 출력용 스트림
           bos = new BufferedOutputStream(socket.getOutputStream());
           byte[] b = new byte[1024];
           b = hi.getBytes();
           bos.write(b);
           bos.flush();
           
           bis = new BufferedInputStream(socket.getInputStream());
           byte[] buff = new byte[1024];
            int len= bis.read(buff);
            if(len > 0){
               //create vm complete
               System.out.println("pid is : " + buff);
               execute_term(buff.toString());
            }
            bis.close();
           bos.close();
           socket.close();   
        } catch (UnknownHostException e) {
            System.out.println("Unkonw exception " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("IOException caught " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
   }
   
   public static boolean makeKickstart(){
      String strConfig = "install\n"
                  +"lang en_US\n"
                  +"keyboard us\n"
                  +"timezone Asia/Seoul\n"
                  +"auth --useshadow --enablemd5\n"
                  +"selinux --disabled\n"
                  +"firewall --disabled\n"
                  +"services --enabled=NetworkManager,sshd,chronyd\n"
                  +"eula --agreed\n"
                  +"ignoredisk --only-use=sda\n"
                  +"reboot\n\n"
                  +"bootloader --location=mbr\n"
                  +"zerombr\n"
                  +"clearpart --all --initlabel\n"
                  +"part swap --asprimary --fstype=\"swap\" --size=1024\n"
                  +"part /boot --fstype xfs --size=200\n"
                  +"part pv.01 --size=1 --grow\n"
                  +"volgroup rootvg01 pv.01\n"
                  +"logvol / --fstype xfs --name=lv01 --vgname=rootvg01 --size=1 --grow\n"
                  +"rootpw \""+password+"\"\n"
                  +"user --groups=sudoers --homedir=/home/centOS --name=centOS --password=\""+ password +"\" --gecos=\"centOS\"\n"
                  +"repo --name=base --baseurl=http://mirror.cogentco.com/pub/linux/centos/7/os/x86_64/\n"
                  +"url --url=\"http://mirror.cogentco.com/pub/linux/centos/7/os/x86_64/\"\n"
                  +"%packages --ignoremissing\n"
                  +"@base\n"
                  +"@core\n"
                  +"chrony\n"
                  +"yum-cron\n"
                  +"vim\n"
                  +"salt-minion\n"
                  +"%end";
      
      if(!webId.equals("")){
         ksFilename = webId + "_centos.ks";
      }
      
      FileWriter writer;
      try {
         writer = new FileWriter("./" + ksFilename);
         writer.write(strConfig);
         writer.close();
      } catch (IOException e1) {
         e1.printStackTrace();
         return false;
      }

      Socket socket;
      try {
         socket = new Socket("211.189.20.11", 3303);
         FileSender fs = new FileSender(socket, "./" + ksFilename);
         fs.start();
      } catch (UnknownHostException e) {
         e.printStackTrace();
         return false;
      } catch (IOException e) {
         e.printStackTrace();
         return false;
      }
      
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
         return false;
      }
      
      return true;
   }

   public static void generate() {
      if(type.equals("centos")){
         
         //if(makeKickstart()){
            String virt_install_cmd = "sudo virt-install --virt-type=kvm --name "+name+" --ram "+ram+" --vcpus="+cpu
                  +" --os-variant=rhel5 --hvm --graphics none --location /home/node01/iso/CentOS-7-x86_64-Minimal-1511.iso "
                  +"--disk path=/home/node01/images/"+name+".img,size="+disk+" --autostart "
                  +"--extra-args='ks=http://211.189.20.11:8080/files/" + ksFilename + " ip="+ip
                  +" netmask=255.255.255.0 gateway=211.189.20.1 dns=8.8.8.8 console=tty0 console=ttyS0,115200n8'";
            if(execute_shell(virt_install_cmd)){
               System.out.println("[OK] Send CMD To Server OK!");
            }else{
               System.out.println("[FAIL] Send CMD To Server Fail..");
            }
         //}
         //else{
         //   System.out.println("Make Kickstart File Error");
         //}
         

      }else if(type.equals("ubuntu")){
         String vmbuilder_cmd = "sudo vmbuilder kvm ubuntu -o"
               +" --suite precise --flavour virtual --arch i386 "
               +"--ip "+ip+" --gw 211.189.20.1 --dns 8.8.8.8 "
               +"--hostname " + name + " --bridge br0 "
               +"--pass " + password + " --mem " + ram
               +" --cpus " + cpu + " --rootsize "+ disk
               +" --removepkg=cron --addpkg openssh-server --libvirt qemu:///system";
         if(execute_shell(vmbuilder_cmd)){
            System.out.println("[OK] Send CMD To Server OK!");
         }else{
            System.out.println("[FAIL] Send CMD To Server Fail..");
         }
      }
   }
}

class FileSender extends Thread {
    Socket socket;
    DataOutputStream dos;
    FileInputStream fis;
    BufferedInputStream bis;
    String filename;
    int control = 0;
    public FileSender(Socket socket,String filestr) {
        this.socket = socket;
        this.filename = filestr;
        try {
            // 데이터 전송용 스트림 생성
            dos = new DataOutputStream(socket.getOutputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            out.println(filename);
            try {
            Thread.sleep(2000);
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            String fName = filename;


            // 파일 내용을 읽으면서 전송
            File f = new File(fName);
            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);

            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = bis.read(data)) != -1) {
                control++;
                if(control % 10000 == 0)
                {
                    System.out.println("Uploading..." + control/10000);
                }
                dos.write(data, 0, len);
            }

            dos.flush();
            dos.close();
            bis.close();
            fis.close();
            System.out.println("Kickstart file upload complete...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}