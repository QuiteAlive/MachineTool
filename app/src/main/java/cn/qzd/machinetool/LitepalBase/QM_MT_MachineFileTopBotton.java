package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/19.
 */

public class QM_MT_MachineFileTopBotton extends LitePalSupport{
    private String FileTop;
    private String FileBottom;
    private int FileId;
    private int IsChecked;

    public String getFileTop() {
        return FileTop;
    }

    public void setFileTop(String fileTop) {
        FileTop = fileTop;
    }

    public String getFileBottom() {
        return FileBottom;
    }

    public void setFileBottom(String fileBottom) {
        FileBottom = fileBottom;
    }

    public int getFileId() {
        return FileId;
    }

    public void setFileId(int fileId) {
        FileId = fileId;
    }

    public int getIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(int isChecked) {
        IsChecked = isChecked;
    }
}
