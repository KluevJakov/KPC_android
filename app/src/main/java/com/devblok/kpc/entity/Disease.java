package com.devblok.kpc.entity;

import java.util.Date;
import java.util.UUID;

public class Disease {
    protected UUID id;

    protected Date dateStart; //дата возникновения
    protected Date dateEnd; //дата окончания
    protected Date dateToTherapy; //дата поступления на стационарное лечение
    protected String firstDiagnosis; //первоначальный диагноз
    protected String secondDiagnosis; //последующий диагноз
    protected String anamnesis; //анамнез

    /* ----------------------------------------- */
    protected float temperature; //температура
    protected int pulse; //пульс
    protected int breath; //дыхание
    protected String commonHealth; //общее состояние
    protected String fatness; //упитанность

    /* -----------------состояние--------------- */
    protected String externalSkinStatus; //наружных покровов
    protected String internalShellStatus; //наружных покровов
    protected String lymphStatus; //наружных покровов
    /* ---------------исследование-------------- */
    protected String gastroSystemResearch; //пищеварительной
    protected String breathSystemResearch; //дыхательной
    protected String heartSystemResearch; //сердечно-сосудистой
    protected String nervousSystemResearch; //нервной
    protected String urogenitalSystemResearch; //мочеполовой

    /* ----------------------------------------- */

    protected Sick sick; //диагноз

    protected User curator; //куратор

    protected Animal animal; //кто болел

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateToTherapy() {
        return dateToTherapy;
    }

    public void setDateToTherapy(Date dateToTherapy) {
        this.dateToTherapy = dateToTherapy;
    }

    public String getFirstDiagnosis() {
        return firstDiagnosis;
    }

    public void setFirstDiagnosis(String firstDiagnosis) {
        this.firstDiagnosis = firstDiagnosis;
    }

    public String getSecondDiagnosis() {
        return secondDiagnosis;
    }

    public void setSecondDiagnosis(String secondDiagnosis) {
        this.secondDiagnosis = secondDiagnosis;
    }

    public String getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis = anamnesis;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getBreath() {
        return breath;
    }

    public void setBreath(int breath) {
        this.breath = breath;
    }

    public String getCommonHealth() {
        return commonHealth;
    }

    public void setCommonHealth(String commonHealth) {
        this.commonHealth = commonHealth;
    }

    public String getFatness() {
        return fatness;
    }

    public void setFatness(String fatness) {
        this.fatness = fatness;
    }

    public String getExternalSkinStatus() {
        return externalSkinStatus;
    }

    public void setExternalSkinStatus(String externalSkinStatus) {
        this.externalSkinStatus = externalSkinStatus;
    }

    public String getInternalShellStatus() {
        return internalShellStatus;
    }

    public void setInternalShellStatus(String internalShellStatus) {
        this.internalShellStatus = internalShellStatus;
    }

    public String getLymphStatus() {
        return lymphStatus;
    }

    public void setLymphStatus(String lymphStatus) {
        this.lymphStatus = lymphStatus;
    }

    public String getBreathSystemResearch() {
        return breathSystemResearch;
    }

    public void setBreathSystemResearch(String breathSystemResearch) {
        this.breathSystemResearch = breathSystemResearch;
    }

    public String getHeartSystemResearch() {
        return heartSystemResearch;
    }

    public void setHeartSystemResearch(String heartSystemResearch) {
        this.heartSystemResearch = heartSystemResearch;
    }

    public String getNervousSystemResearch() {
        return nervousSystemResearch;
    }

    public void setNervousSystemResearch(String nervousSystemResearch) {
        this.nervousSystemResearch = nervousSystemResearch;
    }

    public String getUrogenitalSystemResearch() {
        return urogenitalSystemResearch;
    }

    public void setUrogenitalSystemResearch(String urogenitalSystemResearch) {
        this.urogenitalSystemResearch = urogenitalSystemResearch;
    }

    public Sick getSick() {
        return sick;
    }

    public void setSick(Sick sick) {
        this.sick = sick;
    }

    public User getCurator() {
        return curator;
    }

    public void setCurator(User curator) {
        this.curator = curator;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getGastroSystemResearch() {
        return gastroSystemResearch;
    }

    public void setGastroSystemResearch(String gastroSystemResearch) {
        this.gastroSystemResearch = gastroSystemResearch;
    }
}
