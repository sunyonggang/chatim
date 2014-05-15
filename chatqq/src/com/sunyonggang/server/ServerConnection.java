package com.sunyonggang.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.sunyonggang.util.XMLUtil;

public class ServerConnection extends Thread
{
	public ServerConnection(Server server, int port)
	{
		try
		{
			this.server = server;
			this.serverSocket = new ServerSocket(port);
			this.server.getJlabel2().setText("运行");
			this.server.getJButton().setEnabled(false);
			
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.server, "端口号被占用！", "警告", JOptionPane.ERROR_MESSAGE);
		}
	}
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Socket socket = this.serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				
				String loginXML = new String(buf, 0, length);
				String username = XMLUtil.extractUsername(loginXML);
				String loginResult = null;
				
				boolean isLogin = false;
				
				if (this.server.getMap().containsKey(username))
				{
					loginResult = "failure";
				}
				else
				{
					loginResult = "success";
					isLogin = true;
				}
				
				String xml = XMLUtil.constructLoginResultXML(loginResult);
				os.write(xml.getBytes());
				
				if (isLogin)
				{
					ServerMessageThread serverMessageThread = new ServerMessageThread(this.server, socket);
					this.server.getMap().put(username, serverMessageThread);
					
					serverMessageThread.updateUserList();
					serverMessageThread.start();
				}
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	private ServerSocket serverSocket;
	private Server server;
	
}
