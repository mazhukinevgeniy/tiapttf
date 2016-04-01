package homemade.menu.model.save;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by Marid on 28.03.2016.
 */
public class Save
{
    private String pathToFile;
    private Document xmlDocument;

    public Save() {}

    public Save(String path) throws Exception
    {
        pathToFile = path;
        xmlDocument = getXMLDocument();
    }

    private Document getXMLDocument() throws Exception
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new File(pathToFile));
        } catch (Exception exception) {
            String message = "XML parsing error!";
            throw new Exception(message);
        }
    }

    //TODO refactoring
    public Integer getIntegerValue(String parameterName)
    {
        Node node = findNode(parameterName);
        String type = getAttributeValue(node, Attribute.TYPE);

        Integer parameterValue = null;
        if (type.equals(ParameterType.INTEGER))
        {
            String value = getAttributeValue(node, Attribute.VALUE);
            parameterValue = new Integer(value);
        }

        return parameterValue;
    }

    public Boolean getBooleanValue(String parameterName)
    {
        Node node = findNode(parameterName);
        String type = getAttributeValue(node, Attribute.TYPE);

        Boolean parameterValue = null;
        if (type.equals(ParameterType.BOOLEAN))
        {
            String value = getAttributeValue(node, Attribute.VALUE);
            parameterValue = Boolean.valueOf(value);
        }

        return parameterValue;
    }

    private Node findNode(String parameterName)
    {
        Node mainNode = xmlDocument.getChildNodes().item(0);
        Node sought = null;

        NodeList children = mainNode.getChildNodes();
        int numberOfChild = children.getLength();
        for (int i = 0; i < numberOfChild; ++i) {
            Node node = children.item(i);
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null)
            {
                Node nameAttrib = attributes.getNamedItem(Attribute.NAME);
                String name = nameAttrib.getNodeValue();
                if (name.equals(parameterName))
                {
                    sought = node;
                    break;
                }
            }
        }

        return sought;
    }

    private String getAttributeValue(Node node, String attributeName)
    {
        NamedNodeMap attributes = node.getAttributes();
        Node attribute = attributes.getNamedItem(attributeName);

        String value = null;
        if (attribute != null)
        {
            value = attribute.getNodeValue();
        }

        return value;
    }

    public final class ParameterType
    {
        public static final String INTEGER = "Integer";
        public static final String BOOLEAN = "Boolean";
        public static final String STRING = "String";
        public static final String DOUBLE = "Double";
    }

    public final class Attribute
    {
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String VALUE = "value";
    }
}
