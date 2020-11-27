package com.github.oxal.JMysql.type;

import com.github.oxal.JBDD.JBDDType;
import com.sun.istack.internal.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class JMysqlType implements JBDDType {

    private String name;

    public static final String VARCHAR = "VARCHAR";
    public static final String INT = "INT";
    public static final String DOUBLE = "DOUBLE";
    public static final String FLOAT = "FLOAT";
    public static final String TEXT = "TEXT";
    public static final String BOOL = "TINYINT";


    public static final String ARRAY_SEPARATOR = ";";
    public static final String REPLACE_ARRAY_SEPARATOR = "\\;";
    public static final String STRING_QUOTE = "\'";
    public static final String REPLACE_STRING_QUOTE = "\\'";

    public final static JMysqlType TYPE_VARCHAR = new JMysqlType(VARCHAR);
    public final static JMysqlType TYPE_INT = new JMysqlType(INT);
    public final static JMysqlType TYPE_DOUBLE = new JMysqlType(DOUBLE);
    public final static JMysqlType TYPE_FLOAT = new JMysqlType(FLOAT);
    public final static JMysqlType TYPE_TEXT = new JMysqlType(TEXT);
    public final static JMysqlType TYPE_BOOL = new JMysqlType(BOOL);
    public final static JMysqlType TYPE_ARRAY = new JMysqlType(TEXT);


    /**
     * créé un type mysql en fonction de son nom ex : varchar
     * des variables pour les noms sont disponible dans la class JMysqlType
     * @param name
     */
    public JMysqlType(@NotNull String name) {
        this.name = name;
    }

    /**
     * essaye de reconnaitre un le type de l'objet et le transcrit en type mysql
     * @param o
     */
    public static Optional<JMysqlType> toJMysqlType(Object o) {
        JMysqlType type = null;
        if (o instanceof Integer || o instanceof Long){
            type = new JMysqlType(INT);
        }else if (o instanceof Float){
            type = new JMysqlType(FLOAT);
        }else if (o instanceof Double){
            type = new JMysqlType(DOUBLE);
        }else if (o instanceof String){
            type = new JMysqlType(VARCHAR);
        }else if (o instanceof Boolean){
            type = new JMysqlType(BOOL);
        }else if(o instanceof Number[] || o instanceof String[]){
            type = new JMysqlType(TEXT);
        }else {
            System.out.println(o);
        }

        return Optional.of(type);
    }

    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getJBDDTypeString(Object o) {
        String name = null;
        if (o instanceof Integer || o instanceof Long){
            name = INT;
        }else if (o instanceof Float){
            name = FLOAT;
        }else if (o instanceof Double){
            name = DOUBLE;
        }else if (o instanceof String){
            name = VARCHAR;
        }else if (o instanceof Boolean){
            name = BOOL;
        }else if(o instanceof Number[] || o instanceof String[] || o instanceof Boolean[]){
            name = TEXT;
        }else {
            System.out.println(o);
        }

        return Optional.of(name);
    }

    @Override
    public Optional<JMysqlType> getJBDDType(Object o) {
        JMysqlType jMysqlType = null;
        Optional<String> name = getJBDDTypeString(o);
        if (name.isPresent()){
            jMysqlType = new JMysqlType(name.get());
        }
        return Optional.of(jMysqlType);
    }

    @Override
    public String toString() {
        return name;
    }

    public static Optional<String> toInsert(Object o){
        String insert = null;
        if (o instanceof Number){
            insert = o.toString();
        }else if (o instanceof String){
            insert = ((String)o).replace(STRING_QUOTE, REPLACE_STRING_QUOTE);
        }else if (o instanceof Boolean){
            insert = (Boolean) o ? 1 + "": 0 + "";
        }else if(o instanceof Number[]){
            insert = Arrays
                    .stream(((Number[])o))
                    .map(a -> a + "")
                    .collect(Collectors.joining(ARRAY_SEPARATOR, STRING_QUOTE + o.getClass().getSimpleName() + ARRAY_SEPARATOR, STRING_QUOTE));
        }else if(o instanceof String[]){
            insert = Arrays
                    .stream(((String[])o))
                    .map(a -> a.replace(ARRAY_SEPARATOR, REPLACE_ARRAY_SEPARATOR))
                    .collect(Collectors.joining(ARRAY_SEPARATOR, STRING_QUOTE + o.getClass().getSimpleName() + ARRAY_SEPARATOR, STRING_QUOTE));
        }else if(o instanceof Boolean[]){
            insert = Arrays
                    .stream(((Boolean[])o))
                    .map(a -> o + "")
                    .collect(Collectors.joining(ARRAY_SEPARATOR, STRING_QUOTE + o.getClass().getSimpleName() + ARRAY_SEPARATOR, STRING_QUOTE));
        }else {
            System.out.println(o);
        }

        return Optional.of(insert);
    }

    public static Object[] getArray(String text){
        String[] fakeArray = text.substring(1, text.length() - 1).split(ARRAY_SEPARATOR);
        Object[] array = new Object[fakeArray.length - 1];
        String nameClass = fakeArray[0];
        if(Integer[].class.getSimpleName().equals(nameClass) || int[].class.getSimpleName().equals(nameClass)){
            for (int i = 1; i < array.length; i++){
                array[i-1] = Integer.parseInt(fakeArray[i]);
            }
        }else if(Long[].class.getSimpleName().equals(nameClass) || long[].class.getSimpleName().equals(nameClass)){
            for (int i = 1; i < array.length; i++){
                array[i-1] = Long.parseLong(fakeArray[i]);
            }
        }else if(Float[].class.getSimpleName().equals(nameClass) || float[].class.getSimpleName().equals(nameClass)){
            for (int i = 1; i < array.length; i++){
                array[i-1] = Float.parseFloat(fakeArray[i]);
            }
        }else if(Double[].class.getSimpleName().equals(nameClass) || double[].class.getSimpleName().equals(nameClass)){
            for (int i = 1; i < array.length; i++){
                array[i-1] = Double.parseDouble(fakeArray[i]);
            }
        }else if(Boolean[].class.getSimpleName().equals(nameClass) || Boolean[].class.getSimpleName().equals(nameClass)){
            for (int i = 1; i < array.length; i++){
                array[i-1] = Boolean.parseBoolean(fakeArray[i]);
            }
        }else if(String[].class.getSimpleName().equals(nameClass)){
            for (int i = 1; i < array.length; i++){
                array[i-1] = fakeArray[i].replace(REPLACE_STRING_QUOTE, STRING_QUOTE);
            }
        }else {
            System.out.println(text);
        }

        return array;
    }

    public static Integer[] getIntArray(String text){
        return Arrays.stream(getArray(text)).collect(Collectors.toList()).toArray(new Integer[]{});
    }

    public static Long[] getLongArray(String text){
        return Arrays.stream(getArray(text)).collect(Collectors.toList()).toArray(new Long[]{});
    }

    public static Float[] getFloatArray(String text){
        return Arrays.stream(getArray(text)).collect(Collectors.toList()).toArray(new Float[]{});
    }

    public static Double[] getDoubleArray(String text){
        return Arrays.stream(getArray(text)).collect(Collectors.toList()).toArray(new Double[]{});
    }

    public static Boolean[] getBooleanArray(String text){
        return Arrays.stream(getArray(text)).collect(Collectors.toList()).toArray(new Boolean[]{});
    }

    public static String[] getStringArray(String text){
        return Arrays.stream(getArray(text)).collect(Collectors.toList()).toArray(new String[]{});
    }
}
