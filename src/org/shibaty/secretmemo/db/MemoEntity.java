/**
 * @author shibaty
 */
package org.shibaty.secretmemo.db;

import java.io.Serializable;
import java.util.Date;

/**
 * メモのエンティティクラス.
 * @author shibaty
 */
public class MemoEntity implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -3185418391571351165L;

    /** 未登録ID. */
    public static final int ID_NONE = -1;

    /** ID. */
    private int id = ID_NONE;
    /** ジャンル. */
    private String genre;
    /** メモ内容. */
    private String memo;
    /** 作成日. */
    private Date createtime;
    /** 更新日. */
    private Date updatetime;

    /**
     * IDを返却.
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * ジャンルを返却.
     * @return ジャンル
     */
    public String getGenre() {
        return genre;
    }

    /**
     * メモ内容を返却.
     * @return メモ内容
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 作成日を返却.
     * @return 作成日
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 更新日を返却.
     * @return 更新日
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * @param id セットする id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param genre セットする genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @param memo セットする memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @param createtime セットする createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * @param updatetime セットする updatetime
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

}
