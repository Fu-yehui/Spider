package com.roger.QuestionBankSpider.enums;

public enum GradeEnum {
    PrimarySchool("1","小学"),
    MiddleSchool("2","初中"),
    HighSchool("3","高中");

    private String id;
    private String description;

    GradeEnum(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GradeEnum vauleOf(String id){
        for(GradeEnum gradeEnum:values()){
            if(gradeEnum.id.equals(id)){
                return gradeEnum;
            }
        }
        return null;
    }
}
