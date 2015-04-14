package com.example.zf_android.entity;

/**
 * Created by holin on 4/14/15.
 */
public class BaseEntity {
    private boolean isDeleting;
    private boolean isBatchEditing;
    private boolean isSelected;
    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public boolean isDeleting() {
        return isDeleting;
    }

    public void setDeleting(boolean isDeleting) {
        this.isDeleting = isDeleting;
    }

    public boolean isBatchEditing() {
        return isBatchEditing;
    }

    public void setBatchEditing(boolean isBatchEditing) {
        this.isBatchEditing = isBatchEditing;
    }
}
