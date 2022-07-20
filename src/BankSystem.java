import java.sql.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class BankSystem {

	public static void main(String args[]) throws IOException {
		Scanner Sc = new Scanner(System.in);
		Connection con = null;
		Statement stmt = null;
		// Declare common variables if any
		try {
			// Load the driver class
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			// Create the connection object
			String conurl = "jdbc:sqlserver://172.17.144.108;databaseName=S1941017099";
			con = DriverManager.getConnection(conurl, "S1941017099", "Iter1234#");
			stmt = con.createStatement();
			int choice_variable;
			do {
				System.out.println("\n\n***** Banking Management System*****");
				// Display the menu
				System.out.println("Enter your choice(1-9):");
				System.out.println("1. Display Customer Records.");
				System.out.println("2. Add Customer Records.");
				System.out.println("3. Delete Customer Records.");
				System.out.println("4. Update Customer Records.");
				System.out.println("5. Display account details.");
				System.out.println("6. Display Loan details.");
				System.out.println("7. Deposit Money.");
				System.out.println("8. Withdraw Money.");
				System.out.println("9. Exit.");
				// Accept user's choice
				choice_variable = Sc.nextInt();
				switch (choice_variable) {
				case 1:
					// Display customer records
					String query1 = "SELECT * FROM Customer order by cust_no";
					ResultSet rs1 = stmt.executeQuery(query1);
					while(rs1.next()) {
						System.out.println(rs1.getString(1) + '\t' + rs1.getString(2) + '\t' + rs1.getString(3) + '\t' + rs1.getString(4));
					}
					break;
				case 2:
					// Add customer record
					// Accept input for each column from user
					System.out.print("Enter the Customer No.(Must start with C): ");
					String id = Sc.next();
					System.out.print("Enter the Customer Name: ");
					Sc.next();
					String name = Sc.nextLine();
					System.out.print("Enter the Customer Phone Number: ");
					BigInteger phone = new BigInteger(Sc.next());
					System.out.print("Enter the Customer's City: ");
					String city = Sc.next();
					String query2 = "INSERT INTO Customer(CUST_NO, NAME, PHONE_NO, CITY) VALUES('" + id + "', '" + name + "', " + phone + ", '" + city + "' )";

					try {
						stmt.execute(query2);
						System.out.println("Customer added successfully.");
					}catch(Exception e) {
						System.out.println("Unable to add the customer.");
					}

					break;
				case 3:
					// Delete customer record
					// Accept customer number from user
					System.out.print("Enter the Customer No.: ");
					id = Sc.next();
					String query3 = "DELETE FROM Customer WHERE CUST_NO='"+ id +"';";
					boolean success3 = false;
					try {
						success3 = stmt.execute(query3);
						
					}catch(Exception e) {
						System.out.println("Error Occured");
					}
					if(success3) {
						System.out.println("Customer deleted successfully.");
					}else {
						System.out.println("Unable to delete the customer.");
					}
					break;
				case 4:
					// Update customer record
					// Accept customer number from user
					System.out.println("Enter the Customer No.: ");
					id = Sc.next();
					System.out.println("Enter 1: For Name 2: For Phone no 3: For City to update:");
					// Accept user's choice
					int choice_variable_1 = Sc.nextInt();
					String query4;
					switch (choice_variable_1) {
						case 1:
							// Update customer's name
							System.out.println("Enter the name to update: ");
							name = Sc.nextLine();
							query4 = "UPDATE Customer SET NAME='" + name + "' WHERE CUST_NO='" + id + "';";
							try{
								stmt.execute(query4);
								System.out.println("Updated name successfully.");
							}catch(Exception e){
								System.out.println("Unable to update name.");
							}
							break;
						case 2:
							// Update customer's phone number
							System.out.println("Enter the phone to update: ");
							phone = new BigInteger(Sc.next());
							query4 = "UPDATE Customer SET PHONE=" + phone + " WHERE CUST_NO='" + id + "';";
							try{
								stmt.execute(query4);
								System.out.println("Updated phone successfully.");
							}catch(Exception e){
								System.out.println("Unable to update phone.");
							}
							break;
						case 3:
							// Update customer's city
							System.out.println("Enter the city to update: ");
							city = Sc.next();
							query4 = "UPDATE Customer SET CITY='" + city + "' WHERE CUST_NO='" + id + "';";
							try{
								stmt.execute(query4);
								System.out.println("Updated city successfully.");
							}catch(Exception e){
								System.out.println("Unable to update city.");
							}
							break;
					}
					break;
				case 5:
					// Display account details
					// Accept customer number from user
					System.out.println("Enter the Account Number: ");
					String acc = Sc.next();
					String query5 = "SELECT * FROM Account WHERE ACCOUNT_NO='" + acc + "';";
					try{
						ResultSet rs5 = stmt.executeQuery(query5);
						while(rs5.next()){
							System.out.println(rs5.getString(1) + '\t' + rs5.getString(2) + '\t' + rs5.getString(3) + '\t' + rs5.getString(4));
						}
					}catch(Exception e){
						System.out.println("Error displaying the accounts details");
					}
					break;
				case 6:
					// Display loan details
					// Accept customer number from user
					System.out.println("Enter the Customer Number : ");
					id = Sc.next();
					// Display the number of loans the customer has or
					// Congratulation if he customer has no loan
					String query6 = "SELECT C.CUST_NO, COUNT(L.CUST_NO) AS No_Of_Loans FROM Customer C LEFT JOIN Loan L ON C.CUST_NO=L.CUST_NO WHERE C.CUST_NO='" + id +"'GROUP BY C.CUST_NO;";
					ResultSet rs6 = stmt.executeQuery(query6);
					rs6.next();
					int loans = rs6.getInt(2);
					if(loans == 0) {
						System.out.println("Congratulations!!! You have no loans.");
					}else {
						System.out.println("You have " + loans + " pending loans.");
					}
					break;
				case 7:
					// Deposit money
					// Accept the account number to be deposited in
					System.out.println("Enter the account number to deposit money: ");
					String acc7 = Sc.next();
					System.out.println("Enter the amount to deposit: ");
					float amount = Sc.nextFloat();
					String query7 = "UPDATE Account SET BALANCE=(SELECT BALANCE FROM Account WHERE ACCOUNT_NO='" + acc7 + "') + " + amount + " WHERE ACCOUNT_NO='" + acc7 + "';";
					boolean success7 = false;
					try{
						success7 = stmt.execute(query7);
					}catch(Exception e){
						System.out.println("Error while depositing money.");
					}
					// Message for transaction completion
					if(success7){
						System.out.println("Successfully deposited " + amount + " to account " + acc7);
					}
					break;
				case 8:
					// Withdraw money
					// Accept the account number to be withdrawn from
					System.out.println("Enter the account number to withdraw money: ");
					String acc8 = Sc.next();
					System.out.println("Enter the amount to deposit: ");
					float amount8 = Sc.nextFloat();
					// Handle appropriate withdrawl check conditions
					String query8_a = "SELECT BALANCE FROM Account WHERE ACCOUNT_NO='" + acc8 + "';";
					ResultSet rs8 = stmt.executeQuery(query8_a);
					rs8.next();
					int bal8 = (int)(rs8.getInt(1));
					boolean success8 = false;
					if (bal8 < amount8){
						System.out.println("Insufficient balance.");
					}else{
						bal8 -= amount8;
						String query8_b = "UPDATE Account SET BALANCE=" + bal8 + " WHERE ACCOUNT_NO='" + acc8 + "';";
						try{
							stmt.execute(query8_b);
							success8 = true;
						}catch(Exception e){
							System.out.println("Error withdrawing money.");
						}
					}
					// Message for transaction completion
					if(success8){
						System.out.println("Successfully withdrawn " + amount8 + "\nRemaining Balance : " + bal8);
					}
					break;
				case 9:
					// Exit the menu
					System.out.println("Thank you!!!");
					break;
				default:
					// Handle wrong choice of option
					System.out.println("Invalid choice, Please try again!!!");
				}
			} while (choice_variable != 9);
		} // try closing
		catch (Exception e) {
			// Handling exception
			System.out.println(e);
		}
		Sc.close();
	}// main closing
}
