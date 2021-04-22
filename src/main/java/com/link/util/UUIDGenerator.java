package com.link.util;

import java.util.UUID;

/**
 * Helper functions for other classes
 *
 * - UUID int generation
 */
public class UUIDGenerator
{
    public static int generateUniqueId()
    {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
}
