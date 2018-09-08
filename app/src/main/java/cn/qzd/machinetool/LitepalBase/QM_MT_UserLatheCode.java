package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by mb on 2018/7/27.
 */

public class QM_MT_UserLatheCode extends LitePalSupport{
    private String FileId;
    private String CodeType;
    private String Number;
    private String Instruction;

    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    public String getCodeType() {
        return CodeType;
    }

    public void setCodeType(String codeType) {
        CodeType = codeType;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getInstruction() {
        return Instruction;
    }

    public void setInstruction(String instruction) {
        Instruction = instruction;
    }
}
