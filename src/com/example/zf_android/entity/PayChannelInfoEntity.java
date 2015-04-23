package com.example.zf_android.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayChannelInfoEntity {
    private int id;
    private int factory_id;
    private int opening_cost;
    private String name;
    private String opening_datum;
    private String opening_protocol;
    private String opening_requirement;

    private boolean support_cancel_flag;
    private boolean support_type;

    private FactoryEntity pcfactory;

    @SerializedName("other_rate")
    private List<OtherRateEntity> other_rates;

    private List<RequireMaterialEntity> requireMaterial_pra;
    private List<RequireMaterialEntity> requireMaterial_pub;
    private List<StandardRateEntity> standard_rates;

    @SerializedName("supportArea")
    private List<String> supportAreas;

    private List<TDateEntity> tDates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFactory_id() {
        return factory_id;
    }

    public void setFactory_id(int factory_id) {
        this.factory_id = factory_id;
    }

    public int getOpening_cost() {
        return opening_cost;
    }

    public void setOpening_cost(int opening_cost) {
        this.opening_cost = opening_cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpening_datum() {
        return opening_datum;
    }

    public void setOpening_datum(String opening_datum) {
        this.opening_datum = opening_datum;
    }

    public String getOpening_protocol() {
        return opening_protocol;
    }

    public void setOpening_protocol(String opening_protocol) {
        this.opening_protocol = opening_protocol;
    }

    public String getOpening_requirement() {
        return opening_requirement;
    }

    public void setOpening_requirement(String opening_requirement) {
        this.opening_requirement = opening_requirement;
    }

    public boolean isSupport_cancel_flag() {
        return support_cancel_flag;
    }

    public void setSupport_cancel_flag(boolean support_cancel_flag) {
        this.support_cancel_flag = support_cancel_flag;
    }

    public boolean isSupport_type() {
        return support_type;
    }

    public void setSupport_type(boolean support_type) {
        this.support_type = support_type;
    }

    public List<OtherRateEntity> getOther_rates() {
        return other_rates;
    }

    public void setOther_rates(List<OtherRateEntity> other_rates) {
        this.other_rates = other_rates;
    }

    public List<RequireMaterialEntity> getRequireMaterial_pra() {
        return requireMaterial_pra;
    }

    public void setRequireMaterial_pra(List<RequireMaterialEntity> requireMaterial_pra) {
        this.requireMaterial_pra = requireMaterial_pra;
    }

    public List<RequireMaterialEntity> getRequireMaterial_pub() {
        return requireMaterial_pub;
    }

    public void setRequireMaterial_pub(List<RequireMaterialEntity> requireMaterial_pub) {
        this.requireMaterial_pub = requireMaterial_pub;
    }

    public List<StandardRateEntity> getStandard_rates() {
        return standard_rates;
    }

    public void setStandard_rates(List<StandardRateEntity> standard_rates) {
        this.standard_rates = standard_rates;
    }

    public List<String> getSupportAreas() {
        return supportAreas;
    }

    public void setSupportAreas(List<String> supportAreas) {
        this.supportAreas = supportAreas;
    }

    public List<TDateEntity> gettDates() {
        return tDates;
    }

    public void settDates(List<TDateEntity> tDates) {
        this.tDates = tDates;
    }

    public FactoryEntity getPcfactory() {
        return pcfactory;
    }

    public void setPcfactory(FactoryEntity pcfactory) {
        this.pcfactory = pcfactory;
    }
}
