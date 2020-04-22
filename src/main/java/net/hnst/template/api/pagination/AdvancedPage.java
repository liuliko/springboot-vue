package net.hnst.template.api.pagination;


import java.io.Serializable;

public class AdvancedPage extends Page implements Serializable {

    public AdvancedPage() {
    }

    private int iTotalDisplayRecords;
    private int iTotalRecords;
    private String sEcho;
    private Integer iDisplayStart;
    private Integer iDisplayLength;
    private String sSearch;

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getiTotalDisplayRecords() {
        iTotalDisplayRecords = this.getTotalResult();
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getiTotalRecords() {
        iTotalRecords = this.getTotalResult();
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public Integer getiDisplayStart() {
        return iDisplayStart == null ? 0 : iDisplayStart;
    }

    public void setiDisplayStart(Integer iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public Integer getiDisplayLength() {
        return iDisplayLength == null ? 10 : iDisplayLength;
    }

    public void setiDisplayLength(Integer iDisplayLength) {
        if (iDisplayLength != null) {
            this.iDisplayLength = iDisplayLength;
            super.setShowCount(iDisplayLength);
        } else {
            this.iDisplayLength = super.getShowCount();
        }
    }

    public int getCurrentResult() {
        if (iDisplayStart != null) {
            return iDisplayStart;
        }
        return super.getCurrentResult();
    }

    public void setCurrentResult(int currentResult) {
        super.setCurrentResult(this.getiDisplayStart());
    }

    public int getShowCount() {
        return this.getiDisplayLength();
    }

    public void setShowCount(int showCount) {
        super.setShowCount(this.getiDisplayLength());
    }

    public String getsSearch() {
        return sSearch;
    }

    public void setsSearch(String sSearch) {
        this.sSearch = sSearch;
    }


}
