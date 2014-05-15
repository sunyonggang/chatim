package com.test.sunyonggang.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import com.test.sunyonggang.util.CharacterUtil;
import com.test.sunyonggang.util.XMLUtil;

public class ClientConnection extends Thread
{
	private Client client;
	private String username;
	private String hostAddress;
	private int port;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private ChatClient chatClient;
	
	public ClientConnection(Client client, String username, String hostAddress, int port)
	{
		this.client = client;
		this.username = username;
		this.hostAddress = hostAddress;
		this.port = port;
		
		this.connect2Server();
	}

	
	public Socket getSocket()
	{
		return socket;
	}


	//connect to server
	private void connect2Server()
	{
		try
		{
			this.socket = new Socket(this.hostAddress, this.port);
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public boolean login()
	{
		try
		{
			String xml = XMLUtil.constructLoginXML(this.username);
			os.write(xml.getBytes());
//			String info = this.username;
//			os.write(info.getBytes());
			
			byte[] buf = new byte[5000];
			int length = is.read(buf);
			
			String loginResultXML = new String(buf, 0, length);
//			System.out.println("response: " + response);
			String loginResult = XMLUtil.extractLoginResult(loginResultXML);
			
			System.out.println(loginResult);
			if("success".equals(loginResult))
			{
				this.chatClient = new ChatClient(this);
				this.client.setVisible(false);
				return true;
			}
			else
			{
				return false;
			}
			
			
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public void run()
	{
		try
		{
			while(true)
			{
				byte[] buf = new byte[5000];
				int length = is.read(buf);
//				System.out.println("buf is " + buf);
				
				String xml = new String(buf, 0, length);
//				System.out.println("one two " + xml);
				
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				if(type == CharacterUtil.USER_LIST)
				{
					List<String> list = XMLUtil.extractUserList(xml);
					String users = "";
					
					for(String user : list)
					{
						users += user + "\n";
					}
					this.chatClient.getJTextArea2().setText(users);
				}
				
				else if(type == CharacterUtil.SERVER_MESSAGE)
				{
					String content = XMLUtil.extractContent(xml);
					this.chatClient.getJTextArea1().append(content + "\n");
					
					
				}
				else if(type == CharacterUtil.CLOSE_SERVER_WINDOW)
				{
					JOptionPane.showMessageDialog(this.chatClient, "服务器端关闭", "警告", JOptionPane.WARNING_MESSAGE);
					System.exit(0);
				}
				else if(type == CharacterUtil.CLOSE_CLIENT_WINDOW_CONFIRMATION)
				{
					try
					{
						this.getSocket().getInputStream();
						this.getSocket().getOutputStream();
						this.getSocket().close();
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						System.exit(0);
					}
				}
				
				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message, String type)
	{
		try
		{
			int t = Integer.parseInt(type);
//			System.out.println(t + "  type: " + type);
			String xml = null;
			
			if(CharacterUtil.CLIENT_MESSAGE == t)
			{
				xml = XMLUtil.constructMessageXML(this.username, message);
				
			}
			else if(CharacterUtil.CLOSE_CLIENT_WINDOW == t)
			{
				xml = XMLUtil.constructCloseClientWindowXML(username);
//				System.out.println(xml);
			}
			
			
			this.os.write(xml.getBytes());
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
