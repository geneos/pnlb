/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.DB;
import org.zynnia.utils.Version;

/**
 *
 * @author alejandro
 */
public class MZynUserVersion extends X_ZYN_USER_VERSION {

    /**
	 * 	Default Constructor
	 *	@param ctx context
	 */
	public MZynUserVersion(Properties ctx, int ZYN_USER_VERSION_ID, String trxName) {
		super(ctx, ZYN_USER_VERSION_ID, trxName);
	}

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MZynUserVersion(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

    public void setClientVersion(Version version) {
        setCLIENT_VERSION(version.toString());
    }

    public Version getClientVersion() {
        return Version.parse(this.getCLIENT_VERSION());
    }

    public static MZynUserVersion getInstanceFor(Properties ctx, String hostName, String clientIP) {
        int ZYN_USER_VERSION_ID = DB.getSQLValue(null,
			"SELECT ZYN_USER_VERSION_ID FROM ZYN_USER_VERSION WHERE USER_ADDRESS=?", clientIP);
        if (ZYN_USER_VERSION_ID != -1) {
            return new MZynUserVersion(ctx, ZYN_USER_VERSION_ID, "newInstanceClientVersion");
        }
        return null;
    }
}
