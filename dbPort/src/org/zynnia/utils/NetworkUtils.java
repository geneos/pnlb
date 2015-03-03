/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zynnia.utils;

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import org.compiere.util.CLogger;

/**
 *
 * @author alejandro
 */
public final class NetworkUtils {

    /**	Logger							*/
    private static CLogger log = CLogger.getCLogger(NetworkUtils.class);
    private static String IP_ADDRESS = "";
    private static String HOSTNAME = "";

    public static String getIpAddress() {
        if (IP_ADDRESS.equals("")) {
            try {
                InetAddress mainLocal = InetAddress.getLocalHost();
                if (!mainLocal.isLoopbackAddress()) {
                    IP_ADDRESS = mainLocal.getHostAddress();
                    HOSTNAME = mainLocal.getHostName();
                } else { // Found first IP than not is loopback
                    Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
                    for (; n.hasMoreElements();) {
                        if (!IP_ADDRESS.equals("")) {
                            break;
                        }
                        NetworkInterface e = n.nextElement();
                        Enumeration<InetAddress> a = e.getInetAddresses();
                        for (; a.hasMoreElements();) {
                            InetAddress addr = a.nextElement();
                            // If not is loopback and is IP v4 for easy read for client
                            if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                                IP_ADDRESS = addr.getHostAddress();
                                HOSTNAME = addr.getHostName();
                                break;
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                log.log(Level.WARNING, "Can't determine IP for client");
            }
        }
        return IP_ADDRESS;
    }

    public static String getHostName() {
        if (IP_ADDRESS.equals("")) {
            getIpAddress();
        }
        return HOSTNAME;
    }

    public final static void main(String[] args) {
        try {
            System.out.println("Network info:");
            System.out.println("  IP/Localhost: " + getIpAddress());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}