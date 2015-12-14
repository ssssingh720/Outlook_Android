package com.app.outlook.modal;

/**
 * Created by Madhumita on 25-11-2015.
 */
public class NotificationVo {
    private String msg;
    private String quarterly_sku;

    private String annual_sku;

    private String magazine_id;

    private String half_yearly_sku;

    private String magazine_title;

    public String getQuarterly_sku ()
    {
        return quarterly_sku;
    }

    public void setQuarterly_sku (String quarterly_sku)
    {
        this.quarterly_sku = quarterly_sku;
    }

    public String getAnnual_sku ()
    {
        return annual_sku;
    }

    public void setAnnual_sku (String annual_sku)
    {
        this.annual_sku = annual_sku;
    }

    public String getMagazine_id ()
    {
        return magazine_id;
    }

    public void setMagazine_id (String magazine_id)
    {
        this.magazine_id = magazine_id;
    }

    public String getHalf_yearly_sku ()
    {
        return half_yearly_sku;
    }

    public void setHalf_yearly_sku (String half_yearly_sku)
    {
        this.half_yearly_sku = half_yearly_sku;
    }

    public String getMagazine_title ()
    {
        return magazine_title;
    }

    public void setMagazine_title (String magazine_title)
    {
        this.magazine_title = magazine_title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [msg="+msg+", quarterly_sku = "+quarterly_sku+", annual_sku = "+annual_sku+", magazine_id = "+magazine_id+", half_yearly_sku = "+half_yearly_sku+", magazine_title = "+magazine_title+"]";
    }
}
