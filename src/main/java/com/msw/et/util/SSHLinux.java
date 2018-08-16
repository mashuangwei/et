package com.msw.et.util;

/**
 * 作者: mashuangwei
 * 日期: 2017/12/1
 */

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SSHLinux {

    private static Session session;
    private static  ChannelExec channelExec;

    public static void main(String[] args) throws IOException, JSchException {
        // TODO Auto-generated method stub
        String host = "localhost";
        int port = 22;
        String user = "root";
        String password = "1234";
        String command = "ls";
        String res = exeCommand(host,port,user,password,command);

        System.out.println(res);

    }

    public static boolean sshLogin(String host, int port, String user, String password){
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.setTimeout(999999999);
            session.connect();

        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static void sshLogout(){
        channelExec.disconnect();
        session.disconnect();
    }

    public static String  executeCommand(String command){
        String out = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();
            out = IOUtils.toString(in, "UTF-8");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return out;
    }

    public static String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {

        JSch jsch = new JSch();
        Session sessions = jsch.getSession(user, host, port);
        sessions.setConfig("StrictHostKeyChecking", "no");
        sessions.setPassword(password);
        sessions.setTimeout(999999999);

        sessions.connect();

        ChannelExec channelExecs = (ChannelExec) sessions.openChannel("exec");
        InputStream in = channelExecs.getInputStream();
        channelExecs.setCommand(command);
        channelExecs.setErrStream(System.err);
        channelExecs.connect();
        String out = IOUtils.toString(in, "UTF-8");

        channelExecs.disconnect();
        sessions.disconnect();

        return out;
    }

}
