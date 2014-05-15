package com.sunyonggang.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import com.sunyonggang.util.CharacterUtil;
import com.sunyonggang.util.XMLUtil;

public class ClientConnection extends Thread
{
	public ClientConnection(Client client, String hostAddress, int port, String username)
	{
		this.client = client;
		this.hostAddress = hostAddress;
		this.port = port;
		this.username = username;
		
		this.connect2Server();
	}
	public void connect2Server()
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
			
			byte[] buf = new byte[5000];
			int length = is.read(buf);
			
			String loginResultXML = new String(buf, 0, length);
			String loginResult = XMLUtil.extractLoginResult(loginResultXML);
			
			if ("success".equals(loginResult))
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
	
	public Socket getSocket()
	{
		return socket;
	}
	public void sendMessage(String message, String type)
	{
		try
		{
			int t = Integer.parseInt(type);
			String xml = null;
			
			if (CharacterUtil.CLIENT_MESSAGE == t)
			{
				xml = XMLUtil.constructMessageXML(this.username, message);
				
			}
			else if (CharacterUtil.CLOSE_CLIENT_WINDOW == t)
			{
				xml = XMLUtil.constructCloseClientWindowXML(this.username);
			}
			this.os.write(xml.getBytes());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				String xml = new String(buf, 0, length);
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				
				if (type == CharacterUtil.USER_LIST)
				{
				    List<String> list = XMLUtil.extractUserList(xml);
				    String users = "";
				    for (String user: list)
				    {
				    	users += user + "\n";
				    }
				    this.chatClient.getJTextArea2().setText(users);
				    
				}
				else if (type == CharacterUtil.SERVER_MESSAGE)
				{
					String content = XMLUtil.extractContent(xml);
					this.chatClient.getJTextArea1().append(content + "\n");
					
				}
				
				else if (type == CharacterUtil.CLOSE_SERVER_WINDOW)
				{
					JOptionPane.showMessageDialog(this.chatClient, "服务器端已关闭，程序将退出！", "message", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
				else if (type == CharacterUtil.CLOSE_CLIENT_WINDOW_CONFIRMATION)
				{
					try
					{
						this.getSocket().getInputStream().close();
						this.getSocket().getOutputStream().close();
						this.getSocket().close();
						
					} catch (Exception e)
					{
						
					}finally
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
	
	private String hostAddress;
	private int port;
	private String username;
	private Client client;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private ChatClient chatClient;
}
