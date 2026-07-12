package Festival;

public class Ulaznica {
	private int id;
    private String tip;
    private double cena;

    public Ulaznica(int id, String tip, double cena) {
        this.id = id;
        this.tip = tip;
        this.cena = cena;
}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}}
