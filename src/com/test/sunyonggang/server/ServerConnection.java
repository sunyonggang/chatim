package com.test.sunyonggang.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.test.sunyonggang.util.XMLUtil;

public class ServerConnection extends Thread
{
	private ServerSocket serverSocket;
	private Server server;
	public ServerConnection(Server server, int port)
	{
		try
		{
			this.server = server;
			this.serverSocket = new ServerSocket(port);
			this.server.getJLabel2().setText("����");
			this.server.getJButton().setEnabled(false);
			
			
			
			
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(this.server, "��ڱ�ռ��", "����", JOptionPane.CANCEL_OPTION);
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
//				System.out.println(socket.getPort());
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				
				//message from the client include username, 
				String loginXML = new String(buf, 0, length);
				String username = XMLUtil.extractUsername(loginXML);
				String loginResult = null;
				boolean isLogin = false;
				
				
				//�ж�����
				if(this.server.getMap().containsKey(username))
				{
					loginResult = "failure";
				}
				else
				{
					loginResult = "success";
					isLogin = true;
					
				}
				String xml = XMLUtil.constructLoginResultXML(loginResult);
				
				
//				System.out.println("username1111: " + username);
				
				os.write(xml.getBytes());
				if(isLogin)
				{
					ServerMessageThread serverMessageThread = new ServerMessageThread(this.server, socket);
					this.server.getMap().put(username, serverMessageThread);
					
					//�����û��б�
//					System.out.println("you are the best");
					serverMessageThread.updateUserList();
					serverMessageThread.start();
				}
				//׼�������µ��̣߳����ڴ����û����������ݣ�ôһ�������ϵ��û�����Ӧһ���߳�
				
				
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
