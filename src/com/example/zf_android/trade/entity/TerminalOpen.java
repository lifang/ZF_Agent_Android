package com.example.zf_android.trade.entity;

/**
 * Created by Leo on 2015/3/4.
 */
public class TerminalOpen {

	private String key;
	private String value;
	private int types;
	private int target_id;
	private int id;
	private int opening_applies_id;
	private long updated_at;
	private long created_at;
	private int opening_requirement_id;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getTypes() {
		return types;
	}

	public void setTypes(int types) {
		this.types = types;
	}

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpening_applies_id() {
        return opening_applies_id;
    }

    public void setOpening_applies_id(int opening_applies_id) {
        this.opening_applies_id = opening_applies_id;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getOpening_requirement_id() {
        return opening_requirement_id;
    }

    public void setOpening_requirement_id(int opening_requirement_id) {
        this.opening_requirement_id = opening_requirement_id;
    }
}
