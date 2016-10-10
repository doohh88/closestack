package com.ssmksh.closestack.worker;

import com.ssmksh.closestack.dto.Instance;
import com.ssmksh.closestack.dto.SnapShot;
import com.ssmksh.closestack.dto.TellCommand;
import com.ssmksh.closestack.util.Util;

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

import akka.event.Logging;
import akka.event.LoggingAdapter;

public class KVMActor extends VMActor {
   LoggingAdapter log = Logging.getLogger(getContext().system(), this);
   public static String ksFilename = "CentOS.ks";
   @Override
   public void onReceive(Object message) throws Throwable {
      if (message instanceof TellCommand) {
         log.info("receive tellCommand [KVMActor]");
         TellCommand tellCommand = (TellCommand) message;
         String cmd = tellCommand.getCommand();
         Instance instance = null;
         SnapShot snapshot = null;
         if (tellCommand.getData() instanceof Instance) {
            instance = (Instance) tellCommand.getData();
         } else {
            snapshot = (SnapShot) tellCommand.getData();
         }

         log.info("command: {}", tellCommand.getCommand());
         if (cmd.equals("generate")) {
            generate(instance);
         } else if (cmd.equals("list")) {
            list();
         } else if (cmd.equals("delete")) {
            stop(instance);
            delete(instance);
         } else if (cmd.equals("start")) {
            start(instance);
         } else if (cmd.equals("stop")) {
            stop(instance);
         } else if (cmd.equals("restart")) {
            restart(instance);
         } else if (cmd.equals("test")) {
            Util.sshChecker("211.189.20.168",instance, getSender());
         } else if (cmd.equals("snapshot")) {
            snapshot(snapshot);
         } else if (cmd.equals("deleteSnapshot")) {
            deleteSnapshot(snapshot);
         } else if (cmd.equals("listSnapshot")) {
            listSnapshot(snapshot);
         } else {
            log.info("no operate: {}", cmd);
         }
      } else {
         unhandled(message);
      }
   }

   void list() {
      log.info("[KVMActor] virsh -c qemu:///system list");
      Util.exec("virsh -c qemu:///system list");
   }

   void delete(Instance instance) {
      log.info("[KVMActor] virsh -c qemu:///system undefine ", instance);
      Util.exec("virsh -c qemu:///system undefine " + instance.getName());
   }

   void start(Instance instance) {
      log.info("[KVMActor] virsh -c qemu:///system start " + instance);
      Util.exec("virsh -c qemu:///system start " + instance.getName());
   }

   void stop(Instance instance) {
      log.info("[KVMActor] virsh -c qemu:///system destroy " + instance);
      Util.exec("virsh -c qemu:///system destroy " + instance.getName());
   }

   void restart(Instance instance) {
      log.info("[KVMActor] virsh -c qemu:///system destroy " + instance);
      Util.exec("virsh -c qemu:///system resume " + instance.getName());
   }

   void snapshot(SnapShot snapshot) {
      // virsh snapshot-create-as --domain [domain이름] --name [스냅샷이름]
      log.info("[KVMActor] virsh snapshot-create-as --domain " + snapshot.getVmName() + " --name "
            + snapshot.getName(), snapshot);
      Util.exec("virsh snapshot-create-as --domain " + snapshot.getVmName() + " --name " + snapshot.getName());
   }

   void deleteSnapshot(SnapShot snapshot) {
      // virsh snapshot-delete --domain [domain이름] --name [스냅샷이름]
      log.info("[KVMActor] virsh snapshot-delete --domain " + snapshot.getVmName() + " --name " + snapshot.getName(),
            snapshot);
      Util.exec("virsh snapshot-delete --domain " + snapshot.getVmName() + " --name " + snapshot.getName());
   }

   void listSnapshot(SnapShot snapshot) {
      // virsh snapshot-list --domain [domain이름]
      log.info("[KVMActor] virsh snapshot-list --domain " + snapshot.getVmName(), snapshot);
      Util.exec("virsh snapshot-list --domain " + snapshot.getVmName());
   }

