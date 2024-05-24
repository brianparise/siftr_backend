package com.siftr.SftpClient;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

@Configuration
@Getter
@Setter
class SftpTask extends TimerTask {
    private int time = 0;
//    String host = "10.211.55.8";
//    String user = "bp51";
    private String host;
    private String username;
    private String password;
    private int port;
    private String localDirectory;
    private String remoteDirectory;
    private Session session;
    private JSch jsch;
    private boolean shouldDeleteAfterDownloading;
    public SftpTask ()
    {

    }
    public SftpTask(String host, String username, String password, int port, String remoteDirectory,
                    String localDirectory, boolean shouldDeleteAfterDownloading)
    {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.remoteDirectory = remoteDirectory;
        this.localDirectory = localDirectory;
        this.shouldDeleteAfterDownloading = shouldDeleteAfterDownloading;
        jsch = new JSch();
    }
    private void connectFTP()
    {
        try {
            String fileSeparator = FileSystems.getDefault().getSeparator();
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("cipher.s2c", "aes128-cbc,3des-cbc,blowfish-cbc");
            session.setConfig("cipher.c2s", "aes128-cbc,3des-cbc,blowfish-cbc");
            session.setConfig("CheckCiphers", "aes128-cbc");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd(remoteDirectory);
            Vector filelist = sftp.ls(remoteDirectory);
            for (Object o : filelist) {
                LsEntry entry = (LsEntry) o;
                if(!entry.getAttrs().isDir())
                {
                    System.out.println(entry.getFilename());
                    String filename = entry.getFilename();
                    InputStream stream = sftp.get(filename);
                    File targetFile = new File(localDirectory + fileSeparator + filename);
                    OutputStream outStream = new FileOutputStream(targetFile);

                    byte[] buffer = new byte[8 * 1024];
                    int bytesRead;
                    while ((bytesRead = stream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                    IOUtils.closeQuietly(stream);
                    IOUtils.closeQuietly(outStream);
                    if(shouldDeleteAfterDownloading)
                    {
                        sftp.rm(filename);
                    }
                }
            }

            session.disconnect();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public void run() {
        time += 1;
        connectFTP();
    }
}


@Component
@Configuration
@Getter
@Setter
public class SftpClient implements Runnable{


    @Value("${spring.ftp.host}")
    private String host;

    @Value("${spring.ftp.username}")
    private String username;

    @Value("${spring.ftp.password}")
    private String password;

    @Value("${spring.ftp.port}")
    private int port;

    @Value("${spring.ftp.remote.folder}")
    private String remoteDirectory;

    @Value("${spring.ftp.local.folder}")
    private String localDirectory;

    @Value("${spring.ftp.timeout}")
    private int timeout;

    @Value("${spring.ftp.delete_after_downloading}")
    private boolean shouldDeleteAfterDownloading;

    public SftpClient()
    {

    }

    @Override
    public void run() {
        try {

            Timer timer = new Timer(); // creating timer
            TimerTask task = new SftpTask(host, username, password, port, remoteDirectory,
                    localDirectory, shouldDeleteAfterDownloading); // creating timer task
            timer.scheduleAtFixedRate(task,new Date(),timeout * 1000L);

        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
