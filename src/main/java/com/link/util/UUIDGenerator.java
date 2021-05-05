package com.link.util;

import java.util.UUID;

/**
 * Static generateUniqueId method for other classes to us
 */
public class UUIDGenerator
{
    /**
     * Generates a new UUID and removes the - characters to make it a int
     *
     * @return random UUID int with 9-14 digits
     */
    public static int generateUniqueId()
    {
        UUID id = UUID.randomUUID();

        String idString = "" + id;
        int uid = idString.hashCode();

        String filteredString = "" + uid;
        idString = filteredString.replaceAll("-", "");

        return Integer.parseInt(idString);
    }
}
