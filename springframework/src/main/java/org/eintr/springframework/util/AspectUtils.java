package org.eintr.springframework.util;

public class AspectUtils {

    public static String[] cutName(String name) {
        StringBuilder res=new StringBuilder("");
        String[] segString=name.split("\\.");
        for(int i=0;i< segString.length-1;i++) {
            res.append(segString[i]);
            if(i<segString.length-2)
                res.append(".");
        }
        return new String[]{res.toString(), segString[segString.length - 1]};
    }
}
