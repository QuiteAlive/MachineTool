package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/19.
 */

public class QM_MT_SaveFtp extends LitePalSupport{
    private int FileId;
    private String IP;
    private String Port;

    public int getFileId() {
        return FileId;
    }

    public void setFileId(int fileId) {
        FileId = fileId;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }
}
