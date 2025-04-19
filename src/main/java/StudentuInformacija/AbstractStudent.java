package StudentuInformacija;

public abstract class AbstractStudent implements StudentInterface {
    protected String vardas;
    protected String pavarde;
    protected String grupe;

    public AbstractStudent(String vardas, String pavarde, String grupe) {
        this.vardas = vardas;
        this.pavarde = pavarde;
        this.grupe = grupe;
    }
}