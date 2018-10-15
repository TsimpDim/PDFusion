package control;

public class WatermarkOptions {

    private int[] selectedFiles;
    private String wtrmkText;
    private Integer wtrmkPos;
    private Integer wtrmkRot;
    private Float wtrmkOpac;
    private Boolean wtrmkAllFiles;

    public WatermarkOptions(int[] selectedFiles, String wtrmkText, Integer wtrmkPos, Integer wtrmkRot, Float wtrmkOpac, Boolean wtrmkAllFiles) {
        this.selectedFiles = selectedFiles;
        this.wtrmkText = wtrmkText;
        this.wtrmkPos = wtrmkPos;
        this.wtrmkRot = wtrmkRot;
        this.wtrmkOpac = wtrmkOpac;
        this.wtrmkAllFiles = wtrmkAllFiles;
    }

    public int[] getSelectedFiles() {
        return selectedFiles;
    }

    public void setSelectedFiles(int[] selectedFiles) {
        this.selectedFiles = selectedFiles;
    }

    public String getWtrmkText() {
        return wtrmkText;
    }

    public void setWtrmkText(String wtrmkText) {
        this.wtrmkText = wtrmkText;
    }

    public Integer getWtrmkPos() {
        return wtrmkPos;
    }

    public void setWtrmkPos(Integer wtrmkPos) {
        this.wtrmkPos = wtrmkPos;
    }

    public Integer getWtrmkRot() {
        return wtrmkRot;
    }

    public void setWtrmkRot(Integer wtrmkRot) {
        this.wtrmkRot = wtrmkRot;
    }

    public Float getWtrmkOpac() {
        return wtrmkOpac;
    }

    public void setWtrmkOpac(Float wtrmkOpac) {
        this.wtrmkOpac = wtrmkOpac;
    }

    public Boolean getWtrmkAllFiles() {
        return wtrmkAllFiles;
    }

    public void setWtrmkAllFiles(Boolean wtrmkAllFiles) {
        this.wtrmkAllFiles = wtrmkAllFiles;
    }
}
