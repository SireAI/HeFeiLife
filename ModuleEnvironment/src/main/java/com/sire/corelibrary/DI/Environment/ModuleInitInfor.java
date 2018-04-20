package com.sire.corelibrary.DI.Environment;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ModuleInitInfor {
    private String MoudleName;
    private String extraInfor;

    public ModuleInitInfor(String moudleName, String extraInfor) {
        MoudleName = moudleName;
        this.extraInfor = extraInfor;
    }

    public String getMoudleName() {
        return MoudleName;
    }

    public void setMoudleName(String moudleName) {
        MoudleName = moudleName;
    }

    public String getExtraInfor() {
        return extraInfor;
    }

    public void setExtraInfor(String extraInfor) {
        this.extraInfor = extraInfor;
    }

    @Override
    public String toString() {
        return "ModuleInitInfor{" +
                "MoudleName='" + MoudleName + '\'' +
                ", extraInfor='" + extraInfor + '\'' +
                '}';
    }
}
