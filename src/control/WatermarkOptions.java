package control;

public class WatermarkOptions {

    private int[] selectedFiles;
    private String wtrmkText;
    private Integer wtrmkPos;
    private Integer wtrmkRot;
    private Float wtrmkOpac;
    private Integer wtrmkFontSize;
    private Boolean wtrmkAllFiles;

    public WatermarkOptions(int[] selectedFiles, String wtrmkText, Integer wtrmkPos, Integer wtrmkRot, Float wtrmkOpac, Integer wtrmkFontSize, Boolean wtrmkAllFiles) {
        this.selectedFiles = selectedFiles;
        this.wtrmkText = wtrmkText;
        this.wtrmkPos = wtrmkPos;
        this.wtrmkRot = wtrmkRot;
        this.wtrmkOpac = wtrmkOpac;
        this.wtrmkFontSize = wtrmkFontSize;
        this.wtrmkAllFiles = wtrmkAllFiles;
    }

    public int[] getSelectedFiles() {
        return selectedFiles;
    }

    public String getWtrmkText() {
        return wtrmkText;
    }

    public Integer getWtrmkPos() {
        return wtrmkPos;
    }

    public Integer getWtrmkRot() {
        return wtrmkRot;
    }

    public Float getWtrmkOpac() {
        return wtrmkOpac;
    }

    public Integer getWtrmkFontSize() {
        return wtrmkFontSize;
    }

    public Boolean getWtrmkAllFiles() {
        return wtrmkAllFiles;
    }
}
