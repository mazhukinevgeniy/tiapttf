package homemade.utils;

import java.io.*;

//TODO: remove if it's not going to be used
public class Serializer<ObjectType extends Serializable> {
    public String serialize(ObjectType object) {
        String serializedObject = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(object);
            so.flush();
            serializedObject = bo.toString();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }

        return serializedObject;
    }

    public ObjectType deserialize(String serializedObject) {
        ObjectType object = null;
        try {
            byte b[] = serializedObject.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            object = (ObjectType) si.readObject();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }

        return object;
    }
}
