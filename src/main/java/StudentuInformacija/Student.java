package StudentuInformacija;

public class Student extends AbstractStudent
{
    public Student(String vardas, String pavarde, String grupe) {
        super(vardas, pavarde, grupe);
    }

    @Override
    public String getVardas() { return vardas; }

    @Override
    public String getPavarde() { return pavarde; }

    @Override
    public String getGrupe() { return grupe; }

    public void setVardas(String vardas) { this.vardas = vardas; }
    public void setPavarde(String pavarde) { this.pavarde = pavarde; }
    public void setGrupe(String grupe) { this.grupe = grupe; }
}
