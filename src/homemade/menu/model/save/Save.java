package homemade.menu.model.save;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by Marid on 28.03.2016.
 */
public class Save
{
    private String pathToFile;
    private Document xmlDocument;

    public Save() {}

    public Save(String path)
    {
        pathToFile = path;
        xmlDocument = getXMLDocument();
    }

    private Document getXMLDocument()
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            document = builder.parse(new File(pathToFile));
        } catch (Exception exception) {
            System.err.print("XML parsing error!");
        }

        return document;
    }

    public boolean isValid()
    {
        boolean valid = true;
        if (xmlDocument == null)
        {
            valid = false;
        }
        return valid;
    }

    //TODO refactoring
    public Integer getIntegerValue(String parameterName)
    {
        Node node = findNode(parameterName);
        Integer parameterValue = null;

        if(node != null)
        {
            String type = getAttributeValue(node, Attribute.TYPE);

            if (type.equals(ParameterType.INTEGER))
            {
                String value = getAttributeValue(node, Attribute.VALUE);
                parameterValue = Integer.valueOf(value);
            }
        }

        return parameterValue;
    }

    public Boolean getBooleanValue(String parameterName)
    {
        Node node = findNode(parameterName);
        Boolean parameterValue = null;

        if (node != null)
        {
            String type = getAttributeValue(node, Attribute.TYPE);

            if (type.equals(ParameterType.BOOLEAN))
            {
                String value = getAttributeValue(node, Attribute.VALUE);
                parameterValue = Boolean.valueOf(value);
            }
        }
        return parameterValue;
    }

    public void setParameterValue(String parameterName, Object newValue)
    {
        Node node = findNode(parameterName);

        if (node != null)
        {
            String stringValue = newValue.toString();
            Node attribute = getAttribute(node, Attribute.VALUE);
            attribute.setNodeValue(stringValue);

            saveDocument();
        }
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
        Node attribute = getAttribute(node, attributeName);
        String value = null;
        if (attribute != null)
        {
            value = attribute.getNodeValue();
        }

        return value;
    }

    private Node getAttribute(Node node, String attributeName)
    {
        NamedNodeMap attributes = node.getAttributes();
        Node attribute = null;
        if (attributes != null)
        {
            attribute = attributes.getNamedItem(attributeName);
        }

        return attribute;
    }

    private void saveDocument()
    {
        try
        {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(pathToFile));
            Source input = new DOMSource(xmlDocument);

            transformer.transform(input, output);
        }
        catch (Exception exception)
        {
            System.err.print("XML save error!");
        }
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
