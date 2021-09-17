package techsoft.util;

public enum DayOfWeek implements Comparable<DayOfWeek> {
    
    MONDAY   (1, "Seg", "Segunda-feira"),
    TUESDAY  (2, "Ter", "Terça-feira"),
    WEDNESDAY(3, "Qua", "Quarta-feira"),
    THURSDAY (4, "Qui", "Quinta-feira"),
    FRIDAY   (5, "Sex", "Sexta-feira"),
    SATURDAY (6, "Sáb", "Sábado"),
    SUNDAY   (7, "Dom", "Domingo");
    
    private final int code;
    private final String description;
    private final String shortName;
    
    DayOfWeek(int code, String shortName, String description) {
        this.code = code;
        this.shortName = shortName;
        this.description = description;
    }
    
    public int getCode() { return this.code; }
    public String getShortName() { return this.shortName; }
    public String getDescription() { return this.description; }

    public static DayOfWeek forCode(int i) {
        for (DayOfWeek d : DayOfWeek.values()) if (d.code == i) return d;
        return null;
    }

}
