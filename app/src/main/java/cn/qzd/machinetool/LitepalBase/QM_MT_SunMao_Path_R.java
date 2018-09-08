package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/17.
 */

public class QM_MT_SunMao_Path_R extends LitePalSupport {
    private String SunMaoNameNo;
    private String PathName;
    private int path_r_id;

    public int getPath_r_id() {
        return path_r_id;
    }

    public void setPath_r_id(int path_r_id) {
        this.path_r_id = path_r_id;
    }

    public String getSunMaoNameNo() {
        return SunMaoNameNo;
    }

    public void setSunMaoNameNo(String sunMaoNameNo) {
        SunMaoNameNo = sunMaoNameNo;
    }

    public String getPathName() {
        return PathName;
    }

    public void setPathName(String pathName) {
        PathName = pathName;
    }
}
