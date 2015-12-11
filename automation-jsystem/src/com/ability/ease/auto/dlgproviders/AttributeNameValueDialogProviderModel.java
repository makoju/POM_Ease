package com.ability.ease.auto.dlgproviders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import jsystem.extensions.paramproviders.BeanCellEditorModel;
import jsystem.framework.scenario.ProviderDataModel;
import jsystem.utils.beans.BeanElement;
import jsystem.utils.beans.CellEditorType;

import org.xml.sax.SAXException;

import com.ability.ease.auto.dataStructure.common.AttibuteXMLParser.CreateUIAttributeXMLFromMappings;
import com.ability.ease.auto.enums.common.UIAttributesXMLFileName;

public class AttributeNameValueDialogProviderModel extends BeanCellEditorModel implements ProviderDataModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		
	public AttributeNameValueDialogProviderModel(ArrayList<BeanElement> arg0,
			ArrayList<LinkedHashMap<String, String>> arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public CellEditorType getEditorType(JTable table, int row, int column) {
		String columnName = table.getColumnName(column);

		if (columnName.equalsIgnoreCase("AttributeName") || columnName.equalsIgnoreCase("newAttrName") || columnName.equalsIgnoreCase("description")) {
			return CellEditorType.LIST;
		}
		else {
			return super.getEditorType(table, row, column);
		}
	}
	
	@Override
	public String[] getOptions(JTable table, int row, int column) {		
		String columnName = table.getColumnName(column);
		if  (columnName.equalsIgnoreCase("AttributeName")) {
			//String epType = super.getValueAt(row, 0).toString();
			CreateUIAttributeXMLFromMappings mapping=new CreateUIAttributeXMLFromMappings();
			try {
				System.out.println("File name from getOptions method..."+UIAttributesXMLFileName.getUIAttributesxmlfileName());
				return mapping.CreateUIAttribuitesDescriptionPropsFromXML(UIAttributesXMLFileName.getUIAttributesxmlfileName().trim(),null);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if  (columnName.equalsIgnoreCase("description")) { 
     		
     		CreateUIAttributeXMLFromMappings mapping=new CreateUIAttributeXMLFromMappings();
     		
			try {
				String attrName = (String)super.getValueAt(row, 0);
				String[] displayNames = mapping.CreateUIAttribuitesDescriptionPropsFromXML(UIAttributesXMLFileName.getUIAttributesxmlfileName().trim(),"//UIAttribute//description/preceding-sibling::displayName"); 
				String[] description = mapping.CreateUIAttribuitesDescriptionPropsFromXML(UIAttributesXMLFileName.getUIAttributesxmlfileName().trim(),"//UIAttribute//description");
				
				Map<String,String> dispnamedescriptionmap = new HashMap<String,String>();
				 for(int i=0;i<description.length;i++)
				{
						dispnamedescriptionmap.put(displayNames[i], description[i]);
				}
				//super.setValueAt("abc",row,column); 
                if(dispnamedescriptionmap.containsKey(attrName.trim())){
                	return new String[]{dispnamedescriptionmap.get(attrName.trim())};
                }
				 
				return new String[]{""};
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new String[] { "" };
	}
		
	public static <T extends Enum<T>> String[] enumNameToStringArray(T[] values) {  
	    int i = 0;  
	    String[] result = new String[values.length];  
	    for (T value: values) {  
	        result[i++] = value.name();  
	    }  
	    return result;  
	}  
	
}
