package com.test.sunyonggang.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * login:1
 * client message : 2
 * server message : 3
 * user list : 4
 * close client window: 5
 * close server window: 6
 * close client window confirmation: 7
 * login result:8
 * @author sunyonggang
 *
 */
public class XMLUtil
{
	private static Document constructDocument()
	{
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("message");
		document.setRootElement(root);
		
		return document;
	}
	/**
	 * �ͻ��˵�½��������˷���xml����
	 * @return
	 */
	public static String constructLoginXML(String username)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("1");
		Element user = root.addElement("user");
		user.setText(username);
		
		return document.asXML();
	}
	/**
	 * �ӿͻ��˵�½���͵�xml�н����û���
	 */
	public static String extractUsername(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element user = document.getRootElement().element("user");
			return user.getText();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ����ͻ��˷��͵������û��б��xml����
	 */
	public static String constructUserList(Set<String> users)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("4");
		
		for(String user:users)
		{
			Element e = root.addElement("user");
			e.setText(user);
		}
		return document.asXML();
				
	}
	/**
 	 * ��XML��Ϣ����ȡ�����е������û��б���Ϣ
	 */
	
	public static List<String> extractUserList(String xml)
	{
		List<String> list = new ArrayList<String>();
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			for(Iterator iter = document.getRootElement().elementIterator("user"); iter.hasNext();)
			{
				Element e = (Element)iter.next();
				list.add(e.getText());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * ��xml�л�ȡ��������
	 * @param xml
	 * @return
	 */
	
	public static String extractType(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element typeElement = document.getRootElement().element("type");
			
			return typeElement.getText();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ������Ϣ�ṹ
	 * @param username
	 * @param message
	 * @return
	 */
	public static String constructMessageXML(String username, String message)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("2");
		Element user = root.addElement("user");
		user.setText(username);
		
		Element content = root.addElement("content");
		content.setText(message);
		
		return document.asXML();
	
	}
	
	/**
	 * ����������������пͻ��˷��͵�����
	 */
	public static String constructServerMessageXML(String message)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("3");
		
		Element content = root.addElement("content");
		content.setText(message);
		
		return document.asXML();
		
		
	}
	
	
	/**
	 * �ӿͻ��������˷��͵�xml���������н�����xml����
	 */
	public static String extractContent(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element contentElement = document.getRootElement().element("content");
			
			return contentElement.getText();
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ����������˴��ڹرյ�xml����
	 */
	public static String constructCloseServerWindowXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("6");
		
		return document.asXML();
	}
	/**
	 * ����ͻ��˹رմ��ڵ�XML����
	 */
	public static String constructCloseClientWindowXML(String username)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("5");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		return document.asXML();
		
	}
	//��������ȷ�Ͽͻ��˹رյ���Ϣ
	
	public static String constructCloseClientWindowConfirmationXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("7");
		
		return document.asXML();
	}
	
	/**
	 * ����ͻ��˷��ؽ����½��xml
	 */
	public static String constructLoginResultXML(String result)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("8");
		
		Element resultElement = root.addElement("result");
		resultElement.setText(result);
		
		return document.asXML();
	}
	/**
	 * ��xml�н��������
	 */
	public static String extractLoginResult(String xml)
	{
		String result = null;
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element root = document.getRootElement();
			result = root.element("result").getText();
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
}
