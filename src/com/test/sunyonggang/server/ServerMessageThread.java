package com.test.sunyonggang.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.test.sunyonggang.util.CharacterUtil;
import com.test.sunyonggang.util.XMLUtil;

public class ServerMessageThread extends Thread
{
	private Server server;
	private  InputStream is;
	private  OutputStream os;
	public ServerMessageThread(Server server, Socket socket)
	{
		try
		{
			this.server = server;
			this.is = socket.getInputStream();
			this.os = socket.getOutputStream();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	//更新用户列表，首先服务端，然后客户端
	public void updateUserList()
	{
		Set<String> users = this.server.getMap().keySet();
		
		String xml = XMLUtil.constructUserList(users);
		
//		System.out.println(users);
		String str = "";
		for(String user: users)
		{
			str += user + "\n";
		}
//		System.out.println(str);
		this.server.getJTextArea().setText(str);
		
		Collection<ServerMessageThread> cols = this.server.getMap().values();
		
		for(ServerMessageThread smt : cols)
		{
			smt.sendMessage(xml);
		}
		
	}
	public void sendMessage(String message)
	{
		try
		{
			os.write(message.getBytes());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				byte[] buf = new byte[5000];
				int length = this.is.read(buf);
				
				String xml = new String(buf, 0, length);
				
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				
//				System.out.println("nothing os " + type);
				if(CharacterUtil.CLIENT_MESSAGE == type)
				{
					String username = XMLUtil.extractUsername(xml);
					String content = XMLUtil.extractContent(xml);
					
					String message = username + " : " + content;
					
					String messageXML = XMLUtil.constructServerMessageXML(message);
					
					Map<String, ServerMessageThread> map = this.server.getMap();
					
					Collection<ServerMessageThread> cols = map.values();
					
					for(ServerMessageThread smt : cols)
					{
						smt.sendMessage(messageXML);
					}
					
				}
				else if(CharacterUtil.CLOSE_CLIENT_WINDOW == type)
				{
//					System.out.println(xml);
					String username = XMLUtil.extractUsername(xml);
					ServerMessageThread smt = this.server.getMap().get(username);
					
					String confirmationXML = XMLUtil.constructCloseClientWindowConfirmationXML();
					smt.sendMessage(confirmationXML);
					
					
					this.server.getMap().remove(username);
					this.updateUserList();
			
					this.is.close();
					this.os.close();
					break; //结束该线程
					
					
				}
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
