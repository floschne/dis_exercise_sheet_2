package de.dis2011.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Contract {
	
	private int id = -1;
	private int contractNumber;
	private Date startDate;
	private String place;
	
	public Contract() {
		
	}
	
	public Contract(int id, int contractNumber, Date startDate, String place) {
		this.id = id;
		this.contractNumber = contractNumber;
		this.startDate = startDate;
		this.place = place;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getContractNumber() {
		return contractNumber;
	}
	
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public void save() {
		// Hole Verbindung
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				// Achtung, hier wird noch ein Parameter mitgegeben,
				// damit spC$ter generierte IDs zurC<ckgeliefert werden!
				String insertSQL = "INSERT INTO contract(contract_no, start_date, place) VALUES (?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL,
						Statement.RETURN_GENERATED_KEYS);

				// Setze Anfrageparameter und fC<hre Anfrage aus
				pstmt.setInt(1, getContractNumber());
				pstmt.setDate(2, getStartDate());
				pstmt.setString(3, getPlace());
				pstmt.executeUpdate();

				// Hole die Id des engefC<gten Datensatzes
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					setId(rs.getInt(1));
				}

				rs.close();
				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE contract SET contract_no = ?, start_date = ?, place = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);
				
				pstmt.setInt(1, getContractNumber());
				pstmt.setDate(2, getStartDate());
				pstmt.setString(3, getPlace());
				pstmt.setInt(4, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Contract> getAll() {
		
		ArrayList<Contract> allContracts = new ArrayList<Contract>();
		
		Connection con = DB2ConnectionManager.getInstance().getConnection();

		try {
			String getAllSQL = "SELECT * FROM contracts";
			PreparedStatement pstmt = con.prepareStatement(getAllSQL);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				allContracts.add(new Contract(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getString(4)));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return allContracts;
	}
	
}
