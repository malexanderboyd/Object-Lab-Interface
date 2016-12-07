package ObjectLabEnterpriseSoftware;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
/**
 * FYI I used select * so far in most cases since I don't know what you want it
 * limited to. Some refinement will be needed.
 *
 * @author Database
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!
public class SQLMethods
{
    /* Same url and connection to the DB until it is closed */

    private final String url;
    private Connection conn;
    private ResultSet res;
    private PreparedStatement stmt;
    public SQLMethods()
    {
        /* To resolve hostname to an IP adr */
    	File f = new File(MainView.getStorageDir() + ":\\Sync\\computername.txt");
    	//File f = new File("/home/alex/Documents/School/Spring 2016/Software eng/Object-Lab-Interface/computername.txt");
        String line, ip = "";

        try
        {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(f.getAbsolutePath());
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null)
            {
                ip = line;
            }

            // Always close files.
            bufferedReader.close();
        } catch (IOException ex)
        {
            System.out.println("Couldn't read file! IOException!");
            ex.printStackTrace();
        }
       
        ///////////////////////////////////
        ///ip = "mysql1110.ixwebhosting.com";  ////////
        ///////////////////////////////////
        //url = "jdbc:mysql://" + ip + ":3306/";
        //connectToDatabase("com.mysql.jdbc.Driver", url + "AAAlvxm_oli", "AAAlvxm_oliAdmin", "Password1");
        
        //For online database
        
        String host = "db4free.net";
        String port = "3306";
        String user = "oliadminuser";
        String pass = "olipass";
        String db   = "olidatabase";
        String driver = "com.mysql.jdbc.Driver";
        
        
        //Russell's local database
        /*
        String host = "127.0.0.1";
        String port = "3306";
        String user = "root";
        String pass = "password";
        String db   = "localoli";
        String driver = "com.mysql.jdbc.Driver";
        */
        
        url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        connectToDatabase(driver, url, user, pass);
    }
    
    private void connectToDatabase(String driver, String urlDatabaseName, String userName, String pw)
    {
        try
        {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(urlDatabaseName, userName, pw);
        } catch (ClassNotFoundException e)
        {
            System.out.println("Driver class not found / created. Exception!\n" + e);
        } catch (InstantiationException | IllegalAccessException | SQLException e)
        {
            System.out.println(e);
        }
    }

    public void closeDBConnection()
    {
        try
        {
            conn.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    	// Begining of Select Methods
    // _______________________________________________________________________________________________________________________
    public ResultSet selectAllFromTable(String table)
    {// select entire table
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM " + table + ";");
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectAllPrintStatus(String status)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM job WHERE status = ?;");
            stmt.setString(1, status);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }
    
    public ResultSet selectFileInfo(int jobid)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT file_name, file_path FROM job WHERE job_id = ?;");
            stmt.setInt(1, jobid);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }
    
    public ResultSet searchPrinterFilesTypes(String printer)
    {// selects the filetypes for the printer
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT file_extension FROM printer WHERE printer_name = ?;");
            stmt.setString(1, printer);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet searchJobsStatusPrinter(String status, String printer) // returns filename,first name,lastname ,submission_date, printer for based off status and printer
    {
        res = null;
        try
        {
            
        	stmt = this.conn.prepareStatement("SELECT job.job_id, job.file_name, users.first_name, users.last_name, "
                    + "job.submission_date ,job.printer_name, class_name, class_section, material.z_corp_plaster, material.objet_build, material.objet_support " 
                    + "FROM job INNER JOIN users ON users.towson_id = job.student_id "
                    + "INNER JOIN class ON job.class_id = class.class_id INNER JOIN material ON material.id = job.student_id WHERE job.status = ? AND "
                                   + "printer_name = ?;");
        	
        	stmt.setString(1, status);
            stmt.setString(2, printer);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return res;
    }
    
    public ResultSet searchStudentBalanceFName(String first_name)
    {
        res = null;
        try 
        {
            stmt = this.conn.prepareStatement("SELECT first_name, last_name, towson_id, z_corp_plaster, objet_build, objet_support " +
                                              "FROM users INNER JOIN material ON users.towson_id = material.id " +
                                               "WHERE users.first_name = ?;"); 
            stmt.setString(1, first_name);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public ResultSet searchStudentBalanceLName(String last_name)
    {
        res = null;
        try 
        {
            stmt = this.conn.prepareStatement("SELECT first_name, last_name, towson_id, z_corp_plaster, objet_build, objet_support " +
                                              "FROM users INNER JOIN material ON users.towson_id = material.id " +
                                               "WHERE users.last_name = ?;"); 
            stmt.setString(1, last_name);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public ResultSet searchStudentBalanceId(String id)
    {
        res = null;
        try 
        {
            stmt = this.conn.prepareStatement("SELECT first_name, last_name, towson_id, z_corp_plaster, objet_build, objet_support " +
                                              "FROM users INNER JOIN material ON users.towson_id = material.id " +
                                               "WHERE towson_id = ?;"); 
            stmt.setString(1, id);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public ResultSet searchStudentBalance()
    {
        res = null;
        try 
        {
            stmt = this.conn.prepareStatement("SELECT first_name, last_name, towson_id, z_corp_plaster, objet_build, objet_support " +
                                              "FROM users INNER JOIN material ON users.towson_id = material.id;"); 
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    /*
    public ResultSet searchAllJobsStatusPrinter(String printer) // returns filename,first name,lastname ,submission_date, printer for based off status and printer
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT job.job_id, job.file_name, users.first_name, users.last_name, "
					+ "job.submission_date ,job.printer_name, class_name, class_section  " 
					+ "FROM job, users , class " + "WHERE job.status <> ? AND printer_name = ? "
					+ "AND users.towson_id = job.student_id AND job.class_id = class.class_id;");
            stmt.setString(1, "rejected");
            stmt.setString(2, printer);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return res;
    }
    */
    
    public ResultSet searchStudentTransactionHistory(String id)
    {
    	res = null;
        try 
        {
            stmt = this.conn.prepareStatement("SELECT date, material, amount " +
                                              "FROM material_transaction_history "+
                                               "WHERE net_id = ?;"); 
            stmt.setString(1, id);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public ResultSet getStudentFileStatus(String studentId)
    {
        res = null;
        try 
        {
            stmt = this.conn.prepareStatement("SELECT file_name, submission_date, comment, status " +
                                              "FROM job INNER JOIN job_stats ON job.job_id = job_stats.job_id " +
                                              "WHERE ? = job.student_id"); 
            stmt.setString(1, studentId);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public ResultSet getStudentInfo(String studentId)
    {
        res = null;
        try 
        {
            stmt = this.conn.prepareStatement("SELECT id, z_corp_plaster, objet_build, objet_support " +
                                              "FROM material WHERE ? = material.id"); 
            stmt.setString(1, studentId);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public double getCurrentMaterialBalance(String studentId, String whichMaterial)
    {
        double current = 0;
        try
        {
        ResultSet queryResult; 
        stmt = this.conn.prepareStatement("SELECT " + whichMaterial + " FROM `material` WHERE id = ?;");
        stmt.setString(1, studentId);
        queryResult = stmt.executeQuery();
        while(queryResult.next())
        {
            current = queryResult.getDouble(whichMaterial);
        }
        } catch (SQLException e)
        {
            System.err.println(e);
        }
        return current;
        
    }
    
    public String getFilePrinter(String fileName, String id)
    {               
        String printer = "";
        try
        {    
            ResultSet queryResult;
            stmt = this.conn.prepareStatement("SELECT printer_name FROM job WHERE job.file_name = ? AND job.student_id = ?;");
            stmt.setString(1, fileName);
            stmt.setString(2, id);
            queryResult = stmt.executeQuery();
            while(queryResult.next())
        {
            printer = queryResult.getString("printer_name");
        }
        } catch (SQLException e)
        {
            System.err.println(e);
        }
        return printer;
        
    }
    
    public String getStudentId(String first_name, String last_name)
    {
             
        String id = "";
        try
        {    
            ResultSet queryResult;
            stmt = this.conn.prepareStatement("SELECT towson_id FROM users WHERE users.first_name = ? AND users.last_name = ?;");
            stmt.setString(1, first_name);
            stmt.setString(2, last_name);
            queryResult = stmt.executeQuery();
            while(queryResult.next())
        {
            id = queryResult.getString("towson_id");
        }
        } catch (SQLException e)
        {
            System.err.println(e);
        }
        return id;
        
    }
    
    public void addMaterial(String studentId, double amount, String whichMaterial)
    {
        try
        {
        double current; 
        SQLMethods dbconn = new SQLMethods();
        current = dbconn.getCurrentMaterialBalance(studentId, whichMaterial);
        amount += current;
        stmt = this.conn.prepareStatement("UPDATE material SET "+ whichMaterial+" = " + amount + " WHERE id = ?;");
        stmt.setString(1, studentId);
        stmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE,
                    null, ex);
        } 
    }
  
    public void addTransactionHistory(String studentID, String material, double amount)
    {
    	try
        {
    		SQLMethods dbconn = new SQLMethods();
    		stmt = this.conn.prepareStatement("INSERT INTO material_transaction_history (date, net_id, material, amount) "
    									    + "VALUES (NOW(), ?, ?, ?)");
    		stmt.setString(1, studentID);
    		stmt.setString(2, material);
    		stmt.setDouble(3, amount);
    		stmt.execute();
        } 
    	catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE,null, ex);
        } 
    }    
    
    
    public ResultSet searchJobsStatus(String status) // returns filename,first name,lastname ,submission_date, printer for based off status and printer
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT job.file_name, users.first_name, users.last_name, "
					+ "job.submission_date ,job.printer_name, class_name, class_section " 
					+ "FROM job, users ,class " + "WHERE job.status = ? "
					+ "AND users.towson_id = job.student_id AND job.class_id = class.class_id;");
            stmt.setString(1, status);
            
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return res;
    }
    
    
    
    
    

    public ResultSet searchPrintersByBuildName(int buildId)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM job WHERE build_id = ?;");
            stmt.setInt(1, buildId);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet selectIDFromJob(String id)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM job Where student_id = ?");
            stmt.setString(1, id);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet searchJobsByBuildName(String buildName)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM job, printer_build "
                    + "WHERE buildName = ?  AND printer_build.build_id= Job.build_id;");
            stmt.setString(1, buildName);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchFilePath(String fileName)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT file_path FROM job WHERE file_name = ? ;");
            stmt.setString(1, fileName);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    
    public ResultSet getStudentSubmissionStatusFromDevice(String printer)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement
            (
                    "SELECT student_submission "
                    + "FROM printer "
                    + "WHERE printer_name = ?"
            );
            
            stmt.setString(1, printer);
            
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet searchPrinterSettings(String printer)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM printer WHERE printer_name = ?");
            
            stmt.setString(1, printer);
            
            res = stmt.executeQuery();
            System.out.println(stmt);
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectClasses()
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM class;");
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet selectPassFromadmin(String admin)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT pass FROM admin Where username = ?; ");
            stmt.setString(1, admin);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectJobForClass(int classes, String status)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM  job Where class_id = ? AND status = ?;");
            stmt.setInt(1, classes);
            stmt.setString(2, status);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectColumnNames(String printer)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM custom_printer_column_names WHERE printer_name = ?;");
            stmt.setString(1, printer);
            res = stmt.executeQuery();

        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }
    
    /*
    This is a method that retrieve custom_field_names from custom_printer_column_names. 
    I use this to dynamically update the enter build info in the tables
    The other methods meant to do this were not working. ~Sean Gahagan
    */
    public ResultSet selectDeviceTrackableMetaData(String printer)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement
            (
                    "SELECT custom_field_name, numerical FROM custom_printer_column_names WHERE printer_name = ?;"
                    
            );
            
            stmt.setString(1, printer);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet selectBuildData(int id)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT * FROM column_build_data Where build_id = ? ");
            res = stmt.executeQuery();
            stmt.setInt(1, id);
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectAcceptedFiles(String printer)
    {

        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT file_extension FROM accepted_files Where printer_name = " + printer +";");
            //stmt.setString(1, printer);
            res = stmt.executeQuery();

        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }

    public ResultSet selectBuildLocation(String printer){
    
        //select file_path from jobsdb.job where file_name = 'stuff2_2015-05-06_00-55-42.zpr';
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("select file_path from job where file_name = ?;");
            stmt.setString(1, printer);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }
    
    public ResultSet selectTableHeader(String printer)
    {

        res = null;
        try
        {
            stmt = this.conn.prepareStatement("SELECT custom_field_name  FROM custom_printer_column_name Where printer_name = ?;");
            stmt.setString(1, printer);
            res = stmt.executeQuery();
        } catch (SQLException e)
        {
            System.err.println("SQL Execution Error.");
        }
        return res;
    }
    
    /* Returns the nubmer of times build file location is found in "table".
        -1 is returned on query execution failure.
    */
    public int doesBuildFileLocationExist(String table, String buildFileLocation)
    {
        try
        {
            stmt = conn.prepareStatement("SELECT COUNT(*) as does_build_flocation_exist FROM " + table + " WHERE build_name = ?;");
            stmt.setString(1, buildFileLocation);
            
            System.out.println(stmt);
            
            ResultSet occurrencesOfBuildFileLocation = stmt.executeQuery();
           
            if(occurrencesOfBuildFileLocation.next())
                 return occurrencesOfBuildFileLocation.getInt("does_build_flocation_exist");
            else
                return -1;
            
        } catch (SQLException e)
        {
            System.err.println(e);
            return -1;
        }
    }
	// END OF SELECT METHODS
    // _____________________________________________________________________________________________________________________
	// BEGGINING OF INSERT METHODS
    // _____________________________________________________________________________________________________________________

    public void insertIntoClasses(String className, String classSection, String professor)
    {
        try
        {
            stmt = conn.prepareStatement("INSERT INTO class (class_name, class_section, professor) values (?,?,?)");
            stmt.setString(1, className);
            stmt.setString(2, classSection);
            stmt.setString(3, professor);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertIntoJob(String filename, String filePath, int class_id, String user_id, String printer)
    {
        try
        {
        	System.out.print("Trying to insert into db...~Alex debug");
            stmt = conn.prepareStatement("INSERT INTO job (file_name, file_path, class_id, student_id, printer_name, submission_date, build_Name, status, comment) values (?,?,?,?,?,NOW(),null,'pending',null);");
            stmt.setString(1, filename);
            stmt.setString(2, filePath);
            stmt.setInt(3, class_id);
            stmt.setString(4, user_id);
            stmt.setString(5, printer);
            stmt.executeUpdate();
            
            
            
            // Adding stat tracking
            
            
            stmt = conn.prepareStatement
        			(
        					"SELECT job_id FROM job WHERE  student_id =  ? AND  file_name =  ?"
        			);
        			
        	stmt.setString(1, user_id);
            stmt.setString(2, filename);
            
            System.out.println(stmt.toString());
            ResultSet res = stmt.executeQuery();
            res.next();
            insertIntoJobStats(res.getInt(1), null, null);
            
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public void insertIntoJobStats(int id, String stat1, String stat2)
    {
    	try {
    	 stmt = conn.prepareStatement("INSERT INTO job_stats (job_id, stat1, stat2) values (?,NULL,NULL);");
         stmt.setInt(1, id);
         stmt.executeUpdate();
    	  } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    
    

    public void insertIntoPrinter(String printer, boolean submit)
    {
        try
        {
            stmt = conn.prepareStatement("INSERT INTO printer (printer_name, student_submission, current) values (?, ?, ?);");
            stmt.setString(1, printer);
            stmt.setBoolean(2, submit);
            stmt.setInt(3, 1);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	public ResultSet checkUserExists(String userID)
	{
		res = null;
		try
		{
			stmt = conn.prepareStatement
			(
					"SELECT towson_id FROM users WHERE towson_id = ?;"
			);
			
			stmt.setString(1, userID);
			//boolean to integer, 1=true, 0=false
            res = stmt.executeQuery();
			return res;
		} catch (Exception e)
        {
            e.printStackTrace();
        }
		return res;
	}
	
    public int insertIntoUsers(String idusers, String firstName, String lastName, String email)
    {
        try
        {
            stmt = conn.prepareStatement
            (
                    "INSERT INTO users(towson_id, first_name, last_name, email) "
                    + "values (?,?,?,?) "
                    + "ON DUPLICATE KEY UPDATE "
                    + "towson_id = VALUES(towson_id), first_name = VALUES(first_name), last_name = VALUES(last_name), email = VALUES(email);"
            );
            
            stmt.setString(1, idusers);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            return stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertIntoMaterial(String id)
    {
    	try
        {
            stmt = conn.prepareStatement
            (
                    "INSERT INTO material(id, z_corp_plaster, objet_build, objet_support) "
                  + "values (?,?,?,?) "
            );
            
            stmt.setString(1, id);
            stmt.setDouble(2, 0);
            stmt.setDouble(3, 0);
            stmt.setDouble(4, 0);
            stmt.execute();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void insertIntoAdmin(String user_id, String userName, String pass)
    {
        try
        {
            stmt = conn.prepareStatement("insert into admin (user_id, username, date_created, pass) values (?,?,NOW(),?);");
            stmt.setString(1, user_id);
            stmt.setString(2, userName);
            stmt.setString(3, pass);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //Rajewski
    //No longer called with these params in UtilController
    /*
    public void insertIntoBuild(String buildname, int runtime, int models, String printer)
    {
        try
        {
            stmt = conn.prepareStatement("INSERT INTO printer_build "
                    + "(build_name, date_created, total_runtime_seconds, number_of_models, printer_name) "
                    + "VALUES (?, NOW(), ?, ?, ?);");
            
            stmt.setString(1, buildname);
            stmt.setInt(2, runtime);
            stmt.setInt(3, models);
            stmt.setString(4, printer);
            
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    */
    
    public void insertIntoBuild(String buildname, int models, String printer)
    {
        try
        {
            stmt = conn.prepareStatement("INSERT INTO printer_build "
                    + "(build_name, date_created, total_runtime_seconds, number_of_models, printer_name) "
                    + "VALUES (?, NOW(), 0, ?, ?);");
            
            stmt.setString(1, buildname);
            stmt.setInt(2, models);
            stmt.setString(3, printer);
            
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void insertIntoColumnBuildData(String printer, String columnName, String data, String buildLocation)
    {
        try
        {
            stmt = this.conn.prepareCall
            (
                    "{call enterBuildData(?, ?, ?, ?)}"
            );
            
            stmt.setString(1, printer);
            stmt.setString(2, columnName);
            stmt.setString(3, data);
            stmt.setString(4, buildLocation);
            stmt.executeQuery();
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertIntoCustom(String printer, String name, boolean num)
    {
        try
        {
            stmt = conn.prepareStatement("insert into custom_printer_column_names ( printer_name, custom_field_name ,numerical) values (?,? ,?);");
            stmt.setString(1, printer);
            stmt.setString(2, name);
            stmt.setBoolean(3, num);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertIntoAcceptedFiles(String printer, String file)
    {
        try
        {
            stmt = conn.prepareStatement("insert into accepted_files ( printer_name, file_extension) values (?,?);");
            stmt.setString(1, printer);
            stmt.setString(2, file);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	// END OF INSERT METHODS
    // _____________________________________________________________________________________________________________________
	// BEGGINIGNG OF UPDATE METHODS
    // _____________________________________________________________________________________________________________________
    public void changeJobStatus(int id, String status)
            throws SQLException
    {
        stmt = this.conn.prepareStatement("UPDATE job SET status = ? WHERE job_id = ?;");
        stmt.setString(1, status);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    public void updateJobFLocation(int id, String fLocation)
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE job SET file_path = ?" + " WHERE job_id = ?;");
            stmt.setString(1, fLocation);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public void updateJobVolume(int id, double volume)
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE job SET volume = "
                    + volume + " WHERE job_id = ?;");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
    
    
    public void updateStats(int id, String stat1, String stat2)
    {
    	try {
    		stmt = this.conn.prepareStatement("UPDATE `job_stats` SET `stat1`=?,`stat2`=? WHERE `job_id`=?");
    		stmt.setString(1, stat1);
    		stmt.setString(2, stat2);
    		stmt.setInt(3, id);
    		stmt.executeUpdate();
    	} catch(SQLException ex) {
    	       Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE,
                       null, ex);
    	}
    }

    public void updateJobBuildName(String build, int jobid)//SEAN USE THIS/
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE job SET build_name = ? WHERE job_id = ?;");
            stmt.setString(1, build);
            stmt.setInt(2, jobid);
            stmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public void updateFirstName(String updatedFirstName, String id)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("UPDATE users SET first_name = ? WHERE towson_id = ?;");
            stmt.setString(1, updatedFirstName);
            stmt.setString(2, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateLastName(String updatedLastName, String id)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("UPDATE users SET last_name = ? WHERE towson_id = ?;");
            stmt.setString(1, updatedLastName);
            stmt.setString(2, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateStatus(String statusUpdate, int primaryKey)
    {
        res = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE job SET status = ? WHERE job_id = ?  ");
            stmt.setString(1, statusUpdate);
            stmt.setInt(2, primaryKey);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updatePassword(String password, String username)
    {
        res = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE admin SET pass= ? WHERE username= ?; ");
            stmt.setString(1, password);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateEmail(String newEmail, String id)
    {
        res = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE users SET email= ? WHERE towson_id= ?; ");
            stmt.setString(1, newEmail);
            stmt.setString(2, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getFileComment(String file)
    {
        String comment = "";
        try
        {
            stmt = conn.prepareStatement("SELECT comment FROM job WHERE file_name = ?; ");
            stmt.setString(1 , file);
            res = stmt.executeQuery();
            while(res.next())
            {
                comment = res.getString("comment");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return comment;
    }
    
    public void updateFileComment(String file, String comment)
    {
        res = null;
        try
        {
            stmt = conn.prepareStatement("UPDATE job SET comment = ? WHERE file_name = ?; ");
            stmt.setString(1, comment);
            stmt.setString(2, file);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void updateColumnFieldName(String updatedName, int id)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("UPDATE custom_printer_column_names SET column_field_name = ? WHERE column_names_id = ? ;");
            stmt.setString(1, updatedName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateColumnFieldData(String data, int columnId, String buildName)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement("UPDATE column_build_data SET column_field_data =? Where column_name_id = ? AND build_name = ? ;");
            stmt.setString(1, data);
            stmt.setInt(2, columnId);
            stmt.setString(3, buildName);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updatePrinterFileExtension(String printer_name, String file_extension)
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE printer SET file_extension = ? WHERE printer_name = ?;");
            stmt.setString(1, file_extension);
            stmt.setString(2, printer_name);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePrinterCurrent(String printer_name, String current)
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE printer SET current  = ? WHERE printer_name = ?;");
            stmt.setString(1, current);
            stmt.setString(2, printer_name);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePrinterBuildDateCreated(String buildName, String date_created)
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE printer_build SET date_created = NOW() WHERE build_name = ?;");
            stmt.setString(1, buildName);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePrinterBuildTotalRuntimeSeconds(String buildName, int total_runtime_seconds)
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE printer_build SET total_runtime_seconds = ? WHERE build_name = ?;");
            System.out.println(stmt);
            stmt.setInt(1, total_runtime_seconds);
            stmt.setString(2, buildName);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePrinterBuildNumberOfModels(String buildName, int number_of_models)
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE printer_build SET number_of_models =? WHERE build_name = ?;");
            System.out.println(stmt);
            stmt.setInt(1, number_of_models);
            stmt.setString(2, buildName);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	// END OF UPDATE METHODS
    // _____________________________________________________________________________________________________________________
	// BEGINGING OF DELETE METHODS
    // _____________________________________________________________________________________________________________________
    public void deletebyID(int id)
    {
        try
        {
            stmt = this.conn.prepareStatement("DELETE FROM job WHERE submission_id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteByBuildId(String buildid)
    {
        try
        {
            stmt = this.conn.prepareStatement("DELETE FROM job WHERE build_name = ?;");
            stmt.setString(1, buildid);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteFromJob(int id)
    {
        try
        {
            stmt = conn.prepareStatement("DELETE FROM job WHERE job_id = ?; ");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deletePrinter(String printerName)
    {
        try
        {
            stmt = conn.prepareStatement("UPDATE printer SET current = 0 where printer_name = ?; ");
            stmt.setString(1, printerName);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteFromUser(String id)
    {
        try
        {
            stmt = conn.prepareStatement("DELETE FROM users WHERE towson_id = ?; ");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteFromClass(int class_id)
    {
        try
        {
            stmt = conn.prepareStatement("DELETE FROM class WHERE class_id = ?; ");
            stmt.setInt(1, class_id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void clearData(){
    
        try
        {
            stmt = conn.prepareStatement("call clearTables();");
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }

    public void deleteFromAdmin(String id)
    {
        try
        {
            stmt = conn.prepareStatement("delete from admin where user_id= ?; ");
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteFromBuild(String buildName)
    {
        try
        {
            stmt = this.conn.prepareStatement("DELETE FROM printer_build WHERE build_name = ?");
            stmt.setString(1, buildName);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void deleteColumnName(int primaryKey)
    {
        try
        {
            stmt = conn.prepareStatement("delete from custom_printer_column_names where column_names_id= ?; ");
            stmt.setInt(1, primaryKey);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteColumnData(String buildName, int columnId)
    {
        try
        {
            stmt = conn.prepareStatement("delete from column_build_data where build_name= ? AND column_name_id = ?;");
            stmt.setString(1, buildName);
            stmt.setInt(2, columnId);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	// END OF DELETE METHODS
    // _____________________________________________________________________________________________________________________
    public ResultSet getReport(String printer_name)
    {
    	res = null;
        try
        {
          /*  stmt = this.conn.prepareStatement(
                    "call report('" + printer_name + "');"
            );*/
        	// temp fix to get reports view working
        	   //stmt = this.conn.prepareStatement(
               //        "SELECT * FROM custom_printer_column_names WHERE printer_name = ?;"
               //);
               //stmt.setString(1, printer_name);
        	/*
        	stmt = this.conn.prepareStatement("SELECT job.job_id, job.file_name, users.first_name, users.last_name, "
                    + "job.submission_date ,job.printer_name, class_name, class_section, job.volume " 
                    + "FROM job INNER JOIN users ON users.towson_id = job.student_id "
                    + "INNER JOIN class ON job.class_id = class.class_id WHERE job.status = ? AND "
                                   + "printer_name = ?;");
        	*/
        	stmt = this.conn.prepareStatement("SELECT job.job_id, job.file_name, users.first_name, users.last_name, "
                    + "job.submission_date ,job.printer_name, class_name, class_section, job_stats.stat1, job_stats.stat2 " 
                    + "FROM job INNER JOIN job_stats ON job.job_id = job_stats.job_id "
                             + "INNER JOIN users ON users.towson_id = job.student_id "
                             + "INNER JOIN class ON job.class_id = class.class_id WHERE job.status = 'completed' AND "
                                  + "printer_name = ?;");
        	
        	stmt.setString(1, printer_name);
        	//stmt.setString(1, "completed");
        	
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return res;
    }

    public ResultSet getReport(String column, String value, String printer_name)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement(
                    "call reportFiltered('" + printer_name + "','" + column + "','" + value + "');"
            );

            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return res;
    }

    public ResultSet searchPending()
    {
		return selectAllPrintStatus("pending");
	}

    @Deprecated
    public ResultSet searchPrintersByBuildName(String buildName, String printer)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM "
                    + printer + " "
                    + "WHERE "
                    + "buildName = ?");

            stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
    @Deprecated
    public ResultSet searchIncompletePendingByBuild(String buildName)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "status = 'incomplete' "
                    + "AND buildName = ?");
            stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    @Deprecated
    public ResultSet searchPendingByBuildName(String buildName)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "buildName = '" + buildName + "'");
            //stmt.setString(1, buildName);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public ResultSet searchWithJobID(String ID) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT file_name as fileName, submission_date, class_name, class_section, first_name, last_name, email "
                    + "FROM job, users, class "
                    + "WHERE "
                    + "job_id = ? AND towson_id=student_id AND job.class_id= class.class_id ");
            stmt.setString(1,ID );
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public ResultSet searchID( String id) {
        res = null;
        try {
            stmt = this.conn.prepareStatement(
                    "SELECT job_id, file_path ,printer_name "
                    + "FROM job  "
                    + "WHERE "
                    + " file_name = ? ;");
            stmt.setString(1, id); 
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Deprecated
    public void delete(String table, String id) throws SQLException
    {
        stmt = this.conn.prepareStatement(
                "DELETE "
                + "FROM " + table + " "
                + "WHERE idJobs = ?");
        // stmt.setString(1, table);
        stmt.setString(1, id);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    @Deprecated
    public void approve(String id) throws SQLException
    {
        stmt = this.conn.prepareStatement(
                "UPDATE pendingjobs "
                + "SET status = 'approved', "
                + "dateUpdated = now() "
                + "WHERE idJobs = ?");
        stmt.setString(1, id);
        System.out.println(stmt);
        stmt.executeUpdate();
    }

    @Deprecated
    public void updatePendingJobFLocation(int idJob, String fLocation)
    {
        try
        {
            stmt = this.conn.prepareStatement(
                    "UPDATE jobs"
                    + " SET filePath = '" + fLocation + "'"
                    + " WHERE idJobs = '" + idJob + "'"
            );

            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Deprecated
    public void updateToPrint(String idJob) throws SQLException
    {
        stmt = this.conn.prepareStatement(
                "UPDATE pendingjobs "
                + "SET status = 'to print' "
                + "WHERE idJobs = '?';");
        stmt.setString(1, idJob);
        System.out.println(stmt);
    }

    /* This querys for jobs that have been approved (status = approved). 
     With this query we can display the student's submissions that needs to
     be inserted into a build. The search is based on the name of the printer.
     */
    @Deprecated
    public ResultSet searchApprovedJobsNotPrinted(String printer)
    {
        res = null;

        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT fileName, dateStarted "
                    + "FROM pendingjobs "
                    + "WHERE "
                    + "printer = ? "
                    + "AND status = 'approved'"
            );

            stmt.setString(1, printer);
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    @Deprecated
    public void updatePendingJobVolume(String idJob, double volume)
    {
        try
        {
            stmt = this.conn.prepareStatement(
                    "UPDATE pendingjobs "
                    + "SET volume = " + volume + " "
                    + "WHERE idJobs = '" + idJob + "'");
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Deprecated
    public void updatePendingJobsBuildName(String build, String fileName)
    {
        try
        {
            stmt = this.conn.prepareStatement(
                    "UPDATE pendingjobs "
                    + "SET buildName = ? WHERE filename = ?");
            stmt.setString(1, build);
            stmt.setString(2, fileName);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Deprecated
    public void insertIntoSolidscape(String bn, int models, double resolution, String BuildTime, String comment, double cost)
    {
        String date = "(CURTIME())";
        try
        {
            stmt = this.conn.prepareStatement(
                    "INSERT "
                    + "INTO solidscape "
                    + "(idSolidscape, "
                    + "buildName, "
                    + "dateRun, "
                    + "noModels, "
                    + "resolution, "
                    + "runTime, "
                    + "comments, "
                    + "costOfBuild) "
                    + "VALUES"
                    + "(DATE_FORMAT(NOW(), '%Y-%m-%d_%H-%i-%s_" + bn + "'), '"
                    + "" + bn + "', " + date + ", '" + models + "', '" + resolution + "', '" + BuildTime + "', '" + comment + "', '" + cost + "')");
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Deprecated
    public void insertIntoZcorp(String bn, double mb, double yb, double mab, double cb, double ci, int models, String comment, double cost, String status)
    {
        String id = "DATE_FORMAT(NOW(), '%Y-%m-%d_%H-%i-%s_" + bn + "')";
        String date = "(CURTIME())";
        try
        {
            stmt = this.conn.prepareStatement(
                    "INSERT "
                    + "INTO zcorp "
                    + "(idZcorp, "
                    + "buildName, "
                    + "dateRun, "
                    + "monoBinder, "
                    + "yellowBinder, "
                    + "magentaBinder, "
                    + "cyanBinder, "
                    + "cubicInches, "
                    + "noModels, "
                    + "comments, "
                    + "costOfBuild, "
                    + "status) "
                    + "VALUES (" + id + ", '" + bn + "'," + date + ", '" + mb + "', '" + yb + "', '" + mab + "', '" + cb + "', '" + ci + "', '" + models + "', '" + comment + "','" + cost + "','" + status + "')");
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Deprecated
    public void insertIntoObjet(String bn, double bc, double sc, int models, String bm, double resolution, String comment, double cost)
    {
        String date = "(CURTIME())";
        try
        {
            stmt = this.conn.prepareStatement(
                    "INSERT INTO objet "
                    + "(idObjet, "
                    + "buildName, "
                    + "dateRun, "
                    + "buildConsumed, "
                    + "supportConsumed, "
                    + "noModels, "
                    + "buildMaterials, "
                    + "resolution, "
                    + "comments, "
                    + "costOfBuild) "
                    + "VALUES(DATE_FORMAT(NOW(), '%Y-%m-%d_%H-%i-%s_" + bn + "'),"
                    + "'" + bn + "'," + date + ",'" + bc + "','" + sc + "','" + models
                    + "','" + bm + "','" + resolution + "','" + comment + "','" + cost + "');"
            );

            if (stmt.executeUpdate() == 1)
            {
                System.out.print("Successfully inserted value");
            }

            System.out.println(stmt);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setAllClassesInvisible()
    {
        try
        {
            stmt = this.conn.prepareStatement("UPDATE class SET current = false");
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateCurrentClasses(int pk)
    {
        try
        {
            stmt = this.conn.prepareStatement(
                    "UPDATE class "
                    + "SET current = true where class_id=?;");
            stmt.setInt(1, pk);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException ex)
        {
            Logger.getLogger(SQLMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Deprecated
    public void insertIntoCompletedJobs(String id, String printer, String firstName, String lastName, String course, String section, String fileName, String filePath,
            String dateStarted, String status, String email, String comment, String buildName, Double volume, Double cost)
    {
        try
        {
            stmt = this.conn.prepareStatement("INSERT INTO completedJobs "
                    + "(idCompJobs, "
                    + "printer, "
                    + "firstName, "
                    + "lastName, "
                    + "course, "
                    + "section, "
                    + "fileName, "
                    + "filePath, "
                    + "dateStarted, "
                    + "dateCompleted, "
                    + "status, "
                    + "email, "
                    + "comment, "
                    + "buildName, "
                    + "volume, "
                    + "cost) "
                    + "VALUES"
                    + "('" + id + "', "
                    + "'" + printer + "', "
                    + "'" + firstName + "', '"
                    + "" + lastName + "', "
                    + "'" + course + "', "
                    + "'" + section + "', "
                    + "'" + fileName + "', "
                    + "'" + filePath + "', "
                    + "'" + dateStarted + "', "
                    + "(CURDATE()), "
                    + "'pending', "
                    + "'" + email + "', "
                    + "'" + comment + "', "
                    + "'" + buildName + "', "
                    + "'" + volume + "', "
                    + "'" + cost + "')");
            stmt.executeUpdate();
            System.out.println(stmt);
            System.out.println("Complete Record Entered!");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	public ResultSet getBuilds()
	{
		res = null;
		try
		{
            stmt = this.conn.prepareStatement(
                    "SELECT build_name "
                    + "FROM printer_build");	
        System.out.println(stmt);
		res = stmt.executeQuery();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}
        
        //Rajewski
        //function to populate new table on build view
        public ResultSet getBuildsForRecordsTable(String currentDevice)
	{
		res = null;
		try
		{
            stmt = this.conn.prepareStatement(
                    "SELECT build_name, date_created, number_of_models "
                    + "FROM printer_build WHERE printer_name = ?;");
            stmt.setString(1, currentDevice);
            
            
        System.out.println(stmt);
		res = stmt.executeQuery();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}
	
    public ResultSet getClasses(boolean status)
    {
        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT * "
                    + "FROM class "
                    + "WHERE "
                    + "current = ?;");

            stmt.setBoolean(1, status);

            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
	
	public ResultSet getAllDeviceNames()
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT printer_name "
                    + "FROM printer;"
            );
			
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
	}
	public ResultSet getCurrentDevices()
	{
		return getDeviceNamesCurrentOption(true);
	}
	
	public ResultSet getDeviceNamesCurrentOption(final boolean current)
    {
        res = null;
    	Thread runner = new Thread() {
			public void run()
			{
        try
        {
            stmt = conn.prepareStatement(
                    "SELECT printer_name "
                    + "FROM printer "
		    + "WHERE current = ?;"
            );
			
			stmt.setBoolean(1, current);
			
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    	};
    	runner.run();
    	return res;
    }
	
	public ResultSet getTrackedDevices()
	{
		return getDeviceNamesCurrentOptionSubmissionOption(true, true);
	}	 
	
    public ResultSet getDeviceNamesCurrentOptionSubmissionOption(boolean current, boolean requireStudentSubmission)
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT printer_name "
                    + "FROM printer "
                    + "WHERE current = ? AND student_submission = ?;"
            );
			
			stmt.setBoolean(1, current);
			stmt.setBoolean(2, requireStudentSubmission);
			
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }
	
    public ResultSet getCurrentTime()
    {
        res = null;
        try
        {
            stmt = this.conn.prepareStatement(
                    "SELECT (DATE_FORMAT(NOW(), '%Y-%m-%d_%H-%i-%s'))"
            );
            System.out.println(stmt);
            res = stmt.executeQuery();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

	public ResultSet findJobsforDevice(String selectedDevice) {
		// TODO Auto-generated method stub
		return null;
	}
}