   boolean makeKickstart(Instance instance) {
      String strConfig = "install\n" + "lang en_US\n" + "keyboard us\n" + "timezone Asia/Seoul\n"
            + "auth --useshadow --enablemd5\n" + "selinux --disabled\n" + "firewall --disabled\n"
            + "services --enabled=NetworkManager,sshd,chronyd\n" + "eula --agreed\n" + "ignoredisk --only-use=sda\n"
            + "reboot\n\n" + "bootloader --location=mbr\n" + "zerombr\n" + "clearpart --all --initlabel\n"
            + "part swap --asprimary --fstype=\"swap\" --size=1024\n" + "part /boot --fstype xfs --size=200\n"
            + "part pv.01 --size=1 --grow\n" + "volgroup rootvg01 pv.01\n"
            + "logvol / --fstype xfs --name=lv01 --vgname=rootvg01 --size=1 --grow\n" + "rootpw \"" + instance.getPw()
            + "\"\n" + "user --groups=sudoers --homedir=/home/centOS --name=centOS --password=\"" + instance.getPw()
            + "\" --gecos=\"centOS\"\n"
            + "repo --name=base --baseurl=http://mirror.cogentco.com/pub/linux/centos/7/os/x86_64/\n"
            + "url --url=\"http://mirror.cogentco.com/pub/linux/centos/7/os/x86_64/\"\n"
            + "%packages --ignoremissing\n" + "@base\n" + "@core\n" + "chrony\n" + "yum-cron\n" + "vim\n"
            + "salt-minion\n" + "%end";

      if (!instance.getUserName().equals("")) {
         ksFilename = instance.getUserName() + "_centos.ks";
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

   void generate(Instance instance) {
      log.info("[KVM] os type : "+instance.getOs());
      if (instance.getOs().equals("centos")) {
         log.info("[KVM] GENERATE\n");
         if (makeKickstart(instance)) {
            String virt_install_cmd = "b]"+instance.getName()+"]sudo virt-install --virt-type=kvm --name " + instance.getName() + " --ram " + (instance.getFlavor().getRam()*1024)
                  + " --vcpus=" + instance.getFlavor().getvCpus()
                  + " --os-variant=rhel5 --hvm --graphics none --location /home/node01/iso/CentOS-7-x86_64-Minimal-1511.iso "
                  + "--disk path=/home/node01/images/" + instance.getName() + ".qcow,size=" 
                  + instance.getFlavor().getDisk() + " --autostart "
                  + "--extra-args='ks=http://211.189.20.11:8080/files/" + ksFilename + " ip=" + instance.getIp()
                  + " netmask=255.255.255.0 gateway=211.189.20.1 dns=8.8.8.8 console=tty0 console=ttyS0,115200n8'";
            log.info("[KVM] CMD : " + virt_install_cmd);
            if (Util.execute_shell(instance, virt_install_cmd, getSender())) {
               System.out.println("[KVMActor][OK] Send CMD To Server OK!");
            } else {
               System.out.println("[KVMActor][FAIL] Send CMD To Server Fail..");
            }
         } else {
            System.out.println("[KVMActor][FAIL]Make Kickstart File Error");
         }

      } else if (instance.getOs().equals("ubuntu")) {
         String vmbuilder_cmd = "a]"+instance.getName()+"]sudo vmbuilder kvm ubuntu -o" + " --suite precise --flavour virtual --arch i386 "
               + "--ip " + instance.getIp() + " --gw 211.189.20.1 --dns 8.8.8.8 " + "--hostname " + instance.getName() 
               + " --bridge br0 " + "--pass " + instance.getPw() + " --mem " + (instance.getFlavor().getRam()*1024)
               + " --cpus " + instance.getFlavor().getvCpus() + " --rootsize " + (instance.getFlavor().getDisk()*1024)
               + " --removepkg=cron --addpkg openssh-server --libvirt qemu:///system";
         if (Util.execute_shell(instance, vmbuilder_cmd, getSender())) {
            System.out.println("[KVMActor][OK] Send CMD To Server OK!");
         } else {
            System.out.println("[KVMActor][FAIL] Send CMD To Server Fail..");
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

   public FileSender(Socket socket, String filestr) {
      this.socket = socket;
      this.filename = filestr;
      try {
         // 데이터 전송용 스트림 생성
         dos = new DataOutputStream(socket.getOutputStream());
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
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
            if (control % 10000 == 0) {
               System.out.println("Uploading..." + control / 10000);
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